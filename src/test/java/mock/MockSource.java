package mock;

import codedef.codenode.CodeNode;
import codedef.enums.CODE_NODE;
import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;
import codedef.impl.AttribModifier;
import codedef.impl.AttribStruct;
import codedef.impl.PrototypeFactory;
import runstate.Glob;

import java.util.Arrays;
import java.util.List;

import static codedef.enums.CODE_NODE.*;
import static codedef.enums.ENU_BOOLEAN.TRUE;
import static codedef.enums.ENU_COMPARISON.EQ;
import static codedef.enums.ENU_COMPARISON.LTE;
import static codedef.enums.ENU_CONJUNCTION.AND;
import static codedef.enums.ENU_CONJUNCTION.OR;
import static codedef.enums.ENU_LANGUAGE.JAVA_;
import static codedef.enums.ENU_VISIBILITY.PUBLIC;
import static codedef.enums.MODIFIER.*;
import static codedef.enums.ENU_DATA_TYPE.*;

public class MockSource {
    protected final PrototypeFactory f;

    public MockSource() {
        f = Glob.PROTOTYPE_FACTORY;
    }

    public IAttribModifier mockAttribModifier(){
        IAttribModifier attribModifier = new AttribModifier(CLASS);
        attribModifier.initRequired(NAME_);
        attribModifier.initAllowed(ACCESS_, ABSTRACT_, STATIC_, FINAL_, IMPLEMENTS_, EXTENDS_);
        attribModifier.put(ACCESS_, PUBLIC.toString());
        attribModifier.put(ABSTRACT_, "FALSE");
        attribModifier.put(STATIC_, "FALSE");
        attribModifier.put(FINAL_, "FALSE");
        attribModifier.put(IMPLEMENTS_);
        attribModifier.put(EXTENDS_);
        return attribModifier;
    }
    public IAttribStruct mockAttribStruct(){
        IAttribStruct attribStruct = new AttribStruct(CLASS);
        attribStruct.initRequired(IMPORT);
        attribStruct.initAllowed(STATEMENT, METHOD);
        return attribStruct;
    }
    public ICodeNode mockCodeNode(){
        return new CodeNode(CLASS, null, mockAttribModifier(), null, mockAttribStruct());
    }


