package pushpoputil.impl;

import err.ERR_TYPE;
import langdef.CMD;
import langdef.STRUCT_SYMBOL;
import pushpoputil.iface.IPushPopUtil;
import readnode.iface.IReadNode;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

public class PushPopUtil implements IPushPopUtil {
    private static PushPopUtil instance;

    public static PushPopUtil initInstance(){
        return (instance == null)? (instance = new PushPopUtil()): instance;
    }

    private PushPopUtil(){}

    @Override
    public boolean handleTextEvent(IStackPayload stackTop) {
        IReadNode readNode = Glob.RUN_STATE.getCurrNode();
        if(readNode.hasTextEvent()){
            return (readNode.textEvent().cmd() == CMD.POP && tryPop()) ||
                    (readNode.textEvent().cmd() == CMD.PUSH && tryPush(readNode, stackTop));
        }
        return false;
    }
    private boolean tryPop() {
        if(Glob.RUN_STATE.getStack().size() == 0){
            Glob.ERR.kill(ERR_TYPE.SYNTAX);
        }
        Glob.RUN_STATE.getStack().pop();
        return true;
    }

    private boolean tryPush(IReadNode readNode, IStackPayload stackTop) {
        if(stackTop != null && stackTop.getParentEnum() == STRUCT_SYMBOL.DATA_VAL){
            Glob.ERR.kill(ERR_TYPE.SYNTAX);
        }
        IStackPayload newTop = readNode.textEvent().langStruct().newStackPayload();
        Glob.RUN_STATE.getStack().push(newTop);
        return true;
    }
}
