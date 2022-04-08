package codedef.impl;

import err.ERR_TYPE;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;
import codedef.iface.IAttribModifier;
import codedef.enums.*;

import java.util.*;
import java.util.stream.Collectors;

import static codedef.enums.ENU_BOOLEAN.FALSE;
import static codedef.enums.ENU_BOOLEAN.TRUE;
import static codedef.enums.MODIFIER.*;

public class AttribModifier  implements IAttribModifier {
    protected final CODE_NODE codeNodeEnum;
    protected List<MODIFIER> required, allowed;
    protected final HashMap<MODIFIER, String[]> attributes;
    protected final JsonAttribExporter contentToJson;
    protected final JsonAttribImporter contentFromJson;

    public AttribModifier(CODE_NODE codeNodeEnum) {
        this.codeNodeEnum = codeNodeEnum;
        this.required = new ArrayList<>();
        this.allowed = new ArrayList<>();
        this.attributes = new HashMap<>();
        this.contentToJson = new JsonAttribExporter();
        this.contentFromJson = new JsonAttribImporter();
        this.setModifier(TYPE_, codeNodeEnum.toString());

        this.required.add(TYPE_);
        this.required.add(NAME_);

        this.allowed.add(IS_HEADER_);
    }
    public AttribModifier(
            CODE_NODE codeNodeEnum,
            List<MODIFIER> required,
            List<MODIFIER> allowed,
            HashMap<MODIFIER, String[]> attributes
    ) {
        this.codeNodeEnum = codeNodeEnum;
        this.required = required;
        this.allowed = allowed;
        this.attributes = attributes;
        this.contentToJson = new JsonAttribExporter();
        this.contentFromJson = new JsonAttribImporter();
    }

    @Override
    public void initRequired(MODIFIER... requiredModifiers) {
        this.required.addAll(Arrays.asList(requiredModifiers));
    }

    @Override
    public void initAllowed(MODIFIER... allowedModifiers) {
        this.allowed.addAll(Arrays.asList(allowedModifiers));
    }

    @Override
    public List<MODIFIER> getRequired() {
        return new ArrayList<>(required);
    }

    @Override
    public List<MODIFIER> getAllowed() {
        return new ArrayList<>(allowed);
    }

    @Override
    public HashMap<MODIFIER, String[]> getAttributes() {
        return attributes;
    }

    @Override
    public void put(MODIFIER modifier, Object... objects) {
        // Validate the value(s) from the JSON file

        // 1. Make sure modifier is allowed
        this.assertIsAllowedModifier(modifier);
        // 2. Check values against STRING, INT, FLOAT, BOOL requirement in MODIFIER
        // 3. Check values.length against ZERO, ONE, MANY requirement in MODIFIER
        // 4. Check values against enumerated string type, if any
        Object o = Glob.ATTRIB_CONVERT_UTIL;
        //AttribConvertUtil o = AttribConvertUtil.initInstance();
        String[] valuesAsStr = Glob.ATTRIB_CONVERT_UTIL.convert(modifier, objects);
        this.setModifier(modifier, valuesAsStr);
    }

    @Override
    public void assertIsAllowedModifier(MODIFIER modifier) {
        if(!allowed.contains(modifier) && !required.contains(modifier)){
            List<MODIFIER> allAllowed = new ArrayList(allowed.size() + required.size());
            allAllowed.addAll(allowed);
            allAllowed.addAll(required);

            String desc = String.format(
                    "Found %s in %s, allowed fields are %s",
                    modifier,
                    codeNodeEnum,
                    String.join(
                            ", ",
                            Glob.UTIL_ENUM.enumListToStringList(allAllowed)
                    )
            );
            Glob.ERR.kill(ERR_TYPE.DISALLOWED_ATTRIB, desc);
        }
    }

    @Override
    public void assertHaveAllRequired() {
        for(MODIFIER modifier : required){
            if(!attributes.containsKey(modifier)){
                String desc = String.format(
                        "Missing %s in %s, required fields are %s",
                        modifier,
                        codeNodeEnum,
                        String.join(
                                ", ",
                                Glob.UTIL_ENUM.enumListToStringList(required)
                        )
                );
                Glob.ERR.kill(ERR_TYPE.MISSING_REQUIRED, desc);
            }
        }
    }

    @Override
    public void clear(MODIFIER modifier) {
        this.setModifier(modifier);
    }

    @Override
    public String[] get(MODIFIER modifier) {
        return attributes.get(modifier);
    }

    @Override
    public IAttribModifier prototype() {
        return new AttribModifier(
                codeNodeEnum,
                new ArrayList<>(required),
                new ArrayList<>(allowed),
                new HashMap<>(attributes)
        );
    }

    @Override
    public String friendlyString() {
        return null;
    }

    @Override
    public String csvString() {
        return "ICodeAttrib{" +
                "codeNodeEnum=" + codeNodeEnum +
                ",required=" + required +
                ",allowed=" + allowed +
                ",attributes={" + mapToString(attributes) +
                "}" +
                "}";
    }

    private void setModifier(MODIFIER modifier, String... value) {
        attributes.put(modifier, value);
    }

    protected String mapToString(HashMap<MODIFIER, String[]> map){
        return map.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + Arrays.toString(e.getValue()))
                .collect(Collectors.joining(","));
    }

    @Override
    public void importJson(JSONObject attrObj) {
        contentFromJson.readIn(this, attrObj);
    }

    @Override
    public JSONObject exportJson() {
        return contentToJson.getJsonObj(this);
    }

    private static class JsonAttribExporter {
        public JSONObject getJsonObj(IAttribModifier attribModifier){
            return new JSONObject(attribModifier.getAttributes());
        }
    }

    private static class JsonAttribImporter {
        public void readIn(IAttribModifier attribModifier, JSONObject attrObj) {
            for (String key : attrObj.keySet()) {
                MODIFIER modifier = Glob.JSON_ERR_HANDLER.toModifierEnum(key);

                JSONArray jArr = Glob.JSON_ERR_HANDLER.toJArr(attrObj, key);
                if (jArr.isEmpty()) {
                    attribModifier.clear(modifier);
                } else {
                    attribModifier.put(modifier, Glob.JSON_ERR_HANDLER.toArray(jArr));
                }
            }
        }
    }
}
