package langformat.impl;

import langformat.iface.IFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class FormatUtilTest {
    @Test
    void givenUntrimmedText_returnBackTrimmedText(){
        IFormatter formatter = Formatter.initInstance();
        String text = "    four    ";
        String expected = "    four";
        String actual = formatter.trimBack(text);
        Assertions.assertEquals(expected, actual);
    }
}