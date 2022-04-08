package codejson.iface;

import codedef.iface.ICodeNode;
import org.json.JSONObject;

public interface IJCodeBuilder {
    /**Reads a Json file and builds a CodeNode tree
     * @param filePath Path to Json file to be read
     * @return  self for builder pattern */
    IJCodeBuilder build(String filePath);

    /**Parses a Json object and builds a CodeNode tree
     * @param jRoot Json object to parse
     * @return self for builder pattern */
    IJCodeBuilder build(JSONObject jRoot);

    /** @return CodeNode tree root built by build() */
    ICodeNode getRoot();
}
