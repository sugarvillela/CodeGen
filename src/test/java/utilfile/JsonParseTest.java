package utilfile;

import codedef.iface.ICodeNode;
import codejson.JsonToCodeTree;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;
import generictree.impl.CodeNodeTree;
import generictree.task.TaskDisp;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import runstate.Glob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonParseTest<T> {
    boolean show = true;
    private void assertEqNodes(List<IGTreeNode<String>> actual, String... expected){
        List<String> actualToString = new ArrayList<>();
        int i = 0;
        for(IGTreeNode<String> node : actual){
            String s = node.csvString();
            if(show){
                System.out.println(s);
            }
            actualToString.add(s);
        }
        assertEqStrings(actualToString, expected);
    }
    private void assertEqStrings(List<String> actual, String... expected){
        if(show){
            System.out.println("\"" + String.join("\", \"", actual) + "\"");
        }
        String a = String.join("|", expected);
        String b = String.join("|", actual);
        Assertions.assertEquals(a, b);
    }

    @Test
    void givenJSONObjectFile_retrieveJSONObject(){
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test1.json");
        System.out.println("filePath: " + filePath);
        JSONObject jsonObj = new JsonObjFile().getJsonObject(filePath);
        System.out.println("jsonObj: " + jsonObj);
        String expected = "{\"attrib\":[{\"color\":\"red\",\"value\":\"#f00\"},{\"color\":\"green\",\"value\":\"#0f0\"},{\"color\":\"blue\",\"value\":\"#00f\"},{\"color\":\"cyan\",\"value\":\"#0ff\"},{\"color\":\"magenta\",\"value\":\"#f0f\"},{\"color\":\"yellow\",\"value\":\"#ff0\"},{\"color\":\"black\",\"value\":\"#000\"}]}";
        Assertions.assertEquals(expected, jsonObj.toString());
    }
    @Test
    void givenJSONArrayFile_retrieveJSONArray(){
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test3.json");
        System.out.println("filePath: " + filePath);
        JSONArray jsonArray = new JsonObjFile().getJsonArray(filePath);
        System.out.println("jsonArray: " + jsonArray);
        String expected = "[{\"attrib\":[{\"color\":\"red\",\"value\":\"#f00\"},{\"color\":\"green\",\"value\":\"#0f0\"},{\"color\":\"blue\",\"value\":\"#00f\"},{\"color\":\"cyan\",\"value\":\"#0ff\"},{\"color\":\"magenta\",\"value\":\"#f0f\"},{\"color\":\"yellow\",\"value\":\"#ff0\"},{\"color\":\"black\",\"value\":\"#000\"}]}]";
        Assertions.assertEquals(expected, jsonArray.toString());
    }
    @Test
    void givenJSONObjectFile5_buildTree(){
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test5.json");
        System.out.println("filePath: " + filePath);
        JSONObject jsonRoot = new JsonObjFile().getJsonObject(filePath);
        JsonToCodeTree jsonTree = new JsonToCodeTree();
        jsonTree.buildTree(jsonRoot);
        CodeNodeTree pathTree = jsonTree.getTree();

        IGTreeParse<ICodeNode> parser = pathTree.getParse();
        IGTreeTask<ICodeNode> disp = new TaskDisp<>();
        parser.breadthFirst(pathTree.getRoot(), disp);

        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');
        assertEqStrings(allPaths, "GLOB-package1-file1-class1", "GLOB-package1-file2-class2", "GLOB-package2-file3-class3", "GLOB-package2-file4-class4");
    }
    @Test
    void givenJSONObjectFile6_buildUnevenTree(){
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test6.json");
        System.out.println("filePath: " + filePath);
        JSONObject jsonRoot = new JsonObjFile().getJsonObject(filePath);
        JsonToCodeTree jsonTree = new JsonToCodeTree();
        jsonTree.buildTree(jsonRoot);
        CodeNodeTree pathTree = jsonTree.getTree();

        IGTreeParse<ICodeNode> parser = pathTree.getParse();
        IGTreeTask<ICodeNode> disp = new TaskDisp<>();
        parser.breadthFirst(pathTree.getRoot(), disp);

        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');
        assertEqStrings(allPaths, "GLOB-package1-file1-class1-method1", "GLOB-package1-file1-class1-METHOD_BODY", "GLOB-package1-file1-class1-METHOD_ARGS-arg1", "GLOB-package1-file1-class1-METHOD_ARGS-arg2", "GLOB-package2");
    }
    @Test
    void test2(){
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test2.json");
        System.out.println("filePath: " + filePath);
        Object obj = new JsonObjFile().getObject(filePath);

        System.out.println("jsonObj recurse: ");
        IStrategy<Integer> strategy = new Display();
        JsonRecurse<Integer> jsonRecurse = new JsonRecurse<>();
        jsonRecurse.recurse(obj, strategy, -2);
    }
    @Test
    void test3(){
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test2.json");
        System.out.println("filePath: " + filePath);
        Object obj = new JsonObjFile().getObject(filePath);

        System.out.println("jsonObj recurse: ");
        List<String> flatList = new ArrayList<>();
        JsonFlattener flattened = new JsonFlattener(flatList);
        flattened.recurse(obj);

        for(String line : flatList){
            System.out.println(line);
        }
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
