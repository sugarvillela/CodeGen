package codedef.impl;

import codedef.iface.IAttribModifier;
import codedef.modifier.CODE_NODE;
import codedef.modifier.MODIFIER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static codedef.modifier.MODIFIER.NAME;

public class AttribModifier_Nameless extends AttribModifier {
    public AttribModifier_Nameless(CODE_NODE codeNodeEnum) {
        super(codeNodeEnum);
        this.put(NAME, codeNodeEnum.toString());// the only difference between this and parent
    }

    public AttribModifier_Nameless(CODE_NODE codeNodeEnum, List<MODIFIER> required, List<MODIFIER> allowed, HashMap<MODIFIER, String[]> attributes) {
        super(codeNodeEnum, required, allowed, attributes);
    }

    @Override
    public IAttribModifier prototype() {
        return new AttribModifier_Nameless(
                codeNodeEnum,
                new ArrayList<>(required),
                new ArrayList<>(allowed),
                new HashMap<>(attributes)
        );
    }
}
