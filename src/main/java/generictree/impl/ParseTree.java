package generictree.impl;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;
import generictree.task.TaskNegate;
import generictree.task.TaskUnwrap;
import generictree.node.ParseTreeNode;
import tokenizer.composite.CharTok;
import tokenizer.iface.IStringParser;

import java.util.ArrayList;
import java.util.List;

/** For cases where a delimiter-separated string will be split
 *  into a tree, e.g. a&b&!(b|c)
 *
 * @param <T> the IGTreeNode payload type
 */
public class ParseTree<T> extends GTreeBase <T> {
    private static final String AND = "&";
    private static final String OR = "|";
    private static final char NEGATE_SYMBOL = '!';
    private static final char WRAP_SYMBOL_OPEN = '(';
    private static final char WRAP_SYMBOL_CLOSE = ')';
    private final IStringParser tokenizer;

    private final IGTreeTask<T> taskNegate;
    private final IGTreeTask<T> taskUnwrap;

    public ParseTree() {
        taskNegate = new TaskNegate<>(NEGATE_SYMBOL);
        taskUnwrap = new TaskUnwrap<>(WRAP_SYMBOL_OPEN, WRAP_SYMBOL_CLOSE);
        tokenizer = new CharTok().setStartPos(10).setDelimiter(" ").setSkipSymbols(WRAP_SYMBOL_OPEN +"'");
    }

    @Override
    public IGTreeNode<T> add(T payload, String... path) {
        root = new ParseTreeNode<>();
        root.setLevel(0);
        root.setIdentifier(path[0]);
        root.setPayload(payload);
        //System.out.println("root: " + root.csvString());
        boolean more;
        do{
            tokenizer.setDelimiter(AND);
            more = this.split(root, AND.charAt(0));

            tokenizer.setDelimiter(OR);
            more |= this.split(root, OR.charAt(0));

            more |= parseObject.preOrder(root, taskNegate);
            more |= parseObject.preOrder(root, taskUnwrap);
        }
        while(more);

        return (lastAdded = root);
    }

    @Override
    public IGTreeNode<T> add(T payload, List<String> path) {
        return null;
    }

    private boolean split(IGTreeNode<T> currNode, char delim) {
        if(currNode.isLeaf()){
            String identifier = currNode.identifier();
            String[] tokens = tokenizer.setText(identifier).parse().toArray();
            if(tokens.length > 1){
                currNode.setIdentifier("");
                currNode.setOp(delim);
                for (String token : tokens) {
                    //System.out.println("token: " + token);
                    currNode.addChild(token, null);
                }
                return true;
            }
        }
        else{
            boolean more = false;
            for (IGTreeNode<T>  child : currNode.getChildren()) {
                more |= this.split(child, delim);
            }
            return more;
        }
        return false;
    }

    @Override
    public String toString(){
        return unParse(root);
    }

    private String unParse(IGTreeNode<T> currNode){
        String format = currNode.wrapped()? "%s(%s)" : "%s%s";
        String negateSymbol = currNode.negated()? String.valueOf(NEGATE_SYMBOL) : "";

        if(currNode.isLeaf()){
            return String.format(format, negateSymbol, currNode.identifier());
        }
        else{
            String op = String.format(" %c%c ", currNode.op(), currNode.op());
            ArrayList<String> childrenToList = new ArrayList<>();
            for(IGTreeNode<T> child : currNode.getChildren()){
                childrenToList.add(unParse(child));
            }

            return String.format(format, negateSymbol, String.join(op, childrenToList));
        }
    }
}
