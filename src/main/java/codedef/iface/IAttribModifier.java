package codedef.iface;

import iface_global.ICsv;
import codedef.modifier.MODIFIER;
import iface_global.ISerializableJson;

import java.util.HashMap;
import java.util.List;

public interface IAttribModifier extends ICsv, ISerializableJson {
    void initRequired(MODIFIER... requiredModifiers);
    void initAllowed(MODIFIER... allowedModifiers);
    List<MODIFIER> getRequired();
    List<MODIFIER> getAllowed();
    HashMap<MODIFIER, String[]> getAttributes();

    boolean isRequired(MODIFIER modifier);
    MODIFIER reportMissingModifier();
    void assertIsAllowed(MODIFIER modifier);

    void put(MODIFIER modifier, String... values);
    void clear(MODIFIER modifier);
    String[] get(MODIFIER modifier);

    IAttribModifier prototype();

    // ICsv
    //String csvString();
    //String friendlyString();
}
