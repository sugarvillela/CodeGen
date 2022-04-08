package utilfile;

import mock.MockSource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import runstate.Glob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonParseTest<T> {
    MockSource mockSource = new MockSource();
    boolean show = true;

    @Test
    void givenJSONObjectFile_retrieveJSONObject(){
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("colors.json");
        System.out.println("filePath: " + filePath);
        JSONObject jsonObj = UtilFileJson.initInstance().getJsonObject(filePath);
        System.out.println("jsonObj: " + jsonObj);
        String expected = "{\"attrib\":[{\"color\":\"red\",\"value\":\"#f00\"},{\"color\":\"green\",\"value\":\"#0f0\"},{\"color\":\"blue\",\"value\":\"#00f\"},{\"color\":\"cyan\",\"value\":\"#0ff\"},{\"color\":\"magenta\",\"value\":\"#f0f\"},{\"color\":\"yellow\",\"value\":\"#ff0\"},{\"color\":\"black\",\"value\":\"#000\"}]}";
        Assertions.assertEquals(expected, jsonObj.toString());
    }
    @Test
    void givenJSONArrayFile_retrieveJSONArray(){
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("colors2.json");
        System.out.println("filePath: " + filePath);
        JSONArray jsonArray = UtilFileJson.initInstance().getJsonArray(filePath);
        System.out.println("jsonArray: " + jsonArray);
        String expected = "[{\"attrib\":[{\"color\":\"red\",\"value\":\"#f00\"},{\"color\":\"green\",\"value\":\"#0f0\"},{\"color\":\"blue\",\"value\":\"#00f\"},{\"color\":\"cyan\",\"value\":\"#0ff\"},{\"color\":\"magenta\",\"value\":\"#f0f\"},{\"color\":\"yellow\",\"value\":\"#ff0\"},{\"color\":\"black\",\"value\":\"#000\"}]}]";
        Assertions.assertEquals(expected, jsonArray.toString());
    }
    @Test
    void givenJSONObjectFile5_buildTree(){
//        CodeNodeTree pathTree = mockSource.getPopulatedTreeFromFile("test2.json");
//
//        IGTreeParse<ICodeNode> parser = pathTree.getParse();
//        IGTreeTask<ICodeNode> disp = new TaskDisp<>();
//        parser.breadthFirst(pathTree.getRoot(), disp);
//
//        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');
//        assertEqStrings(allPaths, "GLOB-package1-file1-class1", "GLOB-package1-file2-class2", "GLOB-package2-file3-class3", "GLOB-package2-file4-class4");
//
    }
    @Test
    void givenJSONObjectFile6_buildUnevenTree(){
//        CodeNodeTree pathTree = mockSource.getPopulatedTreeFromFile("test3.json");
//
//        IGTreeParse<ICodeNode> parser = pathTree.getParse();
//        IGTreeTask<ICodeNode> disp = new TaskDisp<>();
//        parser.breadthFirst(pathTree.getRoot(), disp);
//
//        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');
//        System.out.println(String.join("\n", allPaths));
//        assertEqStrings(
//                allPaths,
//                "GLOB-package1-file1-class1-method1-METHOD_ARGS-arg1",
//                "GLOB-package1-file1-class1-method1-METHOD_ARGS-arg2",
//                "GLOB-package2"
//        );

    }
    
    public interface IStrategy <T>{
        T increment(String key, T acc);
        void doTask(Object object, T acc);
    }
    public class Display implements IStrategy <Integer>{
        public final String tab(String text, Integer acc){
            return new String(new char[acc]).replace('\0', '.') + text;
        }
        public Integer increment(String key, Integer acc){
            return acc + 2;
        }

        @Override
        public void doTask(Object object, Integer acc) {
            System.out.println(tab(object.toString(), acc));
        }
    }

    public static class JsonRecurse <T> {
        public void recurse(Object obj, IStrategy strategy, T acc){
            if (obj instanceof JSONObject){
                this.recurse((JSONObject)obj, strategy, acc);
            }
            else if(obj instanceof JSONArray){
                this.recurse((JSONArray)obj, strategy, acc);
            }
            else{
                Glob.ERR.kill("Not a JSON object");
            }
        }
        public void recurse(JSONObject currObj, IStrategy<T> strategy, T acc){
            Iterator<?> keys = currObj.keySet().iterator();

            while(keys.hasNext()) {
                String key = (String)keys.next();
                Object obj = currObj.get(key);
                if (obj instanceof JSONObject){
                    acc = (T)strategy.increment(key, acc);
                    this.recurse((JSONObject)obj, strategy, acc);
                }
                else if(obj instanceof JSONArray){
                    this.recurse((JSONArray)obj, strategy, acc);
                }
                else{
                    strategy.doTask(obj, acc);
                    //System.out.println(obj);
                }
            }
        }
        public void recurse(JSONArray currArray, IStrategy<T> strategy, T acc){
            for (Object obj : currArray) {
                if (obj instanceof JSONObject){
                    this.recurse((JSONObject)obj, strategy, acc);
                }
                else if(obj instanceof JSONArray){
                    this.recurse((JSONArray)obj, strategy, acc);
                }
                else{
                    strategy.doTask(obj, acc);
                }
            }
        }
    }

    public static class JsonFlattener {
        private final List<String> flatList;

        public JsonFlattener(List<String> flatList) {
            this.flatList = flatList;
        }

        public void recurse(Object obj){
            if (obj instanceof JSONObject){
                this.recurse((JSONObject)obj, "");
            }
            else if(obj instanceof JSONArray){
                this.recurse((JSONArray)obj, "");
            }
            else{
                Glob.ERR.kill("Not a JSON object");
            }
        }
        public void recurse(JSONObject currObj, String acc){
            Iterator<?> keys = currObj.keySet().iterator();
            while(keys.hasNext()) {
                String key = (String)keys.next();
                String nextAcc = this.prepend(key, acc);
                Object obj = currObj.get(key);
                if (obj instanceof JSONObject){
                    this.recurse((JSONObject)obj, nextAcc);
                }
                else if(obj instanceof JSONArray){
                    this.recurse((JSONArray)obj, nextAcc);
                }
                else{
                    flatList.add(nextAcc + ":" + obj);
                }
            }
        }
        public void recurse(JSONArray currArray, String acc){
            int key = 0;
            for (Object obj : currArray) {
                String nextAcc = this.prepend(key, acc);
                if (obj instanceof JSONObject){
                    this.recurse((JSONObject)obj, nextAcc);
                }
                else if(obj instanceof JSONArray){
                    this.recurse((JSONArray)obj, nextAcc);
                }
                else{
                    flatList.add(nextAcc + ':' + obj);
                }
                key++;
            }
        }
        private String prepend(int key, String acc){
            return (acc.isEmpty())? String.valueOf(key) : acc + '-' + key;
        }
        private String prepend(String key, String acc){
            return (acc.isEmpty())? key : acc + '-' + key;
        }
    }
}
