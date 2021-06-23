package codedef.codenode;

import codedef.iface.ICodeNode;
import codejson.iface.IJCodeBuilder;
import codejson.impl.JCodeBuilder;
import mock.MockSource;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import runstate.Glob;
import utilfile.UtilFileJson;

class CodeNodeTest {
    MockSource mockSource = new MockSource();
    @Test
    void givenTree_display() {
        ICodeNode root = mockSource.mockCodeNodeTree();
        //root.display(0);
        String actual = root.translator().format(root);
        System.out.println(actual);
    }
    @Test
    void givenTree_write() {
        ICodeNode root = mockSource.mockCodeNodeTree();
        //root.display(0);
        String actual = root.translator().format(root);
        System.out.println(actual);
        JSONObject jsonRoot = root.toJson();
        System.out.println("=====================done=====================");
        //System.out.println(jsonRoot.toString(2));

        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test2.json");
        System.out.println("filePath: " + filePath);
        new UtilFileJson().put(jsonRoot, filePath);
    }
    @Test
    void givenFile_buildTree() {
        String fileName = "test2.json";
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath(fileName);
        IJCodeBuilder builder = new JCodeBuilder().build(filePath);
        ICodeNode root = builder.getRoot();
        root.display(0);
        String actual = root.translator().format(root);
        System.out.println(actual);
    }

//
//        System.out.println("filePath: " + filePath);
//        pathTree = new JsonToCodeTree().buildTree(filePath).getTree();
//
//        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');
//        System.out.println(String.join("\n", allPaths));
//        return pathTree;
}