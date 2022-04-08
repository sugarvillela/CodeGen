package codejson;

import codedef.enums.CODE_NODE;
import codedef.enums.MODIFIER;
import err.ERR_TYPE;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;

/**For accessing org.json Objects, Arrays and Strings, with exceptions
 * mapped to an alternate error reporting system.
 *
 * Expectation for Json encoding:
 * Numbers, booleans and strings are all encoded as strings, which are further
 * encoded as single-element String arrays.
 * Thus, String access involves passing a JSONArray and returning element 0.
 * There are no Strings in CodeGen-formatted Json files, only string arrays
 */
public class JsonErrHandler {
    public static JsonErrHandler initInstance(){
        return new JsonErrHandler();
    }

    private JsonErrHandler(){}

    /** Try to class cast object to JSON object */
    public JSONObject toJObj(Object object){
        try{
            return (JSONObject)object;
        }
        catch(ClassCastException e){
            Glob.ERR.kill(ERR_TYPE.BAD_JSON, object.toString());
        }
        return null;
    }

    /** Try to get Json object from parent with key */
    public JSONObject toJObj(JSONObject parentObj, String key){
        try{
            return parentObj.getJSONObject(key);
        }
        catch(org.json.JSONException e){
            Glob.ERR.kill(ERR_TYPE.UNKNOWN_ID, key);
        }
        return null;
    }

    /** Try to get Json array from parent with key */
    public JSONArray toJArr(JSONObject parentObj, String key){
        try{
            return parentObj.getJSONArray(key);
        }
        catch(org.json.JSONException e){
            Glob.ERR.kill(ERR_TYPE.UNKNOWN_ID, key);
        }
        return null;
    }

    /** Try to get String from Json object with key. */
    public String toStr(JSONObject parentObj, String key){
        return this.toStr(this.toJArr(parentObj, key));
    }

    /** Try to get element 0 from Json array. */
    public String toStr(JSONArray jArr){
        return this.toStr(jArr, 0);
    }

    /** Try to get String from Json array with index */
    public String toStr(JSONArray jArr, int i){
        if(jArr.isEmpty()){
            return "";
        }
        try{
            return jArr.getString(i);
        }
        catch(org.json.JSONException e){
            Glob.ERR.kill(ERR_TYPE.INVALID_STRING, jArr.toString());
        }
        return null;
    }

    /** Try to get all strings from Json array */
    public String[] toArray(JSONArray jArr){
        int len = jArr.length();
        String[] strArr = new String[len];
        try{
            for(int i = 0; i < len; i++){
                strArr[i] = jArr.getString(i);
            }
        }
        catch(org.json.JSONException e){
            Glob.ERR.kill(ERR_TYPE.INVALID_STRING, jArr.toString());
        }
        return strArr;
    }

    /** Try to get enum from string; UTIL_ENUM handles error */
    public CODE_NODE toCodeNodeEnum(String enumName){
        return Glob.UTIL_ENUM.fromStringOrKill(CODE_NODE.class, enumName);
    }

    /** Try to get enum from string; UTIL_ENUM handles error */
    public MODIFIER toModifierEnum(String enumName){
        return Glob.UTIL_ENUM.fromStringOrKill(MODIFIER.class, enumName);
    }
}

