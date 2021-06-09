package utilfile;

import err.ERR_TYPE;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import runstate.Glob;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UtilFileJson {
    private int indent;

    public UtilFileJson(){
        indent = 2;
    }
    public void setIndent(int indent){
        this.indent = indent;
    }

    /** If file content is unknown, get an object and check type before using
     * @return JSONObject or JSONArray as Object supertype */
    public Object getObject(String filePath){
        try {
            File file = new File(filePath);
            String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
            return (content.charAt(0) == '{')? new JSONObject(content) : new JSONArray(content);
        }
        catch (StringIndexOutOfBoundsException e){
            Glob.ERR.kill(ERR_TYPE.FILE_ERROR, "The file is empty");
        }
        catch (NullPointerException e) {
            Glob.ERR.kill(ERR_TYPE.FILE_ERROR, "The file path may be null");
        }
        catch (JSONException e) {
            Glob.ERR.kill(ERR_TYPE.BAD_JSON, e.getMessage());
        }
        catch (IOException e) {
            Glob.ERR.kill(ERR_TYPE.FILE_ERROR, e.getMessage());
        }
        catch (Exception e) {
            Glob.ERR.kill(ERR_TYPE.INTERNAL_ERROR, e.toString());
        }
        return null;
    }
    /** If file content is known, get the appropriate JSON type
     * @return JSONObject, if file describes the correct type */
    public JSONObject getJsonObject(String filePath){
        try {
            return (JSONObject)this.getObject(filePath);
        }
        catch (ClassCastException e) {
            Glob.ERR.kill("JSON type is not a JSON object. Try JSON Array");
        }
        catch (Exception e) {
            Glob.ERR.kill(ERR_TYPE.FILE_ERROR, e.getMessage());
        }
        return null;
    }
    /** If file content is known, get the appropriate JSON type
     * @return JSONArray, if file describes the correct type */
    public JSONArray getJsonArray(String filePath){
        try {
            return (JSONArray)this.getObject(filePath);
        }
        catch (ClassCastException e) {
            Glob.ERR.kill("JSON type is not a JSON Array. Try JSON Object");
        }
        catch (Exception e) {
            Glob.ERR.kill(ERR_TYPE.FILE_ERROR, e.getMessage());
        }
        return null;
    }

    public void put(JSONObject jsonObject, String filePath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(jsonObject.toString(indent));
        } catch (IOException e) {
            Glob.ERR.kill(ERR_TYPE.FILE_ERROR, e.getMessage());
        }
    }
    public void put(JSONArray jsonArray, String filePath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(jsonArray.toString(indent));
        } catch (IOException e) {
            Glob.ERR.kill(ERR_TYPE.FILE_ERROR, e.getMessage());
        }
    }
}
