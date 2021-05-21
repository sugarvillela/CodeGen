package codedef.impl;

import codedef.iface.IAttribModifier;
import err.ERR_TYPE;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;
import codedef.modifier.CODE_NODE;

import java.util.*;
import java.util.stream.Stream;

public class AttribStruct implements IAttribStruct {
    protected final CODE_NODE codeNodeEnum;
    protected List<CODE_NODE> required, allowed;
    protected final StructContentToJson contentToJson;

    public AttribStruct(CODE_NODE codeNodeEnum) {
        this.codeNodeEnum = codeNodeEnum;
        this.required = new ArrayList<>();
        this.allowed = new ArrayList<>();
        this.contentToJson = new StructContentToJson();
    }
    public AttribStruct(CODE_NODE codeNodeEnum, List<CODE_NODE> required, List<CODE_NODE> allowed) {
        this.codeNodeEnum = codeNodeEnum;
        this.required = required;
        this.allowed = allowed;
        this.contentToJson = new StructContentToJson();
    }

    @Override
    public void initRequired(CODE_NODE... requiredChildren) {
        this.required = new ArrayList<>(Arrays.asList(requiredChildren));
    }

    @Override
    public void initAllowed(CODE_NODE... allowedChildren) {
        this.allowed = (required == null)? new ArrayList<>() : new ArrayList<>(required);
        this.allowed.addAll(Arrays.asList(allowedChildren));
    }

    @Override
    public List<CODE_NODE> getRequired() {
        return required;
    }

    @Override
    public List<CODE_NODE> getAllowed() {
        return allowed;
    }

    @Override
    public boolean isRequired(CODE_NODE childEnum) {
        return required != null && required.contains(childEnum);
    }

    @Override
    public void assertHaveRequiredChildren(List<ICodeNode> children) {
        if(required != null){
            Stream<ICodeNode> stream = children.stream();
            for(CODE_NODE requiredEnum : required){
                if(!stream.anyMatch(child -> child.codeNodeEnum() == requiredEnum)){
                    Glob.ERR.kill(ERR_TYPE.MISSING_REQUIRED, requiredEnum.toString());
                }
            }
        }
    }

    @Override
    public void assertIsAllowedChild(CODE_NODE newChildEnum) {
        if(allowed == null || !allowed.contains(newChildEnum)){
            Glob.ERR.kill(ERR_TYPE.DISALLOWED_NESTING, newChildEnum.toString());
        }
    }

    @Override
    public IAttribStruct prototype() {
        return new AttribStruct(this.codeNodeEnum, new ArrayList<>(required), new ArrayList<>(allowed));
    }

    @Override
    public String friendlyString() {
        return null;
    }

    @Override
    public String csvString() {
        return "IAttribStruct{" +
                "codeNodeEnum=" + codeNodeEnum +
                ",required=" + required +
                ",allowed=" + allowed +
                "}" +
                "}";
    }

    @Override
    public void fromJson(JSONObject jsonObject) {

    }

    @Override
    public JSONObject toJson() {
        return contentToJson.getJsonObj(this);
    }

    private static class StructContentToJson {
        public JSONObject getJsonObj(IAttribStruct attribStruct){
            JSONObject out = new JSONObject();
            out.put("required", new JSONArray(attribStruct.getRequired()));
            out.put("allowed", new JSONArray(attribStruct.getAllowed()));
            return out;
        }
    }
}