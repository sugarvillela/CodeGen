package codejson.impl;

import codedef.iface.ICodeNode;
import codedef.impl.PrototypeFactory;
import codedef.enums.CODE_NODE;
import codejson.JsonErrHandler;
import codejson.iface.IJCodeBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;
import utilfile.UtilFileJson;

import static codedef.enums.MODIFIER.TYPE_;

public class JCodeBuilder implements IJCodeBuilder {
    private final JsonErrHandler jsonErrHandler;
    private ICodeNode root;

    public JCodeBuilder() {
        jsonErrHandler = Glob.JSON_ERR_HANDLER;
    }

    @Override
    public IJCodeBuilder build(String filePath) {
        return this.build(UtilFileJson.initInstance().getJsonObject(filePath));
    }

    @Override
    public IJCodeBuilder build(JSONObject jRoot) {
        root = recurse(jRoot);
        return this;
    }

    @Override
    public ICodeNode getRoot() {
        return root;
    }

    private ICodeNode recurse(JSONObject jObject){
        PrototypeFactory f = Glob.PROTOTYPE_FACTORY;

        // get ICodeNode prototype from type
        JSONObject attrObj = jsonErrHandler.toJObj(jObject, "attributes");
        String attribKey = TYPE_.toString();
        CODE_NODE codeNodeEnum = jsonErrHandler.toCodeNodeEnum(
                jsonErrHandler.toStr(attrObj, attribKey)
        );
        ICodeNode codeNode = f.get(codeNodeEnum);

        // populate attributes
        codeNode.getAttribModifier().importJson(attrObj);

        // populate children
        JSONArray children = jsonErrHandler.toJArr(jObject, "children");
        ICodeNode[] childrenToAdd = new ICodeNode[children.length()];
        int k = 0;
        for(Object childObject : children){
            JSONObject jChildObject = jsonErrHandler.toJObj(childObject);
            childrenToAdd[k++] = this.recurse(jChildObject);
        }
        return codeNode.setChildren(childrenToAdd);
    }
}
