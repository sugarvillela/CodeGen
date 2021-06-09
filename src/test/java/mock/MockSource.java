package mock;

import codedef.codenode.CodeNode;
import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;
import codedef.impl.AttribModifier;
import codedef.impl.AttribStruct;
import codedef.impl.PrototypeFactory;
import codedef.modifier.CODE_NODE;
import codedef.modifier.MODIFIER;
import codejson.JsonToCodeTree;
import generictree.iface.IGTreeNode;
import generictree.impl.CodeNodeTree;
import langdef.LangConstants;
import runstate.Glob;

import java.util.List;
import java.util.Locale;

import static codedef.modifier.CODE_NODE.*;
import static codedef.modifier.ENU_VISIBILITY.PUBLIC;
import static codedef.modifier.MODIFIER.*;

public class MockSource {
    PrototypeFactory p = Glob.PROTOTYPE_FACTORY;
    CodeNodeTree pathTree;
    int uq;

    public CodeNodeTree simpleMockTree(){
        CODE_NODE[] nodes = {GLOB, PACKAGE, FILE, CLASS, METHOD, METHOD_ARGS, METHOD_ARG};
        pathTree = new CodeNodeTree(LangConstants.PATH_TREE_SEP);

        for(int i = 0; i < nodes.length; i++){
            ICodeNode nuNode = p.getPrototype(nodes[i]);
            String name = nodes[i].toString().toLowerCase(Locale.ROOT) + "_" + i;
            nuNode.getAttribModifier().put(NAME, name);

            if((i + 1) % 3 == 0){
                System.out.println("setRestore before adding: " + name);
                pathTree.A_();
            }

            pathTree.addBranch(nuNode, name);
        }
        return pathTree;
    }

    public void addBranch(CODE_NODE codeNodeEnum){
        addBranch(codeNodeEnum, null);
    }
    public void addBranch(CODE_NODE codeNodeEnum, String name){
        ICodeNode component = p.getPrototype(codeNodeEnum);
        if(name == null){
            name = codeNodeEnum.name() + (uq++);
        }
        else{
            component.getAttribModifier().put(NAME, name);
        }
        pathTree.addBranch(component, name);
    }
    public void addLeaf(CODE_NODE codeNodeEnum){
        addLeaf(codeNodeEnum, null);
    }
    public void addLeaf(CODE_NODE codeNodeEnum, String name){
        ICodeNode component = p.getPrototype(codeNodeEnum);
        if(name == null){
            name = codeNodeEnum.name() + (uq++);
        }
        else{
            component.getAttribModifier().put(NAME, name);
        }
        pathTree.addLeaf(component, name);
    }
    public void modify(MODIFIER modifier, String... values){
        IGTreeNode<ICodeNode> treeNode;
        if((treeNode = pathTree.getLastAdded()) != null){
            treeNode.getPayload().getAttribModifier().put(modifier, values);
        }
    }
    public CodeNodeTree getPopulatedTree() {
        pathTree = new CodeNodeTree(LangConstants.PATH_TREE_SEP);
        addBranch(GLOB);
            addBranch(PACKAGE,"package1");
            pathTree.A_();
                addBranch(FILE, "file1");
                    addBranch(CLASS, "class1");
                pathTree.V_();
                pathTree.A_();
                addBranch(FILE, "file2");
                    addBranch(CLASS, "class2");
            pathTree.V_();
        pathTree.finalizeTree();
        return pathTree;
    }
    public CodeNodeTree getPopulatedTree2() {
        pathTree = new CodeNodeTree(LangConstants.PATH_TREE_SEP);
        addBranch(GLOB);
        pathTree.A_();//+ GLOB
            addBranch(PACKAGE, "package1");
            pathTree.A_();//+ PACKAGE
                addBranch(FILE, "file1");
                    addBranch(CLASS, "class1");
            pathTree.V_();//-PACKAGE
            pathTree.A_();//+ PACKAGE
                addBranch(FILE, "file2");
                    addBranch(CLASS, "class2");
            pathTree.V_();//-PACKAGE
        pathTree.V_();//-GLOB
        pathTree.A_();//+GLOB
            addBranch(PACKAGE, "package2");
            pathTree.A_();//+ PACKAGE
                addBranch(FILE, "file3");
                    addBranch(CLASS, "class3");
            pathTree.V_();//-PACKAGE
            pathTree.A_();//+ PACKAGE
                addBranch(FILE, "file4");
                    addBranch(CLASS, "class4");
            pathTree.V_();//-PACKAGE
        pathTree.V_();//-GLOB
        pathTree.finalizeTree();
        return pathTree;
    }

