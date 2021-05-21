package generictree.task;

import codedef.iface.IAttribModifier;
import codedef.iface.ICodeNode;
import codedef.modifier.CODE_NODE;
import codedef.modifier.MODIFIER;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static codedef.modifier.MODIFIER.NAME;

/** does it with a strategy pattern, but unnecessarily  complex. Simple recursion works */
public class TaskCodeNodeToJSON implements IGTreeTask<ICodeNode> {
    private final JSONObject jsonRoot;

    private final HashMap<IGTreeNode<ICodeNode>, JSONObject> map;

    public TaskCodeNodeToJSON(JSONObject jsonRoot, IGTreeNode<ICodeNode> treeRoot) {
        map = new HashMap<>();
        this.jsonRoot = jsonRoot;
        this.init(treeRoot);
    }
    private void init(IGTreeNode<ICodeNode> treeRoot){
        JSONObject globJson = treeRoot.getPayload().toJson();
        this.jsonRoot.put("glob", globJson);
        map.put(treeRoot, globJson);
    }

    private JSONObject addIfNew(IGTreeNode<ICodeNode> parentNode){
        if(map.containsKey(parentNode)){
            return map.get(parentNode);
        }
        else{
            JSONObject parentJson = parentNode.getPayload().toJson();
            if(!parentJson.has("children")){
                System.out.println("oops!");
            }
            map.put(parentNode, parentJson);
            return parentJson;
        }
    }
    @Override
    public boolean doTask(IGTreeNode<ICodeNode> treeNode) {
        System.out.println();
        System.out.println(treeNode.csvString());
        if(treeNode.level() == 0){
            System.out.println("null parent");
        }
        else{
            JSONObject parentJson = addIfNew(treeNode.parent());
            JSONObject childJson = addIfNew(treeNode);

            String[] modifier = treeNode.getPayload().getAttribModifier().get(NAME);
            String name = (modifier == null)? "no name" : modifier[0];
            parentJson.getJSONArray("children").put(childJson);
            //displayMap();
        }

        return false;
    }
    private void displayMap(){
        System.out.println("==display map==");
        Iterator<Map.Entry<IGTreeNode<ICodeNode>, JSONObject>> itr = map.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<IGTreeNode<ICodeNode>, JSONObject> entry = itr.next();
            IGTreeNode<ICodeNode> key = entry.getKey();
            //System.out.println(key);
            JSONObject value = entry.getValue();
            System.out.println(value);
        }
        System.out.println("=====");
    }
}