    public ICodeNode mockCodeNodeTree(){
        ICodeNode root = f.get(GLOB).putAttrib(LANGUAGE_, JAVA_).putAttrib(INDENT_, 4).setChildren(
             mockPackage1(), mockPackage2()
        );
        return root;
    }
    private ICodeNode mockPackage1(){
        ICodeNode root = f.get(PACKAGE).putAttrib(NAME_, "package1").setChildren(
                        f.get(FILE).putAttrib(NAME_, "class1").setChildren(
                                f.get(IMPORT).setChildren(
                                        f.get(IMPORT_ITEM).putAttrib(LIT_, "someDir1.someFile1"),
                                        f.get(IMPORT_ITEM).putAttrib(LIT_, "someDir2.someFile2")
                                ),
                                f.get(CLASS).putAttrib(NAME_, "class1").setChildren(
                                        f.get(COMMENT_LONG).putAttrib(LIT_, "This here is a long string comment, which will be split across two or more lines, assuming its length exceeds the the allowed characters in a comment line which is somewhat less than a regular line"),
                                        f.get(STATEMENT).setChildren(
                                                f.get(VAR_DEF_SCALAR).putAttrib(NAME_, "classField1").putAttrib(TYPE_DATA_, STRING),
                                                f.get(ASSIGN),
                                                "\"comprende\""
                                        ),
                                        f.get(METHOD).putAttrib(NAME_, "method1").putAttrib(TYPE_RETURN_, VOID).setChildren(
                                                f.get(METHOD_ARGS).setChildren(
                                                        f.get(VAR_DEF_SCALAR).putAttrib(NAME_, "arg1").putAttrib(TYPE_DATA_, INT),
                                                        f.get(VAR_DEF_SCALAR).putAttrib(NAME_, "arg2").putAttrib(TYPE_DATA_, STRING),
                                                        f.get(VAR_DEF_SCALAR).putAttrib(NAME_, "arg3").putAttrib(TYPE_DATA_, BOOLEAN)
                                                ),
                                                f.get(CODE_BLOCK).setChildren(
                                                        f.get(COMMENT).putAttrib(LIT_, "This here is a short string comment, which will also be split across two or more lines, assuming its length exceeds the the allowed characters in a comment line"),
                                                        f.get(SWITCH).putAttrib(LIT_, "arg3").setChildren(
                                                                f.get(SWITCH_CASE).putAttrib(LIT_, "true").setChildren(
                                                                        f.get(STATEMENT).setChildren(
                                                                                f.get(LIT).putAttrib(LIT_, "arg3"),
                                                                                f.get(ASSIGN),
                                                                                f.get(LIT).putAttrib(LIT_, "false")
                                                                        ),
                                                                        f.get(BREAK)
                                                                ),
                                                                f.get(SWITCH_CASE).putAttrib(LIT_, "null"),
                                                                f.get(SWITCH_CASE).putAttrib(LIT_, "false").setChildren(
                                                                        f.get(STATEMENT).setChildren(
                                                                                f.get(LIT).putAttrib(LIT_, "arg3"),
                                                                                f.get(ASSIGN),
                                                                                f.get(LIT).putAttrib(LIT_, "true")
                                                                        ),
                                                                        f.get(BREAK)
                                                                ),
                                                                f.get(SWITCH_DEFAULT).setChildren(
                                                                        f.get(STATEMENT).setChildren(
                                                                                f.get(LIT).putAttrib(LIT_, "arg3"),
                                                                                f.get(ASSIGN),
                                                                                f.get(LIT).putAttrib(LIT_, "true")
                                                                        ),
                                                                        f.get(BREAK)
                                                                )
                                                        ),
                                                        f.get(IF_ELSE).setChildren(
                                                                f.get(CONDITIONAL).setChildren(
                                                                        f.get(BOOL_BLOCK).setChildren(
                                                                                f.get(LIT).putAttrib(LIT_, "arg1"),
                                                                                f.get(COMPARISON).putAttrib(TYPE_COMP_, LTE),
                                                                                f.get(LIT).putAttrib(LIT_, "48")
                                                                        ),
                                                                        f.get(CONJUNCTION).putAttrib(TYPE_CONJ_, OR),
                                                                        f.get(BOOL_BLOCK).putAttrib(IS_NEGATE_, TRUE).setChildren(
                                                                                f.get(LIT).putAttrib(LIT_, "arg2"),
                                                                                f.get(COMPARISON).putAttrib(TYPE_COMP_, EQ),
                                                                                f.get(LIT).putAttrib(LIT_, 17)
                                                                        )
                                                                ),
                                                                f.get(CODE_BLOCK).setChildren(
                                                                        f.get(STATEMENT).setChildren(
                                                                                f.get(LIT).putAttrib(LIT_, "classField1"),
                                                                                f.get(ASSIGN),
                                                                                f.get(LIT).putAttrib(LIT_, "\"no comprende\"")
                                                                        )
                                                                ),
                                                                f.get(ELSE).setChildren(
                                                                        f.get(CODE_BLOCK).setChildren(
                                                                                f.get(STATEMENT).setChildren(
                                                                                        f.get(LIT).putAttrib(LIT_, "arg1"),
                                                                                        f.get(ASSIGN),
                                                                                        f.get(LIT).putAttrib(LIT_, 69)
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
    private ICodeNode mockPackage2(){
        ICodeNode root = f.get(PACKAGE).putAttrib(NAME_, "package2").setChildren(
            f.get(PACKAGE).putAttrib(NAME_, "package2A").setChildren(
                f.get(FILE).putAttrib(NAME_, "classA").setChildren(
                        f.get(IMPORT).setChildren(
                                f.get(IMPORT_ITEM).putAttrib(LIT_, "someDirA.someFileA"),
                                f.get(IMPORT_ITEM).putAttrib(LIT_, "someDirB.someFileB")
                        ),
                        f.get(CLASS).putAttrib(NAME_, "classA").setChildren(
                                f.get(STATEMENT).setChildren(
                                        f.get(VAR_DEF_SCALAR).putAttrib(NAME_, "classAStuff").putAttrib(TYPE_DATA_, INT),
                                        f.get(ASSIGN),
                                        f.get(LIT).putAttrib(LIT_, 42)
                                ),
                                f.get(METHOD).putAttrib(NAME_, "methodA").putAttrib(TYPE_RETURN_, STRING).setChildren(
                                        f.get(METHOD_ARGS).setChildren(
                                                f.get(VAR_DEF_SCALAR).putAttrib(NAME_, "methodAArg").putAttrib(TYPE_DATA_, INT)
                                        ),
                                        f.get(CODE_BLOCK)
                                )
                        )

                )
            )
        );
        //root.display(0);
        return root;
    }
    public ICodeNode mockVarDef(){
        return mockSinglePackageClass(
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_SCALAR).putAttrib(NAME_, "myScalar")
                                .putAttrib(STATIC_, TRUE)
                                .putAttrib(FINAL_, TRUE)
                                .putAttrib(TYPE_DATA_, INT),
                        f.get(ASSIGN),
                        "42"
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_LIST).putAttrib(NAME_, "myList").putAttrib(TYPE_DATA_, STRING),
                        f.get(ASSIGN),
                        f.get(NEW_LIST).setChildren("Arrays.toList(\"1\", \"2\", \"3\")")
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_MAP).putAttrib(NAME_, "myMap").putAttrib(TYPE_DATA_, STRING,INT),
                        f.get(ASSIGN),
                        f.get(NEW_MAP)
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_SET).putAttrib(NAME_, "mySet").putAttrib(TYPE_DATA_, STRING),
                        f.get(ASSIGN),
                        f.get(NEW_SET)
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_OBJECT).putAttrib(NAME_, "myObject").putAttrib(TYPE_DATA_, "SomeType"),
                        f.get(ASSIGN),
                        f.get(NEW_OBJECT)
                                .putAttrib(TYPE_DATA_, "SomeType")
                                .setChildren(
                                    "myList",
                                    f.get(FUN_CALL).putAttrib(NAME_, "myFunction"),
                                    "myScalar"
                                )
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_MAP).putAttrib(NAME_, "myTreeMap").putAttrib(TYPE_DATA_, STRING,FLOAT),
                        f.get(ASSIGN),
                        f.get(NEW_OBJECT)
                                .putAttrib(TYPE_DATA_, "TreeMap").putAttrib(IS_GENERIC_, TRUE)
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_ARRAY).putAttrib(NAME_, "myArray")
                                .putAttrib(TYPE_DATA_, INT),
                        f.get(ASSIGN),
                        f.get(NEW_ARRAY)
                                .putAttrib(TYPE_DATA_, INT)
                                .putAttrib(SIZE_, 100)
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_ARRAY).putAttrib(NAME_, "myIntArray")
                                .putAttrib(TYPE_DATA_, INT),
                        f.get(ASSIGN),
                        f.get(NEW_ARRAY)
                                .putAttrib(TYPE_DATA_, INT)
                                .setChildren(
                                    1, 2, 3,
                                    f.get(FUN_CALL).putAttrib(NAME_, "myFunction")
                                )
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_ARRAY).putAttrib(NAME_, "myStringArray")
                                .putAttrib(TYPE_DATA_, STRING),
                        f.get(ASSIGN),
                        f.get(NEW_ARRAY)
                                .putAttrib(TYPE_DATA_, STRING)
                                .setChildren("\"A\"", "\"B\"", "\"C\"")
                )
        );
    }
    public ICodeNode mockVarDefMultiFile(){
        return mockSpread(
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_SCALAR).putAttrib(NAME_, "myScalar")
                                .putAttrib(STATIC_, TRUE)
                                .putAttrib(FINAL_, TRUE)
                                .putAttrib(TYPE_DATA_, INT),
                        f.get(ASSIGN),
                        f.get(LIT).putAttrib(LIT_, 42)
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_LIST).putAttrib(NAME_, "myList").putAttrib(TYPE_DATA_, STRING),
                        f.get(ASSIGN),
                        f.get(NEW_LIST).putAttrib(ARGS_, "Arrays.toList(\"1\", \"2\", \"3\")")
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_MAP).putAttrib(NAME_, "myMap").putAttrib(TYPE_DATA_, STRING,INT),
                        f.get(ASSIGN),
                        f.get(NEW_MAP)
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_SET).putAttrib(NAME_, "mySet").putAttrib(TYPE_DATA_, STRING),
                        f.get(ASSIGN),
                        f.get(NEW_SET)
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_OBJECT).putAttrib(NAME_, "myObject").putAttrib(TYPE_DATA_, "SomeType"),
                        f.get(ASSIGN),
                        f.get(NEW_OBJECT)
                                .putAttrib(TYPE_DATA_, "SomeType")
                                .putAttrib(ARGS_, "myList", "myScalar")
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_MAP).putAttrib(NAME_, "myTreeMap").putAttrib(TYPE_DATA_, STRING, FLOAT),
                        f.get(ASSIGN),
                        f.get(NEW_OBJECT)
                                .putAttrib(TYPE_DATA_, "TreeMap").putAttrib(IS_GENERIC_, TRUE)
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_ARRAY).putAttrib(NAME_, "myArray")
                                .putAttrib(TYPE_DATA_, INT),
                        f.get(ASSIGN),
                        f.get(NEW_ARRAY)
                                .putAttrib(TYPE_DATA_, INT)
                                .putAttrib(SIZE_, 100)
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_ARRAY).putAttrib(NAME_, "myIntArray")
                                .putAttrib(TYPE_DATA_, INT),
                        f.get(ASSIGN),
                        f.get(NEW_ARRAY)
                                .putAttrib(TYPE_DATA_, INT)
                                .putAttrib(ARGS_, 1, 2, 3)
                ),
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_ARRAY).putAttrib(NAME_, "myStringArray")
                                .putAttrib(TYPE_DATA_, STRING),
                        f.get(ASSIGN),
                        f.get(NEW_ARRAY)
                                .putAttrib(TYPE_DATA_, STRING)
                                .putAttrib(ARGS_, "A", "B", "C")
                )
        );
    }
    public ICodeNode mockBigArray(){
        long[] bigLong = new long[] {
                0x3510937901BA531CL,  0x19976161965BE08AL,  0xF41B039D2073D12L,  0xA8AC2A0020851C5AL,
                0x404B2D082F62EAB9L,  0x433B4C190092E513L,  0xC1C580133AC18014L,  0x8E7B7225C921032BL,
                0x10E222EDB010CE61L,  0xCB1084DBA8C42261L,  0x3DEC54D9BC4B309L,  0x2E5A2A024A2A450FL,
                0xA024C1008023A014L,  0xAA438EEF89A06401L,  0x6F654A7A20ED7506L,
        };

        return mockSinglePackageClass(
            f.get(STATEMENT).setChildren(
                    f.get(VAR_DEF_ARRAY).putAttrib(NAME_, "myLongArray")
                            .putAttrib(TYPE_DATA_, LONG),
                    f.get(ASSIGN),
                    f.get(NEW_ARRAY)
                            .putAttrib(TYPE_DATA_, LONG)
                            .setChildren(bigLong)
            )
        );
    }

    public ICodeNode mockListAsArray(){
        List<Integer> list = Arrays.asList(33, 44, 55);

        return mockSinglePackageClass(
                f.get(STATEMENT).setChildren(
                        f.get(VAR_DEF_ARRAY).putAttrib(NAME_, "myIntArray")
                                .putAttrib(TYPE_DATA_, INT),
                        f.get(ASSIGN),
                        f.get(NEW_ARRAY)
                                .putAttrib(TYPE_DATA_, INT)
                                .setChildren(list)
                )
        );
    }

    public ICodeNode mockFunCall(){
        return mockSinglePackageClass(
            this.method_(
                f.get(STATEMENT).setChildren(
                    f.get(FUN_CALL).putAttrib(NAME_, "myFun")
                        .putAttrib(ARGS_, "\"Hello\"", "\"Goodbye\"")
                        .setChildren(
                        f.get(FUN_CALL).putAttrib(NAME_, "myOtherFun")
                    )
                )
            )
        );
    }
    public ICodeNode mockIfElse(){
        return mockSinglePackageClass(
                this.method_(
                        f.get(IF_ELSE).setChildren(
                                f.get(CONDITIONAL).setChildren(
                                        f.get(BOOL_BLOCK).setChildren(
                                                f.get(LIT).putAttrib(LIT_, 24),
                                                f.get(COMPARISON).putAttrib(TYPE_COMP_, LTE),
                                                f.get(LIT).putAttrib(LIT_, 48)
                                        ),
                                        f.get(CONJUNCTION).putAttrib(TYPE_CONJ_, AND),
                                        f.get(BOOL_BLOCK).putAttrib(IS_NEGATE_, TRUE).setChildren(
                                                f.get(LIT).putAttrib(LIT_, 2),
                                                f.get(COMPARISON).putAttrib(TYPE_COMP_, EQ),
                                                f.get(LIT).putAttrib(LIT_, 17)
                                        )
                                ),
                                f.get(CODE_BLOCK).setChildren(
                                    f.get(STATEMENT).setChildren(
                                        f.get(LIT).putAttrib(LIT_, "System.out.println(\"It's true\")")
                                    )
                                ),
                                f.get(ELSE).setChildren(
                                        f.get(CODE_BLOCK).setChildren(
                                            f.get(STATEMENT).setChildren(
                                                    f.get(LIT).putAttrib(LIT_, "System.out.println(\"It's not true\")")
                                            )
                                        )
                                )
                        )
                )
        );
    }

    public ICodeNode mockWhile(CODE_NODE doOrWhile){
        return mockSinglePackageClass(
                this.method_(
                        f.get(STATEMENT).setChildren(
                                f.get(VAR_DEF_SCALAR).putAttrib(NAME_, "i")
                                        .putAttrib(TYPE_DATA_, INT),
                                f.get(ASSIGN),
                                f.get(LIT).putAttrib(LIT_, "0")
                        ),
                        f.get(doOrWhile).setChildren(
                                f.get(CONDITIONAL).setChildren(
                                        f.get(BOOL_BLOCK).setChildren(
                                                f.get(LIT).putAttrib(LIT_, "i"),
                                                f.get(COMPARISON).putAttrib(TYPE_COMP_, "LT"),
                                                f.get(LIT).putAttrib(LIT_, "20")
                                        )
                                ),
                                f.get(CODE_BLOCK).setChildren(
                                        f.get(STATEMENT).setChildren(
                                                f.get(LIT).putAttrib(LIT_, "System.out.println(\"i = \" + i)")
                                        ),
                                        f.get(STATEMENT).setChildren(
                                                f.get(LIT).putAttrib(LIT_, "i++")
                                        )
                                )
                        )
                )
        );
    }

    public ICodeNode glob_(ICodeNode... children){
        return f.get(GLOB).putAttrib(LANGUAGE_, JAVA_.toString()).setChildren(children);
    }
    public ICodeNode package_(String packageName, ICodeNode... children){
        return f.get(PACKAGE).putAttrib(NAME_, packageName).setChildren(children);
    }
    public ICodeNode file_(String packageName, ICodeNode... children){
        return f.get(FILE).putAttrib(NAME_, packageName).setChildren(children);
    }
    public ICodeNode class_(String packageName, ICodeNode... children){
        return f.get(CLASS).putAttrib(NAME_, packageName).setChildren(children);
    }
    public ICodeNode method_(ICodeNode... children){
        return f.get(METHOD).putAttrib(NAME_, "myMethod").setChildren(
                f.get(METHOD_ARGS),
                f.get(CODE_BLOCK).setChildren(children)
        );
    }
    public ICodeNode mockSinglePackageClass(ICodeNode... children){
        return glob_(
          package_("package1",
              file_("ClassA",
                      f.get(IMPORT),
                      class_("ClassA", children)
              )
          )
        );
    }
    public ICodeNode mockSpread(ICodeNode... children){
        int len = children.length;
        if(len >= 3){
            int inc = (int)(len/3);
            ICodeNode[] children1 = Arrays.copyOfRange(children, 0, inc);
            ICodeNode[] children2 = Arrays.copyOfRange(children, inc, inc * 2);
            ICodeNode[] children3 = Arrays.copyOfRange(children, inc * 2, len);
            return glob_(
                    package_("package1",
                            file_("ClassA",
                                    f.get(IMPORT),
                                    class_("ClassA", children1)
                            )
                    ),
                    package_("package2",
                            file_("ClassA",
                                    f.get(IMPORT),
                                    class_("ClassA", children2)
                            ),
                            file_("ClassB",
                                    f.get(IMPORT),
                                    class_("ClassB", children3)
                            )
                    )
            );
        }
        else{
            return mockSinglePackageClass(children);
        }

    }
}
