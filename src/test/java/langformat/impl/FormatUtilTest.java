package langformat.impl;

import langformat.iface.IFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

class FormatUtilTest {
    @Test
    void givenUntrimmedText_returnBackTrimmedText(){
        IFormatter formatter = Formatter.initInstance(new FormatStrategyCType());
        String text = "    four    ";
        String expected = "    four";
        String actual = formatter.trimBack(text);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenAppendedWords_returnFormatted(){
        IFormatter formatter = Formatter.initInstance(new FormatStrategyCType());
        StringBuilder code = new StringBuilder();
        formatter.addWord_(code, "    four    ");
        formatter.addWord_(code, "five    ");
        String expected = "    four five ";
        String actual = formatter.formatAll(code.toString());
        Assertions.assertEquals(expected, actual);
    }

    private String longString(){
        String[] main = new String[1000];
        String curr = "abcdefghij abcdefghij abcdefghij abcdefghij abcdefghij abcdefghij abcdefghij";
        for(int i = 0; i < 1000; i++){
            main[i] = curr;
        }
        return String.join(" ", main);
    }
}