package codedef.impl;

import codedef.iface.ICodeNode;
import codedef.init.CodeDef;
import codedef.enums.CODE_NODE;

import java.util.HashMap;

/**A singleton class with instance maintained at Glob.PROTOTYPE_FACTORY
 * On start: calls CodeDef to populate prototypes map */
public class PrototypeFactory {
    public static PrototypeFactory initInstance(){
        return new PrototypeFactory();
    }

    private final HashMap<CODE_NODE, ICodeNode> prototypes;

    private PrototypeFactory(){
        prototypes = new CodeDef().getPrototypes();
    }

    public ICodeNode get(CODE_NODE codeNodeEnum){
        return prototypes.get(codeNodeEnum).prototype();
    }
}
