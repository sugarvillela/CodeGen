package generictree.impl;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;
import generictree.iface.ISteadyPathTree;
import generictree.task.TaskCodeNodeParent;
import generictree.task.TaskToList;
import mock.MockSource;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import runstate.Glob;
import codedef.iface.ICodeNode;
import utilfile.UtilFileJson;

import java.util.ArrayList;
import java.util.List;

import static codedef.modifier.CODE_NODE.*;


class SteadyPathTreeTest {
    MockSource mockSource = new MockSource();
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
    void testSteadyPathTreeFunctions1(){
        ISteadyPathTree<String> pathTree = new SteadyPathTree('-');
        pathTree.addBranch("a", "a");
        //pathTree.setRestore();
        pathTree.addBranch("b", "b");
        pathTree.addBranch("c", "c");
        //pathTree.pathBack(2);
        //pathTree.pathBack("a");
        pathTree.pathBackTo(0);
        //pathTree.restorePath();;
        pathTree.addBranch("d", "d");
        pathTree.addBranch("e", "e");

        ArrayList<IGTreeNode<String>> list = new ArrayList<>();
        IGTreeTask<String> d = new TaskToList<>(list);
        IGTreeParse<String> parser = pathTree.getParse();
        parser.breadthFirst(pathTree.getRoot(), d);
        assertEqNodes(list, "0,0,2,-,-,a,a", "1,0,1,-,-,b,b", "1,0,1,-,-,d,d", "2,1,0,-,-,c,c", "2,1,0,-,-,e,e");
    }
    
