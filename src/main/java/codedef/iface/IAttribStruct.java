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

    boolean isRequired(CODE_NODE childEnum);
    void assertHaveRequiredChildren(List<ICodeNode> children);
    void assertIsAllowedChild(CODE_NODE newChildEnum);

    IAttribStruct prototype();

    // ICsv
    //String csvString();
    //String friendlyString();
}
