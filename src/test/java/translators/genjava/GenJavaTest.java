package translators.genjava;

import codedef.iface.ICodeNode;
import generictree.impl.CodeNodeTree;
import mock.MockSource;
import org.junit.jupiter.api.Test;
import runstate.Glob;

import static codedef.modifier.CODE_NODE.CLASS;
import static codedef.modifier.MODIFIER.*;

class GenJavaTest {
    MockSource mockSource = new MockSource();
    
    @Test
    void givenPopulatedClassNode_outputRightOrder(){
        ICodeNode codeNode = Glob.PROTOTYPE_FACTORY.get(CLASS);
        codeNode.getAttribModifier().put(IMPLEMENTS, "Interface1", "Interface2", "Interface3");
        codeNode.getAttribModifier().put(STATIC, "TRUE");
        codeNode.getAttribModifier().put(NAME, "Class1");
        codeNode.getAttribModifier().put(EXTENDS, "OtherClass1");
        codeNode.getAttribModifier().put(VISIBILITY, "PROTECTED");

        String actual = codeNode.translator().go(codeNode);
        System.out.println(actual);
    }
    @Test
    void givenPopulatedTree(){
//        CodeNodeTree pathTree = mockSource.getPopulatedTree3();
//        pathTree.display();
//        ICodeNode codeNode = pathTree.getRoot().getPayload();
//        String actual = codeNode.translator().format(codeNode);
//        System.out.println(actual);
    }
    @Test
    void givenPopulatedTreeFromFile(){
        CodeNodeTree tree = mockSource.getPopulatedTreeFromFile("test3.json");
//        ICodeNode rootPayload = tree.getRoot().getPayload();
//        System.out.println("rootPayload: " + rootPayload.getClass().getSimpleName());
//        IFormatUtil formatUtil = new FormatUtil();
//
//        ITranslator translator = rootPayload.translator();
//        translator.translate(formatUtil, rootPayload);
//
//        List<String> content = formatUtil.getContent();
//        System.out.println(String.join("\n", content));
    }
    @Test
    void givenPopulatedTree_display() {
//        CodeNodeTree pathTree = mockSource.getSmallTree();
//        pathTree.display();
//        ICodeNode rootPayload = pathTree.getRoot().getPayload();
//
//        System.out.println("\npaths");
    }
}