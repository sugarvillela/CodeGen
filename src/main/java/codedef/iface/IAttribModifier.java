package codedef.iface;

import iface_global.ICsv;
import codedef.enums.MODIFIER;
import iface_global.ISerializableJson;

import java.util.HashMap;
import java.util.List;

public interface IAttribModifier extends ICsv, ISerializableJson {
    void initRequired(MODIFIER... requiredModifiers);
    void initAllowed(MODIFIER... allowedModifiers);
    List<MODIFIER> getRequired();
    List<MODIFIER> getAllowed();
    HashMap<MODIFIER, String[]> getAttributes();

    // Validation for attributes
    void assertIsAllowedModifier(MODIFIER modifier);
    void assertHaveAllRequired();

    void put(MODIFIER modifier, Object... objects);
    void clear(MODIFIER modifier);
    String[] get(MODIFIER modifier);

    IAttribModifier prototype();

    // ICsv
    //String csvString();
    //String friendlyString();
}
