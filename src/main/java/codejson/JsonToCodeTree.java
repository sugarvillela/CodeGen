package codejson;

import codedef.iface.ICodeNode;
import codedef.modifier.CODE_NODE;
import generictree.impl.CodeNodeTree;
import langdef.LangConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;

import java.util.List;

import static codedef.modifier.MODIFIER.CODE_NODE_TYPE;
import static codedef.modifier.MODIFIER.NAME;

public class JsonToCodeTree {

    private final CodeNodeTree codeTree;

    public JsonToCodeTree() {
        codeTree = new CodeNodeTree(LangConstants.PATH_TREE_SEP);
    }

    public void buildTree(JSONObject jsonObj){
        recurse(jsonObj, 0);
        List<String> allPaths = codeTree.getParse().getAllPaths(codeTree.getRoot(), '-');

        //System.out.println("\npaths");
        //System.out.println(String.join("\n", allPaths));
    }
    private void recurse(JSONObject jsonObj, int level){
        JSONObject jsonAttr = Glob.UTIL_JSON.getJObj(jsonObj, "attributes");
        CODE_NODE codeNodeEnum = Glob.UTIL_JSON.getCodeNodeEnu(jsonAttr, CODE_NODE_TYPE.toString());
        ICodeNode codeNode = Glob.PROTOTYPE_FACTORY.getPrototype(codeNodeEnum);
        codeNode.getAttribModifier().fromJson(jsonAttr);

        String identifier = Glob.UTIL_JSON.getStr(jsonAttr, NAME.toString());
        System.out.println("start: level=" + level + ": " + identifier);
        codeTree.put(codeNode, identifier);
        JSONArray children = Glob.UTIL_JSON.getJArr(jsonObj, "children");
        for(Object childObj : children){
            recurse((JSONObject)childObj, level + 1);
        }
        codeTree.pathBack();
        //System.out.println("done:  level=" + level + ": " + identifier);
    }
    public CodeNodeTree getTree(){
        return codeTree;
    }
}
