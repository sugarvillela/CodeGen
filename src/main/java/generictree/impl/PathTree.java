package generictree.impl;

import generictree.iface.IGTreeNode;
import generictree.node.ParseTreeNode;

import java.util.List;

/** For cases where the path to an element is known.
 *  Path is a 'splitChar' separated string that corresponds to
 *  the node identifiers on the path to the element (see tests)
 * @param <T> the IGTreeNode payload type
 */
public class PathTree <T> extends GTreeBase <T> {
    private final char splitChar;

    public PathTree(char splitChar) {
        this.splitChar = splitChar;
    }

    @Override
    public IGTreeNode<T> add(T payload, String... path) {
        //System.out.println(">>>>>>>" + path);
        if(path.length == 1 && path[0].indexOf(splitChar) != -1){
            path = tokenizePathOnSingle(splitChar, path);
        }
        if(root == null){
            root = new ParseTreeNode<>();
            root.setLevel(0);
            root.setIdentifier(path[0]);
            root.setPayload(payload);
            return (lastAdded = root);
        }
        else{
            return (lastAdded = parseObject.addByPath(payload, root, path));
        }
    }

    @Override
    public IGTreeNode<T> add(T payload, List<String> path) {
        if(path.size() == 1 && path.get(0).indexOf(splitChar) != -1){
            path = tokenizePathOnSingle(splitChar, path);
        }
        if(root == null){
            root = new ParseTreeNode<>();
            root.setLevel(0);
            root.setIdentifier(path.get(0));
            root.setPayload(payload);
            return (lastAdded = root);
        }
        else{
            return (lastAdded = parseObject.addByPath(payload, root, path));
        }
    }
}