    public CodeNodeTree getPopulatedTree3() {
        pathTree = new CodeNodeTree(LangConstants.PATH_TREE_SEP);
        addBranch(GLOB);
        {
            addBranch(PACKAGE,"package1");
            {
                addBranch(FILE, "file1");
                {
                    addBranch(IMPORT, "import1");
                    {
                        addLeaf(IMPORT_ITEM, "importDir.fileX");
                        addLeaf(IMPORT_ITEM, "importDir.fileY");
                    }
                    pathTree.pathBack();

                    addBranch(CLASS, "class1");
                    {
//                        addLeaf(COMMENT_LONG);
//                        modify(LIT_VAL, "Long string comment here, which will be split across two or more lines, assuming it is over 79 characters long");

                        addLeaf(CLASS_FIELD, "classField1");
                        modify(DATA_TYPE, "STRING");
                        modify(VAR_VALUE, "\"comprende\"");

                        addBranch(METHOD, "method1");
                        {

                            addBranch(METHOD_ARGS);
                            {
                                addLeaf(METHOD_ARG, "arg1");
                                modify(DATA_TYPE, "INT");

                                addLeaf(METHOD_ARG, "arg2");
                                modify(DATA_TYPE, "STRING");

                                addLeaf(METHOD_ARG, "arg3");
                                modify(DATA_TYPE, "BOOLEAN");
                            }
                            pathTree.pathBack();
                            addBranch(CODE_BLOCK);
                            {
//                                addLeaf(COMMENT);
//                                modify(LIT_VAL, "Short string comment here, which will also be split across two or more lines, assuming it is over 79 characters long");

                                addBranch(IF_ELSE);
                                {
                                    addBranch(CONDITIONAL);
                                    {
                                        addBranch(BOOL_BLOCK);
                                        {
                                            addLeaf(LIT);
                                            modify(LIT_VAL, "arg1");

                                            addLeaf(COMPARISON);
                                            modify(COMPARISON_TYPE, "LTE");

                                            addLeaf(LIT);
                                            modify(LIT_VAL, "48");
                                        }
                                        pathTree.pathBack();

                                        addLeaf(CONJUNCTION);
                                        modify(CONJUNCTION_TYPE, "OR");

                                        addBranch(BOOL_BLOCK);
                                        modify(NEGATE, "TRUE");
                                        {
                                            addLeaf(LIT);
                                            modify(LIT_VAL, "arg2");

                                            addLeaf(COMPARISON);
                                            modify(COMPARISON_TYPE, "EQ");

                                            addLeaf(LIT);
                                            modify(LIT_VAL, "17");
                                        }
                                        pathTree.pathBack();
                                    }
                                    pathTree.pathBack();

                                    addBranch(CODE_BLOCK);
                                    {
                                        addBranch(STATEMENT);
                                        {
                                            addLeaf(LIT);
                                            modify(LIT_VAL, "classField1");

                                            addLeaf(ASSIGN);

                                            addLeaf(LIT);
                                            modify(LIT_VAL, "\"no comprende\"");
                                        }
                                        pathTree.pathBack();
                                    }
                                    pathTree.pathBack();
                                    addBranch(ELSE);
                                    {
                                        addBranch(CODE_BLOCK);
                                        {
                                            addBranch(STATEMENT);
                                            {
                                                addLeaf(LIT);
                                                modify(LIT_VAL, "arg1");

                                                addLeaf(ASSIGN);

                                                addLeaf(LIT);
                                                modify(LIT_VAL, "69");
                                            }
                                            pathTree.pathBack();

                                            addBranch(SWITCH);
                                            modify(LIT_VAL, "classField1");
                                            {
                                                addBranch(SWITCH_CASE);
                                                modify(LIT_VAL, "\"comprende\"");
                                                {
                                                    addBranch(CODE_BLOCK);
                                                    {
                                                        addBranch(STATEMENT);
                                                        {
                                                            addLeaf(RETURN);
                                                        }
                                                        pathTree.pathBack();
                                                    }
                                                    pathTree.pathBack();
                                                }
                                                pathTree.pathBack();

                                                addBranch(SWITCH_CASE);
                                                modify(LIT_VAL, "\"no comprende\"");
                                                {
                                                    addBranch(CODE_BLOCK);
                                                    {
                                                         addBranch(STATEMENT);
                                                        {
                                                            addLeaf(LIT);
                                                            modify(LIT_VAL, "arg1");

                                                            addLeaf(ASSIGN);

                                                            addLeaf(LIT);
                                                            modify(LIT_VAL, "-1");

                                                            addLeaf(BREAK);
                                                        }
                                                        pathTree.pathBack();
                                                    }
                                                    pathTree.pathBack();
                                                }
                                                pathTree.pathBack();

                                                addBranch(SWITCH_DEFAULT);
                                                {
                                                    addBranch(CODE_BLOCK);
                                                    {
                                                        addBranch(STATEMENT);
                                                        {
                                                            addLeaf(LIT);
                                                            modify(LIT_VAL, "arg1");

                                                            addLeaf(ASSIGN);

                                                            addLeaf(LIT);
                                                            modify(LIT_VAL, "arg2");

                                                            addLeaf(BREAK);
                                                        }
                                                        pathTree.pathBack();
                                                    }
                                                    pathTree.pathBack();
                                                }
                                                pathTree.pathBack();

                                            }
                                            pathTree.pathBack();
                                        }
                                        pathTree.pathBack();
                                    }
                                    pathTree.pathBack();
                                }
                                pathTree.pathBack();
                            }
                            pathTree.pathBack();
                        }
                        pathTree.pathBack();
                    }
                    pathTree.pathBack();
                }
                pathTree.pathBack();
            }
            pathTree.pathBack();
            addBranch(PACKAGE, "package2");
            pathTree.pathBack();
        }
        pathTree.finalizeTree();
        return pathTree;
    }

