package codedef.iface;

import generictree.iface.IGTreeNode;
import iface_global.ICsv;
import codedef.modifier.CODE_NODE;
import iface_global.ISerializableJson;
import translators.iface.ITranslator;

import java.util.List;

public interface ICodeNode extends ICsv, ISerializableJson {
    CODE_NODE codeNodeEnum();
    CODE_NODE enumGroup();
    IAttribModifier getAttribModifier();
    IAttribStruct getStructHeader();
    IAttribStruct getStructBody();

    void setParentTreeNode(IGTreeNode<ICodeNode> parentTreeNode);
    IGTreeNode<ICodeNode> getParentTreeNode();

    // recursive tree helpers
    void finalizeTree(IGTreeNode<ICodeNode> parentTreeNode);
    void display(IGTreeNode<ICodeNode> parentTreeNode);

    ITranslator translator();
    ICodeNode prototype();
}
