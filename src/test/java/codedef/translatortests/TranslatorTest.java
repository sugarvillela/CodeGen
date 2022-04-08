package codedef.translatortests;

import codedef.iface.ICodeNode;
import mock.MockSource;
import org.junit.jupiter.api.Test;
import runstate.Glob;
import translators.strategy.WriteStrategyDisplay;
import translators.strategy.WriteStrategyFile;
import translators.strategy.WriteStrategyList;

import java.util.ArrayList;
import java.util.List;

import static codedef.enums.CODE_NODE.DO_WHILE;
import static codedef.enums.CODE_NODE.WHILE;

class TranslatorTest {
    MockSource mockSource = new MockSource();

    private void dispTest(ICodeNode root){
        Glob.TRANSLATION_CENTER.setRoot(root).setWriteStrategy(
                new WriteStrategyDisplay()
        ).readAttribFromRoot().go();
    }
    @Test
    void givenTree_writeJson() {
        ICodeNode root = mockSource.mockCodeNodeTree();

        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath("test2.json");

        Glob.TRANSLATION_CENTER.setRoot(root).exportJson(filePath);
    }

    @Test
    void givenTree_getUnformatted() {
        ICodeNode root = mockSource.mockCodeNodeTree();

        String actual = Glob.TRANSLATION_CENTER.setRoot(root).
            setKeepUnFormatted(true).go().getUnformatted();

        System.out.println(actual);
    }

    @Test
    void givenTree_getFormattedList() {
        ICodeNode root = mockSource.mockCodeNodeTree();
        List<String> list = new ArrayList<>();

        Glob.TRANSLATION_CENTER.setRoot(root).setWriteStrategy(
                new WriteStrategyList(list)
        ).go(); // .setExternalPath(Glob.DEFAULT_PATH)

        for(String line : list){
            System.out.println(line);
        }
    }

    @Test
    void givenTree_writeCode() {
        ICodeNode root = mockSource.mockCodeNodeTree();

        Glob.TRANSLATION_CENTER.setRoot(root).setWriteStrategy(
                new WriteStrategyFile()
        ).setExternalPath(Glob.DEFAULT_PATH).go();
    }

    @Test
    void givenJsonFile_buildTree() {
        String fileName = "test2.json";
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath(fileName);
        ICodeNode root = Glob.TRANSLATION_CENTER.importJson(filePath).getRoot();
        root.display(0);
//        String actual = root.translator().format(root);
//        System.out.println(actual);
    }

    @Test
    void givenJsonFile_useAttributesFromFile() {
        String fileName = "test2.json";
        String filePath = Glob.FILE_NAME_UTIL.mergeDefaultPath(fileName);
        Glob.TRANSLATION_CENTER.importJson(filePath).setWriteStrategy(
            new WriteStrategyDisplay()
        ).readAttribFromRoot().go();
    }

    @Test
    void givenTree_displayFormatted() {
        ICodeNode root = mockSource.mockCodeNodeTree();
        dispTest(root);
    }

    @Test
    void givenVarDefs_displayFormatted() {
        Object o = Glob.ATTRIB_CONVERT_UTIL;
        ICodeNode root = mockSource.mockVarDef();
        dispTest(root);
    }

    @Test
    void givenVarDefs_displayMultiFile() {
        ICodeNode root = mockSource.mockVarDefMultiFile();
        dispTest(root);
    }

    @Test
    void givenBigNumericArray_displayFormatted() {
        ICodeNode root = mockSource.mockBigArray();
        dispTest(root);
    }

    @Test
    void givenNumericList_displayFormatted() {
        ICodeNode root = mockSource.mockListAsArray();
        dispTest(root);
    }

    @Test
    void givenFunCall_display() {
        ICodeNode root = mockSource.mockFunCall();
        dispTest(root);
    }
    @Test
    void givenIfElse_display() {
        ICodeNode root = mockSource.mockIfElse();
        dispTest(root);
    }
    @Test
    void givenWhile_display() {
        ICodeNode root = mockSource.mockWhile(WHILE);
        dispTest(root);
    }

    @Test
    void givenDoWhile_display() {
        ICodeNode root = mockSource.mockWhile(DO_WHILE);
        dispTest(root);
    }

}