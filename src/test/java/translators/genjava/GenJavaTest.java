package translators.genjava;

import codedef.iface.ICodeNode;
import codedef.impl.PrototypeFactory;
import codedef.init.CodeDef;
import codejson.JsonToCodeTree;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;
import generictree.impl.CodeNodeTree;
import generictree.task.TaskCodeNodeParent;
import generictree.task.TaskDisp;
import langformat.iface.IFormatUtil;
import langformat.impl.FormatUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import runstate.Glob;
import translators.iface.ITranslator;
import utilfile.JsonObjFile;

import java.util.List;

import static codedef.modifier.CODE_NODE.CLASS;
import static codedef.modifier.MODIFIER.*;
import static org.junit.jupiter.api.Assertions.*;

class GenJavaTest {
    private CodeNodeTree getPopulatedTree(){
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test6.json");
        System.out.println("filePath: " + filePath);
        JSONObject jsonRoot = new JsonObjFile().getJsonObject(filePath);
        JsonToCodeTree jsonTree = new JsonToCodeTree();
        jsonTree.buildTree(jsonRoot);
        CodeNodeTree pathTree = jsonTree.getTree();

        IGTreeParse<ICodeNode> parser = pathTree.getParse();
        IGTreeTask<ICodeNode> parentLinker = new TaskCodeNodeParent();
        parser.preOrder(pathTree.getRoot(), parentLinker);

        List<String> allPaths = pathTree.getParse().getAllPaths(pathTree.getRoot(), '-');
        System.out.println(String.join("\n", allPaths));
        return pathTree;
    }
    @Test
    void givenPopulatedClassNode_outputRightOrder(){
        IFormatUtil formatUtil;
        ICodeNode codeNode;

        codeNode = Glob.PROTOTYPE_FACTORY.getPrototype(CLASS);
        codeNode.getAttribModifier().put(IMPLEMENTS, "Interface1", "Interface2", "Interface3");
        codeNode.getAttribModifier().put(STATIC, "TRUE");
        codeNode.getAttribModifier().put(NAME, "Class1");
        codeNode.getAttribModifier().put(EXTENDS, "OtherClass1");
        codeNode.getAttribModifier().put(VISIBILITY, "PROTECTED");

        formatUtil = new FormatUtil();
        codeNode.translator().translate(formatUtil, codeNode);
        List<String> content = formatUtil.getContent();
        System.out.println(String.join("\n", content));
    }
    @Test
    void givenPopulatedTree(){
        CodeNodeTree tree = getPopulatedTree();
        ICodeNode rootPayload = tree.getRoot().getPayload();
        System.out.println("rootPayload: " + rootPayload.getClass().getSimpleName());
        IFormatUtil formatUtil = new FormatUtil();

        ITranslator translator = rootPayload.translator();
        translator.translate(formatUtil, rootPayload);

        List<String> content = formatUtil.getContent();
        System.out.println(String.join("\n", content));
    }
}