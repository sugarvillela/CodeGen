package utiljson;

import codedef.modifier.CODE_NODE;
import codedef.modifier.MODIFIER;
import err.ERR_TYPE;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;

public class UtilJson {
    private static UtilJson instance;

    public static UtilJson initInstance(){
        return(instance ==  null)? (instance = new UtilJson()) : instance;
    }

    private UtilJson(){}

    public JSONObject getJObj(JSONObject jsonObj, String key){
        try{
            return jsonObj.getJSONObject(key);
        }
        catch(org.json.JSONException e){
            Glob.ERR.kill(ERR_TYPE.UNKNOWN_ID, key);
            return null;
        }
    }
    public JSONArray getJArr(JSONObject jsonObj, String key){
        try{
            return jsonObj.getJSONArray(key);
        }
        catch(org.json.JSONException e){
            Glob.ERR.kill(ERR_TYPE.UNKNOWN_ID, key);
            return null;
        }
    }

    public CODE_NODE getCodeNodeEnu(JSONObject jsonObj, String key){
        String enumName = getStr(jsonObj, key);
        return Glob.UTIL_ENUM.fromStringOrKill(CODE_NODE.class, enumName);
    }
    public MODIFIER getModifierEnu(JSONObject jsonObj, String key){
        String enumName = getStr(jsonObj, key);
        return Glob.UTIL_ENUM.fromStringOrKill(MODIFIER.class, key);
    }
    public String getStr(JSONObject jsonObj, String key){
        JSONArray jArr = getJArr(jsonObj, key);

        if(jArr.isEmpty()){
            //System.out.println("getStr: key="+key + ", jArr="+jArr.toString() + " EMPTY!!!");
            return "";
        }
        try{
            //System.out.println("getStr: key="+key + ", jArr="+jArr.toString() + " @0: " + jArr.get(0));
            return (String)jArr.get(0);
        }
        catch(ClassCastException e){
            Glob.ERR.kill(ERR_TYPE.INVALID_STRING, key);
            return null;
        }
    }
    public String getStr(JSONArray jArr, int i){
        if(jArr.isEmpty()){
            //System.out.println("getStr: i="+i + ", jArr="+jArr.toString() + " EMPTY!!!");
            return "";
        }
        try{
            //System.out.println("getStr: i="+i + ", jArr="+jArr.toString() + " @0: " + jArr.get(0));
            return (String)jArr.get(i);
        }
        catch(ClassCastException e){
            Glob.ERR.kill(ERR_TYPE.INVALID_STRING, jArr.toString());
            return null;
        }
    }
    public String[] jArrToStringArr(JSONArray jArr){
        int len = jArr.length();
        String[] strArr = new String[len];
        for(int i = 0; i < len; i++){
            strArr[i] = getStr(jArr, i);
        }
        return strArr;
    }
}
