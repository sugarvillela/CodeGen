package codedef.impl;

import err.ERR_TYPE;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;
import codedef.iface.IAttribModifier;
import codedef.modifier.*;
import tuple.TupPair;

import java.util.*;
import java.util.stream.Collectors;

import static codedef.modifier.CODE_NODE.METHOD;
import static codedef.modifier.MODIFIER.*;

public class AttribModifier  implements IAttribModifier {
    protected final CODE_NODE codeNodeEnum;
    protected List<MODIFIER> required, allowed;
    protected final HashMap<MODIFIER, String[]> attributes;
    protected final ModifierContentToJson contentToJson;
    protected final ModifierContentFromJson contentFromJson;

    public AttribModifier(CODE_NODE codeNodeEnum) {
        this.codeNodeEnum = codeNodeEnum;
        this.required = new ArrayList<>();
        this.allowed = new ArrayList<>();
        this.attributes = new HashMap<>();
        this.contentToJson = new ModifierContentToJson();
        this.contentFromJson = new ModifierContentFromJson();
        this.setModifier(CODE_NODE_TYPE, codeNodeEnum.toString());

        this.required.add(CODE_NODE_TYPE);
        this.required.add(NAME);

        this.allowed.add(IS_HEADER);
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
        this.contentToJson = new ModifierContentToJson();
        this.contentFromJson = new ModifierContentFromJson();
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
        return required;
    }

    @Override
    public List<MODIFIER> getAllowed() {
        return allowed;
    }

    @Override
    public HashMap<MODIFIER, String[]> getAttributes() {
        return attributes;
    }

    @Override
    public boolean isRequired(MODIFIER modifier) {
        return required.contains(modifier);
    }

    @Override
    public void assertHaveRequiredModifiers() {
        for(MODIFIER modifier : required){
            if(!attributes.containsKey(modifier)){
                Glob.ERR.kill(ERR_TYPE.MISSING_REQUIRED, modifier.toString());
            }
        }
    }

    @Override
    public void assertIsAllowed(MODIFIER modifier) {
        if(!allowed.contains(modifier) && !required.contains(modifier)){
            Glob.ERR.kill(ERR_TYPE.UNKNOWN_ID, modifier.toString());
        }
    }

    @Override
    public void put(MODIFIER modifier, String... values) {
        // Validate the value(s) from the JSON file
        // 1. Make sure modifier is allowed
        assertIsAllowed(modifier);
        // 2. Check values.length against ZERO, ONE, MANY requirement in MODIFIER
        Glob.ERR.check(
                modifier.initArgQuantity().assertValidQuantity(values.length),
                values
        );
        // 3. If value should be an enumerated string, check that valid string is provided
        if(modifier.isEnumerated()){
            Glob.ERR.check(
                    Glob.UTIL_ENUM.assertValid(modifier.enumeratedType(), values[0]),
                    values
            );
        }
        // 4. or check that numeric/bool settings are valid, strings are not empty or uppercase boolean
        else{
            for(String value : values){
                Glob.ERR.check(
                        modifier.initArgType().assertValidData(value),
                        value
                );
            }
        }
        switch(modifier){
            case DATA_TYPE:     // enumerated type, related to var value
                this.setDataType(values);
                break;
            case VAR_VALUE:     // related to data type
                this.setVarValue(values);
                break;
            default:            // all others
                this.setModifier(modifier, values);
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
    public List<TupPair<MODIFIER, String[]>> toList() {
        List<TupPair<MODIFIER, String[]>> out = new ArrayList<>(attributes.size());
        Iterator<Map.Entry<MODIFIER, String[]>> itr = attributes.entrySet().iterator();

        while(itr.hasNext()) {
            Map.Entry<MODIFIER, String[]> entry = itr.next();
            out.add(new TupPair<>(entry.getKey(), entry.getValue()));
        }
        return out;
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

    // DATA_TYPE and VAR_VALUE are related, and it is uncertain which will be set first.
    // If DATA_TYPE is set first, set a default VAR_VALUE
    // If VAR_VALUE is set first, infer a DATA_TYPE
    // If DATA_TYPE set after, and the VAR_VALUE is compatible, leave it; else reset the default VAR_VALUE
    private void setDataType(String dataTypeName[]) {
        ENU_DATA_TYPE dataTypeEnu;
        if((dataTypeEnu = ENU_DATA_TYPE.fromString(dataTypeName[0])) == null){// not a valid dataType
            Glob.ERR.kill(ERR_TYPE.SYNTAX);
            return;
        }
        String[] currValue = attributes.get(VAR_VALUE);
        if( // not set or is incompatible
            currValue == null ||
            dataTypeEnu.assertValidData(currValue[0]) != ERR_TYPE.NONE
        ){
            this.setModifier(VAR_VALUE, dataTypeEnu.getDefaultValue());
        }
        this.setModifier(MODIFIER.DATA_TYPE, dataTypeName);
    }
    private void setVarValue(String[] value) {
        String[] currDataType = attributes.get(DATA_TYPE);
        ENU_DATA_TYPE dataTypeEnu;
        if( // not set or is incompatible
            currDataType == null ||
                    (dataTypeEnu = ENU_DATA_TYPE.fromString(currDataType[0])).
                    assertValidData(value[0]) != ERR_TYPE.NONE
        ){
            this.setModifier(
                MODIFIER.DATA_TYPE,
                ENU_DATA_TYPE.findCompatibleType(
                    value[0],
                    (METHOD == this.codeNodeEnum)).toString()
            );
        }
        this.setModifier(VAR_VALUE, value[0]);
    }

    private void setModifier(MODIFIER modifier, String... value) {
        attributes.put(modifier, value);
    }

    protected String mapToString(HashMap<MODIFIER, String[]> map){
        return map.entrySet()
                .stream()
                .map(e -> e.getKey() + ":" + Arrays.toString(e.getValue()))
                .collect(Collectors.joining(","));
    }

    @Override
    public void fromJson(JSONObject attribObject) {
        contentFromJson.mergeJsonData(this, attribObject);
    }

    @Override
    public JSONObject toJson() {
        return contentToJson.getJsonObj(this);
    }

    private static class ModifierContentToJson {
        public JSONObject getJsonObj(IAttribModifier attribModifier){
            return new JSONObject(attribModifier.getAttributes());
        }
    }

    private static class ModifierContentFromJson {
        public void mergeJsonData(IAttribModifier attribModifier, JSONObject attribObject) {
            Iterator<?> keys = attribObject.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                MODIFIER modifier = Glob.UTIL_JSON.getModifierEnu(attribObject, key);

                JSONArray jArr = Glob.UTIL_JSON.getJArr(attribObject, key);
                if(jArr.isEmpty()){
                    attribModifier.clear(modifier);
                }
                else{
                    attribModifier.put(modifier, Glob.UTIL_JSON.jArrToStringArr(jArr));
                }
            }
        }
    }
}
