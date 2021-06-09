package codejson;

import codedef.iface.ICodeNode;
import codedef.modifier.CODE_NODE;
import generictree.impl.CodeNodeTree;
import langdef.LangConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;
import utilfile.UtilFileJson;

import static codedef.modifier.MODIFIER.CODE_NODE_TYPE;
import static codedef.modifier.MODIFIER.NAME;

public class JsonToCodeTree {

    private final CodeNodeTree codeTree;

    public JsonToCodeTree() {
        codeTree = new CodeNodeTree(LangConstants.PATH_TREE_SEP);
    }

    public JsonToCodeTree buildTree(String filePath){
        JSONObject jsonObj = new UtilFileJson().getJsonObject(filePath);
        buildTree(jsonObj);
        return this;
    }

    public JsonToCodeTree buildTree(JSONObject jsonObj){
        recurse(jsonObj, 0);
        codeTree.finalizeTree();
        return this;
    }

    private void recurse(JSONObject jsonObj, int level){
        JSONObject jsonAttr = Glob.UTIL_JSON.getJObj(jsonObj, "attributes");
        CODE_NODE codeNodeEnum = Glob.UTIL_JSON.getCodeNodeEnu(jsonAttr, CODE_NODE_TYPE.toString());
        ICodeNode codeNode = Glob.PROTOTYPE_FACTORY.getPrototype(codeNodeEnum);
        codeNode.getAttribModifier().fromJson(jsonAttr);

        String identifier = Glob.UTIL_JSON.getStr(jsonAttr, NAME.toString());
        codeTree.addBranch(codeNode, identifier);
//        System.out.println("\nstart: level=" + level + ": " + identifier);
//        System.out.println(" path:       " + codeTree.pathAsString());

        JSONArray children = Glob.UTIL_JSON.getJArr(jsonObj, "children");
        for(Object childObj : children){
            recurse((JSONObject)childObj, level + 1);
        }

//        System.out.println("done:  level=" + level + ": " + identifier);
//        System.out.println(" path:       " + codeTree.pathAsString());
//        System.out.println();

        codeTree.pathBack();
    }

    public CodeNodeTree getTree(){
        return codeTree;
    }
}
