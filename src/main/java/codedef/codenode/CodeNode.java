package codedef.codenode;

import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;
import codedef.modifier.*;
import err.ERR_TYPE;
import generictree.iface.IGTreeNode;
import langformat.iface.INullableUtil;
import langformat.impl.NullableUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;
import translators.iface.ITranslator;

public class CodeNode implements ICodeNode {
    protected final CODE_NODE codeNodeEnum;
    protected final IAttribModifier attribModifier;
    protected final IAttribStruct attribStruct;
    protected final CodeNodeContentToJson contentToJson;
    protected IGTreeNode<ICodeNode> parentTreeNode;
    protected final ITranslator translator;
    protected final INullableUtil lineUtil;

    public CodeNode(CODE_NODE codeNodeEnum, IAttribModifier attribModifier, IAttribStruct attribStruct) {
        this.codeNodeEnum = codeNodeEnum;
        this.attribModifier = attribModifier;
        this.attribStruct = attribStruct;
        this.translator = Glob.OUT_LANG_MANAGER.getTranslatorFactory().get(codeNodeEnum);
        this.contentToJson = new CodeNodeContentToJson();
        lineUtil = NullableUtil.initInstance();
    }

    @Override
    public CODE_NODE codeNodeEnum() {
        return this.codeNodeEnum;
    }

    @Override
    public IAttribModifier getAttribModifier() {
        return attribModifier;
    }

    @Override
    public IAttribStruct getAttribStruct() {
        return attribStruct;
    }

    @Override
    public void setParentTreeNode(IGTreeNode<ICodeNode> parentTreeNode) {
        this.parentTreeNode = parentTreeNode;
//        System.out.println("set parent:");
//        System.out.println("    p=" + parentTreeNode.csvString());
//        System.out.println("    c=" + this.csvString());
    }

    @Override
    public IGTreeNode<ICodeNode> getParentTreeNode() {
        return this.parentTreeNode;
    }

    @Override
    public void set(ICodeNode newNode) {

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
        return new CodeNode(codeNodeEnum, attribModifier.prototype(), attribStruct.prototype());
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
                "\n    attribStruct=" + attribStruct.csvString() +
                "\n}";
    }

    @Override
    public String csvString() {
        return codeNodeEnum +
                "," + attribModifier.csvString();// +
                //"," + attribStruct.csvString();
    }

    private static class CodeNodeContentToJson {
        public JSONObject getJsonObj(ICodeNode codeNode) {
            JSONObject out = new JSONObject();
            out.put("attributes", codeNode.getAttribModifier().toJson());
            //out.put("attribStruct", codeNode.getAttribStruct().toJson());
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
