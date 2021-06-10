package codedef.codenode;

import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;
import codedef.impl.AttribModifier;
import codedef.impl.AttribStruct;
import codedef.modifier.*;
import err.ERR_TYPE;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;
import translators.iface.ITranslator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static codedef.modifier.MODIFIER.IS_HEADER;
import static codedef.modifier.MODIFIER.NAME;

public class CodeNode implements ICodeNode {
    protected final CODE_NODE codeNodeEnum, enumGroup;
    protected final IAttribModifier attribModifier;
    protected final IAttribStruct structHeader;
    protected final IAttribStruct structBody;
    protected final CodeNodeContentToJson contentToJson;
    protected ICodeNode parentNode;
    protected final ITranslator translator;
    protected final List<ICodeNode> children;

    public CodeNode(CODE_NODE codeNodeEnum, CODE_NODE enumGroup, IAttribModifier attribModifier, IAttribStruct structHeader, IAttribStruct structBody) {
        this.codeNodeEnum = codeNodeEnum;
        this.enumGroup = (enumGroup == null)? codeNodeEnum : enumGroup;
        this.attribModifier = (attribModifier == null)? new AttribModifier(codeNodeEnum) : attribModifier;
        this.structHeader = (structHeader == null)? new AttribStruct(codeNodeEnum) : structHeader;
        this.structBody = (structBody == null)? new AttribStruct(codeNodeEnum) : structBody;
        this.translator = Glob.OUT_LANG_MANAGER.getTranslatorFactory().get(codeNodeEnum);
        this.contentToJson = new CodeNodeContentToJson();
        this.children = new ArrayList<>();
    }

    @Override
    public CODE_NODE codeNodeEnum() {
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
    public ICodeNode setChildren(ICodeNode... childNodes) {
        List<ICodeNode> childNodesList = Arrays.asList(childNodes);
        for(ICodeNode child : childNodesList){
            child.setParentNode(this);
            {
                MODIFIER missingModifier;
                if((missingModifier = child.getAttribModifier().reportMissingModifier()) != null){
                    String desc = String.format("%s in %s", missingModifier, codeNodeEnum);
                    Glob.ERR.kill(ERR_TYPE.MISSING_REQUIRED, desc);
                }
            }

            // Label payload header or body, depending on which attribStruct allows it
            // If not explicitly assigned to header, any small scope is allowed in body
            CODE_NODE enumActual = child.codeNodeEnum();
            CODE_NODE enumAlias = child.enumGroup();
            if(structHeader.isAllowedChild(enumActual) || structHeader.isAllowedChild(enumAlias)){
                child.getAttribModifier().put(IS_HEADER, ENU_BOOLEAN.TRUE.toString());
            }
            else if(structBody.isAllowedChild(enumActual) || structBody.isAllowedChild(enumAlias)){
                child.getAttribModifier().put(IS_HEADER, ENU_BOOLEAN.FALSE.toString());
            }
            else{
                Glob.ERR.kill(ERR_TYPE.DISALLOWED_NESTING, String.format("%s in %s", enumActual, this.codeNodeEnum));
            }
        }
        children.addAll(childNodesList);

        CODE_NODE missingChild;
        if(
            (missingChild = structHeader.reportMissingChild(children)) != null ||
            (missingChild = structBody.reportMissingChild(children)) != null
        ) {
            Glob.ERR.kill(ERR_TYPE.MISSING_REQUIRED, String.format("%s in %s", missingChild, this.codeNodeEnum));
        }

        return this;
    }

    @Override
    public List<ICodeNode> getChildren() {
        return children;
    }

    @Override
    public ICodeNode putAttrib(MODIFIER modifier, String... values) {
        attribModifier.put(modifier, values);
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
                attribModifier.get(NAME)[0]
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
    public void fromJson(JSONObject jsonObject) {}

    @Override
    public JSONObject toJson() {
        return contentToJson.getJsonObj(this);
    }

    @Override
    public ICodeNode prototype() {
        return new CodeNode(codeNodeEnum, enumGroup, attribModifier.prototype(), structHeader.prototype(), structBody.prototype());
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
            out.put("attributes", codeNode.getAttribModifier().toJson());
            //out.put("structBody", codeNode.getAttribStruct().toJson());
            out.put("children", this.getChildren(codeNode));
            return out;
        }
        private JSONArray getChildren(ICodeNode codeNode){
            JSONArray jsonChildren = new JSONArray();
            for(ICodeNode childNode : codeNode.getChildren()){
                jsonChildren.put(childNode.toJson());
            }
            return jsonChildren;
        }
    }
}
