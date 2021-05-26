package generictree.impl;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;
import generictree.iface.ISteadyPathTree;
import generictree.task.TaskCodeNodeParent;
import generictree.task.TaskDisp;
import generictree.task.TaskToList;
import langdef.LangConstants;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import runstate.Glob;
import codedef.iface.ICodeNode;
import codedef.impl.PrototypeFactory;
import utilfile.JsonObjFile;

import java.util.ArrayList;
import java.util.List;

import static codedef.modifier.CODE_NODE.*;
import static codedef.modifier.MODIFIER.*;

class SteadyPathTreeTest {
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
        pathTree.put("a", "a");
        //pathTree.setRestore();
        pathTree.put("b", "b");
        pathTree.put("c", "c");
        //pathTree.pathBack(2);
        //pathTree.pathBack("a");
        pathTree.pathSetLast(0);
        //pathTree.restorePath();;
        pathTree.put("d", "d");
        pathTree.put("e", "e");

        ArrayList<IGTreeNode<String>> list = new ArrayList<>();
        IGTreeTask<String> d = new TaskToList<>(list);
        IGTreeParse<String> parser = pathTree.getParse();
        parser.breadthFirst(pathTree.getRoot(), d);
        assertEqNodes(list, "0,0,2,-,-,a,a", "1,0,1,-,-,b,b", "1,0,1,-,-,d,d", "2,1,0,-,-,c,c", "2,1,0,-,-,e,e");
    }

    CodeNodeTree getPopulatedTree() {
        PrototypeFactory p = Glob.PROTOTYPE_FACTORY;
        CodeNodeTree pathTree = new CodeNodeTree(LangConstants.PATH_TREE_SEP);
        {
            ICodeNode glob = p.getPrototype(GLOB);
            pathTree.put(glob, "glob");
        }
        {
            ICodeNode package1 = p.getPrototype(PACKAGE);
            package1.getAttribModifier().put(NAME, "package1");
            pathTree.put(package1, "package1");
        }
        {
            ICodeNode file = p.getPrototype(FILE);
            file.getAttribModifier().put(NAME, "file1");
            pathTree.put(file, "file1");
        }
        {
            ICodeNode class1 = p.getPrototype(CLASS);
            class1.getAttribModifier().put(NAME, "class1");
            pathTree.put(class1, "class1");
        }
        pathTree.pathBack(2);
        {
            ICodeNode file = p.getPrototype(FILE);
            file.getAttribModifier().put(NAME, "file2");
            pathTree.put(file, "file2");
        }
        {
            ICodeNode class1 = p.getPrototype(CLASS);
            class1.getAttribModifier().put(NAME, "class2");
            pathTree.put(class1, "class2");
        }
        return pathTree;
    }
    CodeNodeTree getPopulatedTree2() {
        PrototypeFactory p = Glob.PROTOTYPE_FACTORY;
        CodeNodeTree pathTree = new CodeNodeTree(LangConstants.PATH_TREE_SEP);
        {
            pathTree.put(p.getPrototype(GLOB), "glob");
        }
        {
            ICodeNode package1 = p.getPrototype(PACKAGE);
            package1.getAttribModifier().put(NAME, "package1");
            pathTree.put(package1, "package1");
        }
        {
            ICodeNode file = p.getPrototype(FILE);
            file.getAttribModifier().put(NAME, "file1");
            pathTree.put(file, "file1");
        }
        {
            ICodeNode class1 = p.getPrototype(CLASS);
            class1.getAttribModifier().put(NAME, "class1");
            pathTree.put(class1, "class1");
        }
        pathTree.pathBack(2);
        {
            ICodeNode file = p.getPrototype(FILE);
            file.getAttribModifier().put(NAME, "file2");
            pathTree.put(file, "file2");
        }
        {
            ICodeNode class1 = p.getPrototype(CLASS);
            class1.getAttribModifier().put(NAME, "class2");
            pathTree.put(class1, "class2");
        }
        pathTree.pathBack(3);
        {
            ICodeNode package1 = p.getPrototype(PACKAGE);
            package1.getAttribModifier().put(NAME, "package2");
            pathTree.put(package1, "package2");
        }
        {
            ICodeNode file = p.getPrototype(FILE);
            file.getAttribModifier().put(NAME, "file3");
            pathTree.put(file, "file3");
        }
        {
            ICodeNode class1 = p.getPrototype(CLASS);
            class1.getAttribModifier().put(NAME, "class3");
            pathTree.put(class1, "class3");
        }
        pathTree.pathBack(2);
        {
            ICodeNode file = p.getPrototype(FILE);
            file.getAttribModifier().put(NAME, "file4");
            pathTree.put(file, "file4");
        }
        {
            ICodeNode class1 = p.getPrototype(CLASS);
            class1.getAttribModifier().put(NAME, "class4");
            pathTree.put(class1, "class4");
        }

//        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');
//        System.out.println("\npaths");
//        System.out.println(String.join("\n", allPaths));

        return pathTree;
    }
    CodeNodeTree getPopulatedTree3() {
        PrototypeFactory p = Glob.PROTOTYPE_FACTORY;
        CodeNodeTree pathTree = new CodeNodeTree(LangConstants.PATH_TREE_SEP);
        {
            pathTree.put(p.getPrototype(GLOB), "glob");
        }
        {
            ICodeNode package1 = p.getPrototype(PACKAGE);
            package1.getAttribModifier().put(NAME, "package1");
            pathTree.put(package1, "package1");
        }
        {
            ICodeNode file = p.getPrototype(FILE);
            file.getAttribModifier().put(NAME, "file1");
            pathTree.put(file, "file1");
        }
        {
            ICodeNode class1 = p.getPrototype(CLASS);
            class1.getAttribModifier().put(NAME, "class1");
            pathTree.put(class1, "class1");
        }
        {
            ICodeNode class1 = p.getPrototype(METHOD);
            class1.getAttribModifier().put(NAME, "method1");
            pathTree.put(class1, "method1");
        }
        {
            ICodeNode class1 = p.getPrototype(METHOD_ARGS);
            pathTree.put(class1, "METHOD_ARGS");
        }
        {
            ICodeNode class1 = p.getPrototype(METHOD_ARG);
            class1.getAttribModifier().put(NAME, "arg1");
            class1.getAttribModifier().put(DATA_TYPE, "INT");
            class1.getAttribModifier().put(VAR_VALUE, "47");
            pathTree.put(class1, "arg1");
        }
        pathTree.pathBack();
        {
            ICodeNode class1 = p.getPrototype(METHOD_ARG);
            class1.getAttribModifier().put(NAME, "arg2");
            class1.getAttribModifier().put(DATA_TYPE, "STRING");
            class1.getAttribModifier().put(VAR_VALUE, "Happy Boy");
            pathTree.put(class1, "arg2");
        }
        pathTree.pathSetLast(0);
        {
            ICodeNode package1 = p.getPrototype(PACKAGE);
            package1.getAttribModifier().put(NAME, "package2");
            pathTree.put(package1, "package2");
        }
//        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');
//        System.out.println("\npaths");
//        System.out.println(String.join("\n", allPaths));

        return pathTree;
    }

    @Test
    void givenCodeNodeTree_testPaths() {
        CodeNodeTree pathTree = getPopulatedTree2();
        IGTreeParse<ICodeNode> parser = pathTree.getParse();
        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');

        System.out.println("\npaths");
        System.out.println(String.join("\n", allPaths));
        assertEqStrings(allPaths, "glob-package1-file1-class1", "glob-package1-file2-class2", "glob-package2-file3-class3", "glob-package2-file4-class4");
    }
    @Test
    void givenUnevenTree_testPaths() {
        CodeNodeTree pathTree = getPopulatedTree3();
        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');

        System.out.println("\npaths");
        System.out.println(String.join("\n", allPaths));
        assertEqStrings(allPaths, "glob-package1-file1-class1-method1", "glob-package1-file1-class1-METHOD_BODY", "glob-package1-file1-class1-METHOD_ARGS-arg1", "glob-package1-file1-class1-METHOD_ARGS-arg2", "glob-package2");
    }

    @Test
    void givenPopulatedTree_setParent() {
        CodeNodeTree pathTree = getPopulatedTree();
        IGTreeParse<ICodeNode> parser = pathTree.getParse();

        IGTreeTask<ICodeNode> parentLinker = new TaskCodeNodeParent();
        parser.preOrder(pathTree.getRoot(), parentLinker);
    }

    @Test
    void givenPopulatedTree_validate() {
        CodeNodeTree pathTree = getPopulatedTree();
        IGTreeParse<ICodeNode> parser = pathTree.getParse();
        IGTreeTask<ICodeNode> disp = new TaskDisp<>();
        parser.preOrder(pathTree.getRoot(), disp);

//        IGTreeTask<ICodeNode> validator = new TaskValidCodeNode();
//        parser.preOrder(pathTree.getRoot(), validator);
    }

    @Test
    void givenPopulatedTree_getJson() {
        CodeNodeTree pathTree = getPopulatedTree2();
        IGTreeParse<ICodeNode> parser = pathTree.getParse();

        IGTreeTask<ICodeNode> parentLinker = new TaskCodeNodeParent();
        parser.preOrder(pathTree.getRoot(), parentLinker);
        JSONObject jsonRoot = pathTree.getRoot().getPayload().toJson();
        System.out.println("=====================done=====================");
        //System.out.println(jsonRoot.toString(2));

        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test5.json");
        System.out.println("filePath: " + filePath);
        new JsonObjFile().put(jsonRoot, filePath);
    }

    @Test
    void givenUnevenTree_getJson2() {
        CodeNodeTree pathTree = getPopulatedTree3();
        IGTreeParse<ICodeNode> parser = pathTree.getParse();

        IGTreeTask<ICodeNode> parentLinker = new TaskCodeNodeParent();
        parser.preOrder(pathTree.getRoot(), parentLinker);
        JSONObject jsonRoot = pathTree.getRoot().getPayload().toJson();
        System.out.println("=====================done=====================");
        //System.out.println(jsonRoot.toString(2));

        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test6.json");
        System.out.println("filePath: " + filePath);
        new JsonObjFile().put(jsonRoot, filePath);
    }
}