package tokenizer.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import tokenizer.iface.IMatchUtil;
import tokenizer.iface.ISplitUtil;

class SplitUtilTest {
    @Test
    void givenStringWithSpace_split() {
        ISplitUtil splitUtil = new SplitUtil("\"");
        String[] pair;
        String text, expected, actual;

        text = "zero one two three four five six seven eight nine ten";

        pair = splitUtil.split(text);
        expected = "zero|one two three four five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        pair = splitUtil.setStartPos(15).split(text);
        expected = "zero one two three|four five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        pair = splitUtil.setStartPos(4).split(text);
        expected = "zero|one two three four five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        pair = splitUtil.setStartPos(3).split(text);
        expected = "zero|one two three four five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        pair = splitUtil.setStartPos(100).split(text);
        expected = "zero one two three four five six seven eight nine ten|null";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenStringWithSkipArea_splitAfterSkip() {
        ISplitUtil splitUtil = new SplitUtil("\"");
        String[] pair;
        String text, expected, actual;

        text = "zero one two \"three four\" five six seven eight nine ten";

        pair = splitUtil.setStartPos(16).split(text);
        expected = "zero one two \"three four\"|five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        pair = splitUtil.setStartPos(13).split(text);
        expected = "zero one two \"three four\"|five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        pair = splitUtil.setStartPos(24).split(text);
        expected = "zero one two \"three four\"|five six seven eight nine ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenStringWithBorderingSkipArea_splitAfterSkip() {
        ISplitUtil splitUtil = new SplitUtil("\"");
        String[] pair;
        String text, expected, actual;

        text = "nine \"ten\"";
        pair = splitUtil.setStartPos(6).split(text);
        expected = "nine \"ten\"|null";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);

        text = "\"eight nine\" ten";
        pair = splitUtil.split(text);
        expected = "\"eight nine\"|ten";
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenStringWithEscapeSymbol_splitOnNext() {
        ISplitUtil splitUtil = new SplitUtil("\"");
        String[] pair;
        String text, expected, actual;

        text = "zero one two\\ three four five six seven eight nine ten";

        pair = splitUtil.setStartPos(16).split(text);
        expected = "zero one two\\ three|four five six seven eight nine ten";;
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenStringWithEscapeSymbol_splitOnNext_deleteEscape() {
        ISplitUtil splitUtil = SplitUtil.builder().keepEscapeSymbol(false).build();
        String[] pair;
        String text, expected, actual;

        text = "zero one two\\ three four five six seven eight nine ten";

        pair = splitUtil.setStartPos(16).split(text);
        expected = "zero one two three|four five six seven eight nine ten";;
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenBuilder_useOtherDelimiter() {
        ISplitUtil splitUtil = SplitUtil.builder().delimiters('_').keepEscapeSymbol(false).build();
        String[] pair;
        String text, expected, actual;

        text = "zero one two_three four_five six seven eight nine ten";

        pair = splitUtil.setStartPos(16).split(text);
        expected = "zero one two_three four|five six seven eight nine ten";;
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenStringWithEmbeddedTarget_findTarget(){
        IMatchUtil matchUtil = new MatchUtil("D_END");
        String text = "zero D_E D_END three four five six seven eight nine ten";
        int actual1 = -1, actual2 = -1;
        int expected1 = 9, expected2 = 14;
        for(int i = 0; i < text.length(); i++){
            char curr = text.charAt(i);
            if(matchUtil.haveMatch(i, curr)){
                actual1 = matchUtil.indexStart();
                actual2 = matchUtil.indexFinish();;
            }
        }
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }
}