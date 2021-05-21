package codedef.iface;

import generictree.iface.IGTreeNode;
import iface_global.ICsv;
import codedef.modifier.CODE_NODE;

import java.util.List;

public interface ICodeNode extends ICsv, ISerializableJson {
    CODE_NODE codeNodeEnum();
    IAttribModifier getAttribModifier();
    IAttribStruct getAttribStruct();

    void visitParent(ICodeNode child);
    void setParentTreeNode(IGTreeNode<ICodeNode> parentTreeNode);
    IGTreeNode<ICodeNode> getParentTreeNode();
    void set(ICodeNode newNode);


//    void fromJson(JSONObject jsonObject);
//    JSONObject toJson();

    ICodeNode prototype();
}
