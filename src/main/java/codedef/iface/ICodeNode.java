package codedef.iface;

import codedef.modifier.MODIFIER;
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

    ICodeNode setChildren(ICodeNode... childNodes);
    List<ICodeNode> getChildren();

    ICodeNode putAttrib(MODIFIER modifier, String... values);
    String[] getAttrib(MODIFIER modifier);

    void setParentNode(ICodeNode parentNode);
    ICodeNode getParentNode();

    void display(int level);

    ITranslator translator();
    ICodeNode prototype();
}
