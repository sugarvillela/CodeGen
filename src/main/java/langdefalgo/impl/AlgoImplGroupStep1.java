package langdefalgo.impl;

import err.ERR_TYPE;
import readnode.iface.IReadNode;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

import static langdef.STRUCT_SYMBOL.*;

public class AlgoImplGroupStep1 {
    private static final int FIRST = 0, SECOND = 1;

    public void initAlgos(){
        // Struct keyword  // no sharing singletons: each algo needs a unique state
        JSON_OBJ.initAlgo(
                new Obj()
        );
        JSON_ARR.initAlgo(
                new Arr()
        );
        OBJ_KEY.initAlgo(
                new PassBelow()
        );
        OBJ_VAL.initAlgo(
                new ObjVal()
        );
        ARR_VAL.initAlgo(
                new PassBelow()
        );
        DATA_VAL.initAlgo(
                new PassBelow()
        );
    }

    private static class Nop extends AlgoBase {
        @Override
        public boolean go(IStackPayload stackTop) {
            return false;
        }
    }
    private static class PassBelow extends AlgoBase {
        @Override
        public boolean go(IStackPayload stackTop) {
            IStackPayload below;
            if(assertNotNull(stackTop) && assertNotNull((below = stackTop.getBelow()))){
                below.getParentEnum().go(below);
            }
            return false;
        }
    }
    private static class ObjVal extends PassBelow {
        @Override
        public void onPop(IStackPayload stackTop) {
            IStackPayload below;
            if(assertNotNull(stackTop) && assertNotNull((below = stackTop.getBelow()))){
                below.getState().set(FIRST);
            }
        }
    }
    private static class Obj extends AlgoBase {
        @Override
        public boolean go(IStackPayload stackTop) {
            if(assertNotNull(stackTop)){
                IReadNode readNode = Glob.RUN_STATE.getCurrNode();

                int state = stackTop.getState().getInt();
                stackTop.getState().set(state + 1);

                switch(state){
                    case FIRST:
                        System.out.println("Obj FIRST: " + readNode.text());
                        break;
                    case SECOND:
                        System.out.println("Obj SECOND: " + readNode.text());
                        break;
                    default:
                        Glob.ERR.kill(ERR_TYPE.SYNTAX);
                }
            }
            return false;
        }
    }
    private static class Arr extends AlgoBase {
        @Override
        public boolean go(IStackPayload stackTop) {
            if(assertNotNull(stackTop)){
                IReadNode readNode = Glob.RUN_STATE.getCurrNode();

                int state = stackTop.getState().getInt();
                stackTop.getState().set(state + 1);

                System.out.println("Arr " + state + ": " + readNode.text());
            }
            return false;
        }
    }
}
