package utilfile;

import datasource.core.SourceArray;
import datasource.dec.SourceNonEmpty;
import datasource.dec.SourceTextEvent;
import datasource.dec_tok.SourceTok;
import datasource.iface.IDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import readnode.iface.IReadNode;
import readnode.impl.ReadNode;
import runstate.Glob;

class FileTest {
    private IReadNode getMockReadNode(){
        int row = 0;
        return ReadNode.builder().source("source").row(row).col(row).text("someText").build();
    }

    private IDataSource mockObjectArray(){
        String[] array = {
                "[{\"color\":\"red\",\"value\":\"#f00\"},",
                "{\"color\":\"green\",\"value\":\"#0f0\"}]"
        };
        return new SourceArray(array);
    }
    private String mockObjectArrayExpectedText(){
        return "[|" +
                "{|" +
                "\"color\":\"red\"|" +
                ",|" +
                "\"value\":\"#f00\"|" +
                "}|" +
                ",|" +
                "{|" +
                "\"color\":\"green\"|" +
                ",|" +
                "\"value\":\"#0f0\"|" +
                "}|" +
                "]";
    }
    private String mockObjectArrayExpectedEvents(){
        return "PUSH,JSON_ARR,[|" +
                "PUSH,ARR_VAL,[|" +
                "PUSH,JSON_OBJ,{|" +
                "PUSH,OBJ_KEY,{|" +
                "ADD_DATA,DATA_VAL,\"color\"|" +
                "POP,OBJ_KEY,:|" +
                "PUSH,OBJ_VAL,:|" +
                "ADD_DATA,DATA_VAL,\"red\"|" +
                "POP,OBJ_VAL,,|" +
                "PUSH,OBJ_KEY,,|" +
                "ADD_DATA,DATA_VAL,\"value\"|" +
                "POP,OBJ_KEY,:|" +
                "PUSH,OBJ_VAL,:|" +
                "ADD_DATA,DATA_VAL,\"#f00\"|" +
                "POP,OBJ_VAL,}|" +
                "POP,JSON_OBJ,}|" +
                "POP,ARR_VAL,,|" +
                "PUSH,ARR_VAL,,|" +
                "PUSH,JSON_OBJ,{|" +
                "PUSH,OBJ_KEY,{|" +
                "ADD_DATA,DATA_VAL,\"color\"|" +
                "POP,OBJ_KEY,:|" +
                "PUSH,OBJ_VAL,:|" +
                "ADD_DATA,DATA_VAL,\"green\"|" +
                "POP,OBJ_VAL,,|" +
                "PUSH,OBJ_KEY,,|" +
                "ADD_DATA,DATA_VAL,\"value\"|" +
                "POP,OBJ_KEY,:|" +
                "PUSH,OBJ_VAL,:|" +
                "ADD_DATA,DATA_VAL,\"#0f0\"|" +
                "POP,OBJ_VAL,}|" +
                "POP,JSON_OBJ,}|" +
                "POP,ARR_VAL,]|" +
                "POP,JSON_ARR,]";
    }

    private IDataSource mockBadObjectArray(){// double colon
        String[] array = {
                "[{\"color\"::\"red\",\"value\":\"#f00\"},",
                "{\"color\":\"green\",\"value\":\"#0f0\"}]"
        };
        return new SourceArray(array);
    }
    private IDataSource mockBadObjectArray2(){// double comma
        String[] array = {
                "[{\"color\":\"red\",,\"value\":\"#f00\"},",
                "{\"color\":\"green\",\"value\":\"#0f0\"}]"
        };
        return new SourceArray(array);
    }
    private IDataSource mockBadObjectArray3(){
        String[] array = {
                "[{\"color\":\"red\":\"value\":\"#f00\"},",
                "{\"color\":\"green\",\"value\":\"#0f0\"}]"
        };
        return new SourceArray(array);
    }
    @Test
    void givenJSON_shouldIterateTokenized() {
        IDataSource dataSource = new SourceTok(
                new SourceNonEmpty(
                        mockObjectArray()
//                        new SourceFile(
//                                FileNameUtil.initInstance().mergeDefaultPath("test1.json")
//                        )
                )
        );
        String actual = TestUtil.iterateAndJoin(dataSource, TestUtil.D_TEXT);
        String expected = mockObjectArrayExpectedText();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenJSON_shouldLocateTextEvents() {
        IDataSource dataSource =
            new SourceTextEvent(
                new SourceTok(
                        new SourceNonEmpty(
                                mockObjectArray()
                        )
                )
            );
        String actual = TestUtil.iterateAndJoin(dataSource, TestUtil.D_EVENT);
        String expected = mockObjectArrayExpectedEvents();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenBadJSON_shouldQuit() {
        IDataSource dataSource =
                new SourceTextEvent(
                        new SourceTok(
                                new SourceNonEmpty(
                                        mockBadObjectArray()
                                )
                        )
                );
        TestUtil.iterateAndJoin(dataSource, TestUtil.D_EVENT);
    }
    @Test
    void givenBadJSON2_shouldQuit() {
        IDataSource dataSource =
                new SourceTextEvent(
                        new SourceTok(
                                new SourceNonEmpty(
                                        mockBadObjectArray2()
                                )
                        )
                );
        TestUtil.iterateAndJoin(dataSource, TestUtil.D_EVENT);
    }
    @Test
    void givenBadJSON3_shouldQuit() {
        IDataSource dataSource =
                new SourceTextEvent(
                        new SourceTok(
                                new SourceNonEmpty(
                                        mockBadObjectArray3()
                                )
                        )
                );
        TestUtil.iterateAndJoin(dataSource, TestUtil.D_EVENT);
    }

    @Test
    void givenJSON1_shouldLocateTextEvents() {

        IDataSource dataSource =
                new SourceTextEvent(
                        new SourceTok(
                                new SourceNonEmpty(
                                        mockObjectArray()
                                )
                        )
                );
        Glob.RUN_STATE.initTest(dataSource);
//        IReadNode mockPayload = getMockReadNode();
//        Glob.RUN_STATE.setCurrNode(mockPayload);
        Glob.RUN_STATE.go();

//        String actual = TestUtil.iterateAndJoin(dataSource, TestUtil.D_EVENT);
//        String expected = mockObjectArrayExpectedEvents();
//        Assertions.assertEquals(expected, actual);
    }
}