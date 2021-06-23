package codejson.iface;

import codedef.iface.ICodeNode;
import org.json.JSONObject;

public interface IJCodeBuilder {
    IJCodeBuilder build(String filePath);
    IJCodeBuilder build(JSONObject jRoot);
    ICodeNode getRoot();
}
