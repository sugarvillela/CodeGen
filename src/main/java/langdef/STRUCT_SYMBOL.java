package langdef;

import err.ERR_TYPE;
import langdefalgo.iface.EnumPOJOJoin;
import langdefalgo.iface.LANG_STRUCT;
import langdefalgo.impl.AlgoBase;
import langdefalgo.impl.AlgoProxy;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

import static langdef.CMD.*;

public enum STRUCT_SYMBOL implements LANG_STRUCT, EnumPOJOJoin {
    JSON_OBJ    (ST_SWAP,'{', '}'),
    JSON_ARR    (ST_SWAP,'[', ']'),

    JSON_SWAP   (AV_SWAP, ',', '\0'),
    OBJ_KEY,
    OBJ_VAL     (KV_SWAP, ':', '\0'),
    ARR_VAL,
    DATA_VAL
    ;

    private final CMD cmd;
    private final char pushSymbol, popSymbol;
    private final AlgoProxy algoProxy;

    STRUCT_SYMBOL(){
        this(null,'\0','\0');
    }
    STRUCT_SYMBOL(CMD cmd, char pushSymbol, char popSymbol) {
        this.cmd = cmd;
        this.pushSymbol = pushSymbol;
        this.popSymbol = popSymbol;
        this.algoProxy = new AlgoProxy();
    }

    public static STRUCT_SYMBOL fromSymbol(char symbol){
        for(STRUCT_SYMBOL structSymbol : values()){
            if(symbol == structSymbol.pushSymbol || symbol == structSymbol.popSymbol){
                return structSymbol;
            }
        }
        return null;
    }

    public CMD command() {
        return cmd;
    }

    public CMD subCommand(char symbol) {
        if(symbol == this.pushSymbol){
            return PUSH;
        }
        if(symbol == this.popSymbol){
            return POP;
        }
        return null;
    }

    /*=====LANG_STRUCT================================================================================================*/

    @Override
    public boolean go(IStackPayload stackTop) {
        return algoProxy.go(stackTop);
    }

    @Override
    public void onPush(IStackPayload stackTop) {
        //System.out.println("STRUCT_SYMBOL push: ENUM = " + this);
        algoProxy.onPush(stackTop);
    }

    @Override
    public void onPop(IStackPayload stackTop) {
        algoProxy.onPop(stackTop);
    }

    @Override
    public void onNest(IStackPayload newTop) {
        algoProxy.onNest(newTop);
    }

    @Override
    public IStackPayload newStackPayload() {
        return algoProxy.newStackPayload();
    }

    /*=====EnumAlgoJoin===============================================================================================*/

    @Override
    public void initAlgo(AlgoBase childAlgo) {
        this.algoProxy.initAlgo(this, childAlgo);
    }

    @Override
    public void initAlgo(LANG_STRUCT parentEnum, AlgoBase childAlgo) {
        Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR);
    }

    @Override
    public LANG_STRUCT getParentEnum() {
        Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR);
        return null;
    }

    @Override
    public LANG_STRUCT getChildAlgo() {
        return algoProxy;
    }
}