    public CodeNodeTree getSmallTree() {
        pathTree = new CodeNodeTree(LangConstants.PATH_TREE_SEP);
        addBranch(METHOD, "method1");
        {
            addBranch(METHOD_ARGS);
            {
                addLeaf(METHOD_ARG, "arg1");
                modify(DATA_TYPE, "INT");
                modify(VAR_VALUE, "47");

                addLeaf(METHOD_ARG, "arg2");
                modify(DATA_TYPE, "STRING");
                modify(VAR_VALUE, "Happy Boy");
            }
            pathTree.pathBack();
            addBranch(CODE_BLOCK);
            {

            }
            pathTree.pathBack();
        }
        pathTree.finalizeTree();
        return pathTree;
    }
    public CodeNodeTree getPopulatedTreeFromFile(String fileName){
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath(fileName);
        System.out.println("filePath: " + filePath);
        pathTree = new JsonToCodeTree().buildTree(filePath).getTree();

        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');
        System.out.println(String.join("\n", allPaths));
        return pathTree;
    }

    public IAttribModifier mockAttribModifier(){
        IAttribModifier attribModifier = new AttribModifier(CLASS);
        attribModifier.initRequired(NAME);
        attribModifier.initAllowed(VISIBILITY, ABSTRACT, STATIC, FINAL, IMPLEMENTS, EXTENDS);
        attribModifier.put(VISIBILITY, PUBLIC.toString());
        attribModifier.put(ABSTRACT, "FALSE");
        attribModifier.put(STATIC, "FALSE");
        attribModifier.put(FINAL, "FALSE");
        attribModifier.put(IMPLEMENTS);
        attribModifier.put(EXTENDS);
        return attribModifier;
    }
    public IAttribStruct mockAttribStruct(){
        IAttribStruct attribStruct = new AttribStruct(CLASS);
        attribStruct.initRequired(IMPORT);
        attribStruct.initAllowed(CLASS_FIELD, METHOD);
        return attribStruct;
    }
    public ICodeNode mockCodeNode(){
        return new CodeNode(CLASS, null, mockAttribModifier(), null, mockAttribStruct());
    }
}
