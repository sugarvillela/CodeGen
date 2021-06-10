package codedef.impl;

import codedef.iface.ICodeNode;
import codedef.init.CodeDef;
import codedef.modifier.CODE_NODE;

import java.util.HashMap;

public class PrototypeFactory {
    private static PrototypeFactory instance;

    public static PrototypeFactory initInstance(){
        return(instance ==  null)? (instance = new PrototypeFactory()) : instance;
    }

    private final HashMap<CODE_NODE, ICodeNode> prototypes;

    private PrototypeFactory(){
        prototypes = new CodeDef().getPrototypes();
    }

    public ICodeNode get(CODE_NODE codeNodeEnum){
        return prototypes.get(codeNodeEnum).prototype();
    }
}
