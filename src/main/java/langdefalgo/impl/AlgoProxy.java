package langdefalgo.impl;

import err.ERR_TYPE;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.LANG_STRUCT;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

/** Adds algorithm mutability to LANG_STRUCT enum */
public class AlgoProxy implements LANG_STRUCT, EnumPOJOJoin {
    AlgoBase childAlgo;

    @Override
    public boolean go(IStackPayload stackTop) {
        return childAlgo.go(stackTop);
    }

    @Override
    public void onPush(IStackPayload stackTop) {
        childAlgo.onPush(stackTop);
    }

    @Override
    public void onPop(IStackPayload stackTop) {
        childAlgo.onPop(stackTop);
    }

    @Override
    public void onNest(IStackPayload newTop) {
        childAlgo.onNest(newTop);
    }

    @Override
    public IStackPayload newStackPayload() {
        return childAlgo.newStackPayload();
    }

    /*=====EnumPOJOJoin===============================================================================================*/

    @Override
    public void initAlgo(AlgoBase childAlgo) {
        Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR);
    }

    @Override
    public void initAlgo(LANG_STRUCT parentEnum, AlgoBase childAlgo) {
        childAlgo.initAlgo(parentEnum, null);
        this.childAlgo = childAlgo;
    }

    @Override
    public LANG_STRUCT getParentEnum() {
        return childAlgo.getParentEnum();
    }

    @Override
    public LANG_STRUCT getChildAlgo() {
        return childAlgo.getChildAlgo();
    }
}
