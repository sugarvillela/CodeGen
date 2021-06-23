package codejson.iface;

import codedef.modifier.CODE_NODE;
import codedef.modifier.MODIFIER;
import err.ERR_TYPE;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;

public interface IErrCatch {
    default JSONObject toJObj(Object object){
        try{
            //System.out.println("getStr: i="+i + ", jArr="+jArr.toString() + " @0: " + jArr.get(0));
            return (JSONObject)object;
        }
        catch(ClassCastException e){
            Glob.ERR.kill(ERR_TYPE.BAD_JSON, object.toString());
        }
        return null;
    }
    default JSONObject toJObj(JSONObject parentObj, String key){
        try{
            return parentObj.getJSONObject(key);
        }
        catch(org.json.JSONException e){
            Glob.ERR.kill(ERR_TYPE.UNKNOWN_ID, key);
        }
        return null;
    }

    default JSONArray toJArr(JSONObject parentObj, String key){
        try{
            return parentObj.getJSONArray(key);
        }
        catch(org.json.JSONException e){
            Glob.ERR.kill(ERR_TYPE.UNKNOWN_ID, key);
        }
        return null;
    }

    // There are no strings, only string arrays
    default String toStr(JSONObject parentObj, String key){
        return this.toStr(this.toJArr(parentObj, key));
    }

    default String toStr(JSONArray jArr){
        if(jArr.isEmpty()){
            //System.out.println("getStr: i="+i + ", jArr="+jArr.toString() + " EMPTY!!!");
            return "";
        }
        try{
            return jArr.getString(0);
        }
        catch(org.json.JSONException e){
            Glob.ERR.kill(ERR_TYPE.INVALID_STRING, jArr.toString());
        }
        return null;
    }
    default String[] toArray(JSONArray jArr){
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
    default CODE_NODE toCodeNodeEnum(String enumName){
        return Glob.UTIL_ENUM.fromStringOrKill(CODE_NODE.class, enumName);
    }
    default MODIFIER toModifierEnum(String enumName){
        return Glob.UTIL_ENUM.fromStringOrKill(MODIFIER.class, enumName);
    }
}
