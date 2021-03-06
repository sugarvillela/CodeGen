package codedef.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;
import codedef.enums.CODE_NODE;

import java.util.*;
import java.util.stream.Stream;

public class AttribStruct implements IAttribStruct {
    protected final CODE_NODE codeNodeEnum;
    protected List<CODE_NODE> required, allowed;
    protected final JsonAttribExporter contentToJson;

    public AttribStruct(CODE_NODE codeNodeEnum) {
        this.codeNodeEnum = codeNodeEnum;
        this.required = new ArrayList<>();
        this.allowed = new ArrayList<>();
        this.contentToJson = new JsonAttribExporter();
    }
    public AttribStruct(CODE_NODE codeNodeEnum, List<CODE_NODE> required, List<CODE_NODE> allowed) {
        this.codeNodeEnum = codeNodeEnum;
        this.required = required;
        this.allowed = allowed;
        this.contentToJson = new JsonAttribExporter();
    }

    @Override
    public void initRequired(CODE_NODE... requiredChildren) {
        this.required.addAll(Arrays.asList(requiredChildren));
    }

    @Override
    public void initAllowed(CODE_NODE... allowedChildren) {
        this.allowed.addAll(Arrays.asList(allowedChildren));
    }

    @Override
    public List<CODE_NODE> getRequired() {
        return new ArrayList<>(required);
    }

    @Override
    public List<CODE_NODE> getAllowed() {
        return new ArrayList<>(allowed);
    }

    @Override
    public CODE_NODE reportMissingChild(List<ICodeNode> children) {
        if(required != null){
            Stream<ICodeNode> stream = children.stream();
            for(CODE_NODE requiredEnum : required){
                if(stream.noneMatch(child -> (child.enumGroup() == requiredEnum || child.getCodeNodeEnum() == requiredEnum))){
                    return requiredEnum;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isAllowedChild(CODE_NODE childEnum) {
        return(allowed.contains(childEnum) || required.contains(childEnum));
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
    public void importJson(JSONObject jsonObject) {}

    @Override
    public JSONObject exportJson() {
        return contentToJson.getJsonObj(this);
    }

    private static class JsonAttribExporter {
        public JSONObject getJsonObj(IAttribStruct attribStruct){
            JSONObject out = new JSONObject();
            out.put("required", new JSONArray(attribStruct.getRequired()));
            out.put("allowed", new JSONArray(attribStruct.getAllowed()));
            return out;
        }
    }
}