package codedef.iface;

import iface_global.ICsv;
import codedef.modifier.CODE_NODE;
import iface_global.ISerializableJson;

import java.util.List;

public interface IAttribStruct  extends ICsv, ISerializableJson {
    void initRequired(CODE_NODE... requiredChildren);
    void initAllowed(CODE_NODE... allowedChildren);

    List<CODE_NODE> getRequired();
    List<CODE_NODE> getAllowed();

    CODE_NODE reportMissingChild(List<ICodeNode> children);
    boolean isAllowedChild(CODE_NODE childEnum);

    IAttribStruct prototype();

    // ICsv
    //String csvString();
    //String friendlyString();
}