    @Test
    void givenGeneratedTree_findByPath(){
        // points are already set at 1, 4
        CodeNodeTree pathTree = mockSource.simpleMockTree();
        String actual, expected;
        String[] path;
        IGTreeNode<ICodeNode> foundNode = null, root = pathTree.getRoot();

        path = new String[]{"glob_0", "package_1", "file_2", "class_3", "method_4", "method_args_5", "method_arg_6"};
        expected = "method_arg_6";
        actual = pathTree.getParse().treeNodeFromPath(root, path).identifier();
        Assertions.assertEquals(expected, actual);

        path = new String[]{"glob_0", "package_1", "file_2", "class_3"};
        expected = "class_3";
        actual = pathTree.getParse().treeNodeFromPath(root, path).identifier();
        Assertions.assertEquals(expected, actual);

    }
    @Test
    void givenGeneratedTree_restorePathByEnum(){
        CodeNodeTree pathTree = mockSource.simpleMockTree();
        String actual, expected;

        // initial state from mockTreeForPathBackTests()
        expected = "glob_0.package_1.file_2.class_3.method_4.method_args_5.method_arg_6";
        actual = pathTree.pathAsString();
        Assertions.assertEquals(expected, actual);

        pathTree.pathBack(PACKAGE);
        expected = "glob_0.package_1";
        actual = pathTree.pathAsString();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenGeneratedTree_restorePathByIndex(){
        CodeNodeTree pathTree = mockSource.simpleMockTree();
        String actual, expected;

        // initial state from mockTreeForPathBackTests()
        expected = "glob_0.package_1.file_2.class_3.method_4.method_args_5.method_arg_6";
        actual = pathTree.pathAsString();
        Assertions.assertEquals(expected, actual);

        pathTree.pathBackTo(1);
        expected = "glob_0.package_1";
        actual = pathTree.pathAsString();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenGeneratedTree_restorePathByNegativeIndex(){
        CodeNodeTree pathTree = mockSource.simpleMockTree();
        String actual, expected;

        // initial state from mockTreeForPathBackTests()
        expected = "glob_0.package_1.file_2.class_3.method_4.method_args_5.method_arg_6";
        actual = pathTree.pathAsString();
        Assertions.assertEquals(expected, actual);

        pathTree.pathBack();
        expected = "glob_0.package_1.file_2.class_3.method_4.method_args_5";
        actual = pathTree.pathAsString();
        Assertions.assertEquals(expected, actual);

        pathTree.pathBack(4);
        expected = "glob_0.package_1";
        actual = pathTree.pathAsString();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenGeneratedTree_restorePathByIdentifier(){
        CodeNodeTree pathTree = mockSource.simpleMockTree();
        String actual, expected;

        // initial state from mockTreeForPathBackTests()
        expected = "glob_0.package_1.file_2.class_3.method_4.method_args_5.method_arg_6";
        actual = pathTree.pathAsString();
        Assertions.assertEquals(expected, actual);

        pathTree.pathBack("package_1");
        expected = "glob_0.package_1";
        actual = pathTree.pathAsString();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenGeneratedTree_restorePathByRestorePoint(){
        // points are already set at 1, 4
        CodeNodeTree pathTree = mockSource.simpleMockTree();
        String actual, expected;

        // initial state from mockTreeForPathBackTests()
        expected = "glob_0.package_1.file_2.class_3.method_4.method_args_5.method_arg_6";
        actual = pathTree.pathAsString();
        Assertions.assertEquals(expected, actual);

        pathTree.V_();
        expected = "glob_0.package_1.file_2.class_3.method_4";
        actual = pathTree.pathAsString();
        Assertions.assertEquals(expected, actual);

        pathTree.V_();
        expected = "glob_0.package_1";
        actual = pathTree.pathAsString();
        Assertions.assertEquals(expected, actual);
    }


    @Test
    void givenCodeNodeTree_testPaths() {
        CodeNodeTree pathTree = mockSource.getPopulatedTree2();
        IGTreeParse<ICodeNode> parser = pathTree.getParse();
        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');

//        System.out.println("\ndisplay");
//        pathTree.display();

        System.out.println("\npaths");
        System.out.println(String.join("\n", allPaths));
        assertEqStrings(allPaths, "glob-package1-file1-class1", "glob-package1-file2-class2", "glob-package2-file3-class3", "glob-package2-file4-class4");
    }
    @Test
    void givenUnevenTree_testPaths() {
        CodeNodeTree pathTree = mockSource.getPopulatedTree3();
        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');

        System.out.println("\npaths");
        System.out.println(String.join("\n", allPaths));
        assertEqStrings(
                allPaths,
                "glob-package1-file1-import1-package2.fileX|glob-package1-file1-import1-package2.fileY",
                "glob-package1-file1-class1-method1-METHOD_ARGS-arg1|glob-package1-file1-class1-method1-METHOD_ARGS-arg2",
                "glob-package2"
        );
    }

    @Test
    void givenPopulatedTree_display() {
        CodeNodeTree pathTree = mockSource.getPopulatedTree3();
        pathTree.display();
//        IGTreeParse<ICodeNode> parser = pathTree.getParse();
//        IGTreeTask<ICodeNode> disp = new TaskDisp<>();
//        parser.preOrder(pathTree.getRoot(), disp);

        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');
        System.out.println("\npaths");
        System.out.println(String.join("\n", allPaths));
    }

    @Test
    void givenPopulatedTree2_getJson() {
        CodeNodeTree pathTree = mockSource.getPopulatedTree2();
        IGTreeParse<ICodeNode> parser = pathTree.getParse();

        IGTreeTask<ICodeNode> parentLinker = new TaskCodeNodeParent();
        parser.preOrder(pathTree.getRoot(), parentLinker);
        JSONObject jsonRoot = pathTree.getRoot().getPayload().toJson();
        System.out.println("=====================done=====================");
        //System.out.println(jsonRoot.toString(2));

        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test2.json");
        System.out.println("filePath: " + filePath);
        new UtilFileJson().put(jsonRoot, filePath);
    }

    @Test
    void givenUnevenTree3_getJson() {
        CodeNodeTree pathTree = mockSource.getPopulatedTree3();
        IGTreeParse<ICodeNode> parser = pathTree.getParse();

        IGTreeTask<ICodeNode> parentLinker = new TaskCodeNodeParent();
        parser.preOrder(pathTree.getRoot(), parentLinker);
        JSONObject jsonRoot = pathTree.getRoot().getPayload().toJson();
        System.out.println("=====================done=====================");
        //System.out.println(jsonRoot.toString(2));

        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test3.json");
        System.out.println("filePath: " + filePath);
        new UtilFileJson().put(jsonRoot, filePath);
    }
}