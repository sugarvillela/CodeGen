package codejson.impl;

import codedef.iface.ICodeNode;
import codedef.impl.PrototypeFactory;
import codedef.modifier.CODE_NODE;
import codejson.iface.IErrCatch;
import codejson.iface.IJCodeBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import runstate.Glob;
import utilfile.UtilFileJson;

import java.util.ArrayList;
import java.util.List;

import static codedef.modifier.MODIFIER.CODE_NODE_TYPE;

public class JCodeBuilder implements IJCodeBuilder {
    private final IErrCatch errCatch;
    private ICodeNode root;

    public JCodeBuilder() {
        errCatch = new IErrCatch(){};
    }

    @Override
    public IJCodeBuilder build(String filePath) {
        return this.build(new UtilFileJson().getJsonObject(filePath));
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
        JSONObject attrObj = errCatch.toJObj(jObject, "attributes");
        System.out.println(attrObj);
        String attribKey = CODE_NODE_TYPE.toString();
        CODE_NODE codeNodeEnum = errCatch.toCodeNodeEnum(
                errCatch.toStr(attrObj, attribKey)
        );
        ICodeNode codeNode = f.get(codeNodeEnum);

        // populate attributes
        codeNode.getAttribModifier().fromJson(attrObj, errCatch);

        // populate children
        JSONArray children = errCatch.toJArr(jObject, "children");
        ICodeNode[] childrenToAdd = new ICodeNode[children.length()];
        int k = 0;
        for(Object childObject : children){
            JSONObject jChildObject = errCatch.toJObj(childObject);
            childrenToAdd[k++] = this.recurse(jChildObject);
        }
        return codeNode.setChildren(childrenToAdd);
    }
}
