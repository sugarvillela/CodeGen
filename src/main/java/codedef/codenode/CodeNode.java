package codedef.codenode;

import codedef.enums.ENU_BOOLEAN;
import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;
import codedef.impl.AttribModifier;
import codedef.impl.AttribStruct;
import codedef.enums.*;
import err.ERR_TYPE;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;
import translators.iface.ITranslator;

import java.util.ArrayList;
import java.util.List;

import static codedef.enums.MODIFIER.*;

public class CodeNode implements ICodeNode {
    private final CODE_NODE codeNodeEnum, enumGroup;
    private final IAttribModifier attribModifier;
    private final IAttribStruct structHeader;
    private final IAttribStruct structBody;
    private final CodeNodeContentToJson contentToJson;
    private ICodeNode parentNode;
    private final ITranslator translator;
    private final List<ICodeNode> children;
    private final NodeWrapUtil nodeWrapUtil;

    private String pathInfo;

    public CodeNode(CODE_NODE codeNodeEnum, CODE_NODE enumGroup, IAttribModifier attribModifier, IAttribStruct structHeader, IAttribStruct structBody) {
        this.codeNodeEnum = codeNodeEnum;
        this.enumGroup = (enumGroup == null)? codeNodeEnum : enumGroup;
        this.attribModifier = (attribModifier == null)? new AttribModifier(codeNodeEnum) : attribModifier;
        this.structHeader = (structHeader == null)? new AttribStruct(codeNodeEnum) : structHeader;
        this.structBody = (structBody == null)? new AttribStruct(codeNodeEnum) : structBody;
        this.translator = Glob.TRANSLATION_CENTER.getTranslatorFactory().get(codeNodeEnum, this);
        this.contentToJson = new CodeNodeContentToJson();
        this.children = new ArrayList<>();
        this.nodeWrapUtil = Glob.CONVERT_UTIL;
    }

    @Override
    public CODE_NODE getCodeNodeEnum() {
        return this.codeNodeEnum;
    }

    @Override
    public CODE_NODE enumGroup() {
        return this.enumGroup;
    }

    @Override
    public IAttribModifier getAttribModifier() {
        return attribModifier;
    }

    @Override
    public IAttribStruct getStructHeader() {
        return structHeader;
    }

    @Override
    public IAttribStruct getStructBody() {
        return structBody;
    }

    @Override
    public ICodeNode setChildren(Object... objects) {
        List<ICodeNode> childNodes = Glob.CONVERT_UTIL.convert(objects);

        for(ICodeNode child : childNodes){
            // Link to parent in tree
            child.setParentNode(this);

            // Make sure child has all required attributes
            child.getAttribModifier().assertHaveAllRequired();

            // Make sure child has only allowed children
            this.validateAllowed(child);
        }
        children.addAll(childNodes);

        CODE_NODE missingChild;
        if(
            (missingChild = structHeader.reportMissingChild(children)) != null ||
            (missingChild = structBody.reportMissingChild(children)) != null
        ) {
            Glob.ERR.kill(ERR_TYPE.MISSING_REQUIRED, String.format("%s in %s", missingChild, this.codeNodeEnum));
        }

        return this;
    }

    private void validateAllowed(ICodeNode child){
        // Label payload header or body, depending on which attribStruct allows it
        // If not explicitly assigned to header, any small scope is allowed in body
        CODE_NODE childEnumActual = child.getCodeNodeEnum();
        CODE_NODE childEnumAlias = child.enumGroup();
        if(structHeader.isAllowedChild(childEnumActual) || structHeader.isAllowedChild(childEnumAlias)){
            child.getAttribModifier().put(IS_HEADER_, ENU_BOOLEAN.TRUE.toString());
        }
        else if(structBody.isAllowedChild(childEnumActual) || structBody.isAllowedChild(childEnumAlias)){
            child.getAttribModifier().put(IS_HEADER_, ENU_BOOLEAN.FALSE.toString());
        }
        else{
            Glob.ERR.kill(ERR_TYPE.DISALLOWED_NESTING, String.format("%s in %s", childEnumActual, this.codeNodeEnum));
        }
    }

    @Override
    public List<ICodeNode> getChildren() {
        return children;
    }

    @Override
    public ICodeNode putAttrib(MODIFIER modifier, Object... objects) {
//        System.out.printf("%s: put attribute: %s: %s \n",
//                codeNodeEnum, modifier, Arrays.toString(values));
        attribModifier.put(modifier, objects);
        return this;
    }

    @Override
    public String[] getAttrib(MODIFIER modifier) {
        return attribModifier.get(modifier);
    }

    @Override
    public void setParentNode(ICodeNode parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    public ICodeNode getParentNode() {
        return parentNode;
    }

    @Override
    public void display(int level) {
        System.out.printf("%s%s: %s \n",
                new String(new char[level * 4]).replace('\0', ' '),
                codeNodeEnum,
                attribModifier.get(NAME_)[0]
        );
        for(ICodeNode childNode : this.getChildren()){
            childNode.display(level + 1);
        }
    }

    @Override
    public ITranslator translator() {
        return this.translator;
    }

    @Override
    public void importJson(JSONObject jsonObject) {}

    @Override
    public JSONObject exportJson() {
        return contentToJson.getJsonObj(this);
    }

    @Override
    public ICodeNode prototype() {
        return new CodeNode(codeNodeEnum, enumGroup, attribModifier.prototype(), structHeader.prototype(), structBody.prototype());
    }

    @Override
    public ICodeNode setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
        return this;
    }

    @Override
    public String getPathInfo() {
        return this.pathInfo;
    }

    @Override
    public boolean havePathInfo() {
        return this.pathInfo != null;
    }


    @Override
    public String toString() {
        return this.csvString();
    }

    @Override
    public String friendlyString() {
        return "CodeStruct{" +
                "\n    codeNodeEnum=" + codeNodeEnum +
                "\n    attribModifier=" + attribModifier.csvString() +
                "\n    structBody=" + structBody.csvString() +
                "\n}";
    }

    @Override
    public String csvString() {
        return codeNodeEnum +
                "," + attribModifier.csvString();// +
                //"," + structBody.csvString();
    }

    private static class CodeNodeContentToJson {
        public JSONObject getJsonObj(ICodeNode codeNode) {
            JSONObject out = new JSONObject();
            out.put("attributes", codeNode.getAttribModifier().exportJson());
            JSONArray jsonChildren = new JSONArray();
            for(ICodeNode childNode : codeNode.getChildren()){
                jsonChildren.put(childNode.exportJson());
            }
            out.put("children", jsonChildren);
            return out;
        }
    }
}
