package langdefalgo.impl;

import err.ERR_TYPE;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.LANG_STRUCT;
import runstate.Glob;
import stackpayload.iface.IStackPayload;
import stackpayload.impl.StackPayload;

public abstract class AlgoBase implements LANG_STRUCT, EnumPOJOJoin {
    protected LANG_STRUCT parentEnum;

    protected AlgoBase() {}

    boolean assertNotNull(Object object){
        if(object == null){
            Glob.ERR.kill(ERR_TYPE.SYNTAX);
            return false;
        }
        return true;
    }

    @Override
    public void onPush(IStackPayload stackTop) {}

    @Override
    public void onPop(IStackPayload stackTop) {}

    @Override
    public void onNest(IStackPayload newTop) {}

    @Override
    public IStackPayload newStackPayload() {
        return new StackPayload(this.parentEnum);
    }

    /*=====EnumAlgoJoin===============================================================================================*/

    @Override
    public void initAlgo(AlgoBase childAlgo) {
        Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR);
    }

    @Override
    public void initAlgo(LANG_STRUCT parentEnum, AlgoBase childAlgo) {
        this.parentEnum = parentEnum;
    }

    @Override
    public LANG_STRUCT getParentEnum() {
        return parentEnum;
    }

    @Override
    public LANG_STRUCT getChildAlgo() {
        Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR);
        return null;
    }
}
