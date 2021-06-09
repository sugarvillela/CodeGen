package codedef.codenode;

import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;
import codedef.impl.AttribModifier;
import codedef.impl.AttribStruct;
import codedef.modifier.*;
import err.ERR_TYPE;
import generictree.iface.IGTreeNode;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;
import translators.iface.ITranslator;

import static codedef.modifier.MODIFIER.IS_HEADER;
import static codedef.modifier.MODIFIER.NAME;

public class CodeNode implements ICodeNode {
    protected final CODE_NODE codeNodeEnum, enumGroup;
    protected final IAttribModifier attribModifier;
    protected final IAttribStruct structHeader;
    protected final IAttribStruct structBody;
    protected final CodeNodeContentToJson contentToJson;
    protected IGTreeNode<ICodeNode> parentTreeNode;
    protected final ITranslator translator;

    public CodeNode(CODE_NODE codeNodeEnum, CODE_NODE enumGroup, IAttribModifier attribModifier, IAttribStruct structHeader, IAttribStruct structBody) {
        this.codeNodeEnum = codeNodeEnum;
        this.enumGroup = (enumGroup == null)? codeNodeEnum : enumGroup;
        this.attribModifier = (attribModifier == null)? new AttribModifier(codeNodeEnum) : attribModifier;
        this.structHeader = (structHeader == null)? new AttribStruct(codeNodeEnum) : structHeader;
        this.structBody = (structBody == null)? new AttribStruct(codeNodeEnum) : structBody;
        this.translator = Glob.OUT_LANG_MANAGER.getTranslatorFactory().get(codeNodeEnum);
        this.contentToJson = new CodeNodeContentToJson();
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
    public void setParentTreeNode(IGTreeNode<ICodeNode> parentTreeNode) {
        this.parentTreeNode = parentTreeNode;
    }

    @Override
    public IGTreeNode<ICodeNode> getParentTreeNode() {
        return this.parentTreeNode;
    }

    @Override
    public void finalizeTree(IGTreeNode<ICodeNode> parentTreeNode) {
        this.parentTreeNode = parentTreeNode;
        for(IGTreeNode<ICodeNode> childTreeNode : parentTreeNode.getChildren()){
            ICodeNode childPayload = childTreeNode.getPayload();

            // link parent tree node with payload
            childPayload.setParentTreeNode(childTreeNode);

            // Label payload header or body, depending on which attribStruct allows it
            // If not explicitly assigned to header, any small scope is allowed in body
            CODE_NODE enumActual = childPayload.codeNodeEnum();
            CODE_NODE enumAlias = childPayload.enumGroup();
            if(structHeader.isAllowedChild(enumActual) || structHeader.isAllowedChild(enumAlias)){
                childPayload.getAttribModifier().put(IS_HEADER, ENU_BOOLEAN.TRUE.toString());
            }
            else if(structBody.isAllowedChild(enumActual) || structBody.isAllowedChild(enumAlias)){
                childPayload.getAttribModifier().put(IS_HEADER, ENU_BOOLEAN.FALSE.toString());
            }
            else{
                Glob.ERR.kill(
                        ERR_TYPE.DISALLOWED_NESTING,
                        enumActual.toString() + " in " + this.codeNodeEnum().toString());
            }
//            String[] isHeader = childPayload.getAttribModifier().get(IS_HEADER);
//            String isHeaderStr = (isHeader == null)? "FALSE" : isHeader[0];
//            System.out.printf("%s%s: %s: header = %s \n",
//                    new String(new char[childTreeNode.level() * 4]).replace('\0', ' '),
//                    childPayload.codeNodeEnum(),
//                    childPayload.getAttribModifier().get(NAME)[0],
//                    isHeaderStr
//            );
            childPayload.finalizeTree(childTreeNode);
        }
    }

    @Override
    public void display(IGTreeNode<ICodeNode> parentTreeNode) {
        System.out.printf("%s%s: %s \n",
                new String(new char[parentTreeNode.level() * 4]).replace('\0', ' '),
                codeNodeEnum,
                attribModifier.get(NAME)[0]
        );
        for(IGTreeNode<ICodeNode> childTreeNode : parentTreeNode.getChildren()){
            childTreeNode.getPayload().display(childTreeNode);
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
            IGTreeNode<ICodeNode> parentTreeNode = codeNode.getParentTreeNode();
            if(parentTreeNode == null){
                Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR);
            }
            JSONArray children = new JSONArray();
            for(ICodeNode childPayload : parentTreeNode.getPayloadChildren()){
                children.put(childPayload.toJson());
            }
            return children;
        }
    }
}
