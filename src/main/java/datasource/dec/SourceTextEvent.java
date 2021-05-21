package datasource.dec;

import datasource.iface.IDataSource;
import err.ERR_TYPE;
import langdef.CMD;
import langdef.STRUCT_SYMBOL;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;
import textevent.impl.TextEvent;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

import static langdef.CMD.*;
import static langdef.STRUCT_SYMBOL.*;

/** Matches text with enums defined in STRUCT_SYMBOL.
 *  If exact match, node.textEvent is updated with the enum so later parsing can respond to it.
 *
 *  Current language definition:
 *    If a name is to be assigned to a text pattern, the name immediately follows the structure keyword.
 *    Naming done by the idDefine text pattern.
 *    If name in idDefine event is copied to the structure keyword event, the idDefine event is
 *    marked inactive and removed by SourceActiveOnly.
 *  */
public class SourceTextEvent extends DecoratorBase{
    private final TextEventUtil textEventUtil;
    private IReadNode currNode;

    public SourceTextEvent(IDataSource dataSource) {
        super(dataSource);
        textEventUtil = new TextEventUtil();
        next();
    }

    @Override
    public boolean hasNext() {
        return currNode != null;
    }

    @Override
    public IReadNode next() {
        IReadNode prevNode = currNode;
        textEventUtil.tryAddTextEvent(dataSource.next());
        currNode = textEventUtil.dequeue();
        return prevNode;
    }

    public static class TextEventUtil {
        private final Queue<IReadNode> queue;
        private final Stack<STRUCT_SYMBOL> stack;
        private boolean recentAvSwap;

        public TextEventUtil() {
            queue = new ArrayDeque<>();
            stack = new Stack<>();
            recentAvSwap = false;
        }

        public void tryAddTextEvent(IReadNode currNode){
            String text;
            if(currNode != null){
                STRUCT_SYMBOL structSymbol;
                char symbol;
                if(
                        (text = currNode.text()).length() != 1 ||
                        (structSymbol = STRUCT_SYMBOL.fromSymbol(symbol = text.charAt(0))) == null
                ){
                    currNode.setTextEvent(new TextEvent(DATA_VAL, ADD_DATA, currNode.text()));
                    queue.add(currNode);
                }
                else{
                    CMD cmdSummary = structSymbol.command();
                    CMD subCommand = structSymbol.subCommand(symbol);

                    switch(cmdSummary){
                        case ST_SWAP:
                            this.handleStSwap(currNode, structSymbol, subCommand);
                            break;
                        case KV_SWAP:
                            this.handleKvSwap(currNode);
                            break;
                        case AV_SWAP:
                            this.handleAvSwap(currNode);
                            break;
                    }
                }
            }
        }
        private STRUCT_SYMBOL linkedSymbol(STRUCT_SYMBOL mainObject, CMD subCommand){
            switch(mainObject){
                case JSON_OBJ:
                    return (subCommand == PUSH)? OBJ_KEY : OBJ_VAL;
                case JSON_ARR:
                    return ARR_VAL;
                default:
                    return null;
            }
        }

        private boolean isSequenced(STRUCT_SYMBOL sequencedSymbol){
            return OBJ_KEY == sequencedSymbol;
        }

        private boolean isLinked(STRUCT_SYMBOL linkedSymbol){
            switch(linkedSymbol){
                case OBJ_KEY:
                case OBJ_VAL:
                case ARR_VAL:
                    return true;
                default:
                    return false;
            }
        }

        private void handleStSwap(IReadNode currNode, STRUCT_SYMBOL structSymbol, CMD subCommand){
            recentAvSwap = false;
            switch(subCommand){
                case PUSH:
                    this.handlePush(currNode, structSymbol);
                    break;
                case POP:
                    this.handlePop(currNode, structSymbol);
                    break;
            }
        }
        private void handlePush(IReadNode currNode, STRUCT_SYMBOL structSymbol){
            STRUCT_SYMBOL linkedSymbol = linkedSymbol(structSymbol, PUSH);
            stack.push(structSymbol);
            stack.push(linkedSymbol);

            IReadNode insertNode = ReadNode.builder().copy(currNode).
                    textEvent(new TextEvent(linkedSymbol, PUSH, currNode.text())).
                    build();
            currNode.setTextEvent(new TextEvent(structSymbol, PUSH, currNode.text()));

            queue.add(currNode);
            queue.add(insertNode);
        }
        private void handlePop(IReadNode currNode, STRUCT_SYMBOL structSymbol){
            STRUCT_SYMBOL linkedSymbol = linkedSymbol(structSymbol, POP);
            stack.pop();
            stack.pop();

            IReadNode insertNode = ReadNode.builder().copy(currNode).
                    textEvent(new TextEvent(linkedSymbol, POP, currNode.text())).
                    build();
            currNode.setTextEvent(new TextEvent(structSymbol, POP, currNode.text()));


            queue.add(insertNode);
            queue.add(currNode);
        }

        private void handleKvSwap(IReadNode currNode){
            STRUCT_SYMBOL structSymbol = null;
            if(stack.isEmpty() || !isSequenced(structSymbol = stack.peek())){
                Glob.ERR.kill(ERR_TYPE.BAD_JSON, currNode);
            }

            stack.pop();
            stack.push(OBJ_VAL);

            IReadNode insertNode = ReadNode.builder().copy(currNode).
                    textEvent(new TextEvent(structSymbol, POP, currNode.text())).
                    build();
            currNode.setTextEvent(new TextEvent(OBJ_VAL, PUSH, currNode.text()));

            queue.add(insertNode);
            queue.add(currNode);
        }

        private void handleAvSwap(IReadNode currNode){
            STRUCT_SYMBOL stackPeek = null;
            if(recentAvSwap || stack.isEmpty() || !isLinked(stackPeek = stack.peek())){
                Glob.ERR.kill(ERR_TYPE.MISPLACED_CHAR, currNode);
            }

            IReadNode insertNode = ReadNode.builder().copy(currNode).
                    textEvent(new TextEvent(stackPeek, POP, currNode.text())).
                    build();

            if(stackPeek == OBJ_VAL){
                stack.pop();
                stack.push(OBJ_KEY);
                stackPeek = OBJ_KEY;
            }

            currNode.setTextEvent(new TextEvent(stackPeek, PUSH, currNode.text()));

            queue.add(insertNode);
            queue.add(currNode);

            recentAvSwap = true;
        }

        public IReadNode dequeue(){
            return (queue.isEmpty())? null : queue.remove();
        }
    }
}
