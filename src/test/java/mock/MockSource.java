package mock;

import codedef.codenode.CodeNode;
import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;
import codedef.impl.AttribModifier;
import codedef.impl.AttribStruct;
import codedef.impl.PrototypeFactory;
import generictree.impl.CodeNodeTree;
import runstate.Glob;

import static codedef.modifier.CODE_NODE.*;
import static codedef.modifier.ENU_OUT_LANG.JAVA;
import static codedef.modifier.ENU_VISIBILITY.PUBLIC;
import static codedef.modifier.MODIFIER.*;

public class MockSource {
    public CodeNodeTree getPopulatedTreeFromFile(String fileName){
//        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath(fileName);
//        System.out.println("filePath: " + filePath);
//        pathTree = new JsonToCodeTree().buildTree(filePath).getTree();
//
//        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');
//        System.out.println(String.join("\n", allPaths));
//        return pathTree;
        return null;
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
    public ICodeNode mockCodeNodeTree() {
        PrototypeFactory f = Glob.PROTOTYPE_FACTORY;
        ICodeNode root = f.get(GLOB).putAttrib(OUT_LANG, JAVA.toString()).putAttrib(FORMAT_INDENT, "4").setChildren(
            f.get(PACKAGE).putAttrib(NAME, "package1").setChildren(
                f.get(FILE).putAttrib(NAME, "class1").putAttrib(PATH, "package1").setChildren(
                    f.get(IMPORT).setChildren(
                            f.get(IMPORT_ITEM).putAttrib(LIT_VAL, "someDir1.someFile1"),
                            f.get(IMPORT_ITEM).putAttrib(LIT_VAL, "someDir2.someFile2")
                        ),
                        f.get(CLASS).putAttrib(NAME, "class1").setChildren(
                            f.get(COMMENT_LONG).putAttrib(LIT_VAL, "This here is a long string comment, which will be split across two or more lines, assuming its length exceeds the the allowed characters in a comment line which is somewhat less than a regular line"),
                            f.get(CLASS_FIELD).putAttrib(NAME, "classField1").putAttrib(DATA_TYPE, "STRING").putAttrib(VAR_VALUE, "\"comprende\""),
                            f.get(METHOD).putAttrib(NAME, "method1").setChildren(
                                f.get(METHOD_ARGS).setChildren(
                                    f.get(METHOD_ARG).putAttrib(NAME, "arg1").putAttrib(DATA_TYPE, "INT"),
                                    f.get(METHOD_ARG).putAttrib(NAME, "arg2").putAttrib(DATA_TYPE, "STRING"),
                                    f.get(METHOD_ARG).putAttrib(NAME, "arg3").putAttrib(DATA_TYPE, "BOOLEAN")
                                ),
                                f.get(CODE_BLOCK).setChildren(
                                    f.get(COMMENT).putAttrib(LIT_VAL, "This here is a short string comment, which will also be split across two or more lines, assuming its length exceeds the the allowed characters in a comment line"),
                                    f.get(SWITCH).putAttrib(LIT_VAL, "arg3").setChildren(
                                        f.get(SWITCH_CASE).putAttrib(LIT_VAL, "true").setChildren(
                                            f.get(STATEMENT).setChildren(
                                                    f.get(LIT).putAttrib(LIT_VAL, "arg3"),
                                                    f.get(ASSIGN),
                                                    f.get(LIT).putAttrib(LIT_VAL, "false")
                                            ),
                                            f.get(BREAK)
                                        ),
                                        f.get(SWITCH_CASE).putAttrib(LIT_VAL, "null"),
                                        f.get(SWITCH_CASE).putAttrib(LIT_VAL, "false").setChildren(
                                            f.get(STATEMENT).setChildren(
                                                    f.get(LIT).putAttrib(LIT_VAL, "arg3"),
                                                    f.get(ASSIGN),
                                                    f.get(LIT).putAttrib(LIT_VAL, "true")
                                            ),
                                            f.get(BREAK)
                                        ),
                                        f.get(SWITCH_DEFAULT).setChildren(
                                            f.get(STATEMENT).setChildren(
                                                    f.get(LIT).putAttrib(LIT_VAL, "arg3"),
                                                    f.get(ASSIGN),
                                                    f.get(LIT).putAttrib(LIT_VAL, "true")
                                            ),
                                            f.get(BREAK)
                                        )
                                    ),
                                    f.get(IF_ELSE).setChildren(
                                        f.get(CONDITIONAL).setChildren(
                                            f.get(BOOL_BLOCK).setChildren(
                                                f.get(LIT).putAttrib(LIT_VAL, "arg1"),
                                                f.get(COMPARISON).putAttrib(COMPARISON_TYPE, "LTE"),
                                                f.get(LIT).putAttrib(LIT_VAL, "48")
                                            ),
                                            f.get(CONJUNCTION).putAttrib(CONJUNCTION_TYPE, "OR"),
                                            f.get(BOOL_BLOCK).putAttrib(NEGATE, "TRUE").setChildren(
                                                f.get(LIT).putAttrib(LIT_VAL, "arg2"),
                                                f.get(COMPARISON).putAttrib(COMPARISON_TYPE, "EQ"),
                                                f.get(LIT).putAttrib(LIT_VAL, "17")
                                            )
                                        ),
                                        f.get(CODE_BLOCK).setChildren(
                                            f.get(STATEMENT).setChildren(
                                                f.get(LIT).putAttrib(LIT_VAL, "classField1"),
                                                f.get(ASSIGN),
                                                f.get(LIT).putAttrib(LIT_VAL, "\"no comprende\"")
                                            )
                                        ),
                                        f.get(ELSE).setChildren(
                                            f.get(CODE_BLOCK).setChildren(
                                                f.get(STATEMENT).setChildren(
                                                    f.get(LIT).putAttrib(LIT_VAL, "arg1"),
                                                    f.get(ASSIGN),
                                                    f.get(LIT).putAttrib(LIT_VAL, "69")
                                                )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
        //root.display(0);
        return root;
    }
}
