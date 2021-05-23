package tokenizer.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import tokenizer.iface.ISplitUtil;

class SplitUtilTest {
    @Test
    void GivenStringWithSpace_split() {
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
    void GivenStringWithSkipArea_splitAfterSkip() {
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
    void GivenStringWithBorderingSkipArea_splitAfterSkip() {
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
    void GivenStringWithEscapeSymbol_splitOnNext() {
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
    void GivenStringWithEscapeSymbol_splitOnNext_deleteEscape() {
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
    void GivenBuilder_useOtherDelimiter() {
        ISplitUtil splitUtil = SplitUtil.builder().delimiters('_').keepEscapeSymbol(false).build();
        String[] pair;
        String text, expected, actual;

        text = "zero one two_three four_five six seven eight nine ten";

        pair = splitUtil.setStartPos(16).split(text);
        expected = "zero one two_three four|five six seven eight nine ten";;
        actual = String.join("|", pair);
        Assertions.assertEquals(expected, actual);
    }
}