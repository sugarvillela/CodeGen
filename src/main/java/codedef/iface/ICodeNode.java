package codedef.iface;

import codedef.enums.MODIFIER;
import iface_global.ICsv;
import codedef.enums.CODE_NODE;
import iface_global.ISerializableJson;
import translators.iface.ITranslator;

import java.util.List;

public interface ICodeNode extends ICsv, ISerializableJson {
    /** @return The type of lang structure this object represents */
    CODE_NODE getCodeNodeEnum();

    /** For small-scope structures, an alias can be used: EXPR_GRP.
     * If no enumGroup is passed to constructor, enumGroup = codeNodeEnum
     * @return The type or group this object represents */
    CODE_NODE enumGroup();

    /** AttribModifier manages JSON attributes
     * @return the AttribModifier object set on construct */
    IAttribModifier getAttribModifier();

    /** AttribStruct manages required/allowed children in ICodeNode tree structure
     * @return the AttribStruct for the lang structure header (first part of code generation) */
    IAttribStruct getStructHeader();

    /** AttribStruct manages required/allowed children in ICodeNode tree structure
     * @return the AttribStruct for the lang structure body (code generation after header) */
    IAttribStruct getStructBody();

    /** Set immediate children of this node in ICodeNode tree structure.
     *  If prim values found, wrap in CodeNode for compatibility (see ConvertUtil)
     * @return this (can chain) */
    ICodeNode setChildren(Object... objects);

//    /** Create an ICodeNode wrapper for values and set as child
//     * @return this (can chain) */
//    ICodeNode setChildren(String... values);
//    ICodeNode setChildren(int... values);
//    ICodeNode setChildren(long... values);
//    ICodeNode setChildren(double... values);

    /** @return immediate children of this node in ICodeNode tree structure */
    List<ICodeNode> getChildren();

    /** Exposes attribModifier.put() method to add a key-value attribute
     * @return this (can chain) */
    ICodeNode putAttrib(MODIFIER modifier, Object... values);
    String[] getAttrib(MODIFIER modifier);

    /** When this is added to parent, set a reference to parent */
    void setParentNode(ICodeNode parentNode);
    ICodeNode getParentNode();

    /** Recursive depth-first display */
    void display(int level);

    /** @return The code generator for the lang structure this object represents */
    ITranslator translator();

    /** @return a deep copy of this (recursively calls prototype on parts) */
    ICodeNode prototype();

    /** Set code path or other info
     * @return this (can chain) */
    ICodeNode setPathInfo(String pathInfo);

    /** @return file path or other info */
    String getPathInfo();

    /** @return true if CodeNode is a file */
    boolean havePathInfo();

}
