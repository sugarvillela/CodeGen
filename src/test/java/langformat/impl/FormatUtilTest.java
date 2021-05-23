package langformat.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

class FormatUtilTest {
    @Test
    void givenShortLines_returnTabbedContent(){
        FormatUtil formatUtil = new FormatUtil();
        formatUtil.addLine("foo{");
        formatUtil.inc();
        formatUtil.addLine("//code here");
        formatUtil.dec();
        formatUtil.addLine("}");
        List<String> content = formatUtil.getContent();
        System.out.println(String.join("\n", content));
        String expected = "foo{|    //code here|}";
        String actual = String.join("|", content);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenLongLines_returnMarginControlledContent(){
        FormatUtil formatUtil = new FormatUtil();
        formatUtil.addLine("foo{");
        formatUtil.inc();
        formatUtil.addLine("here is a long bit of code that should extend across two lines when the margin is set to seventy");
        formatUtil.dec();
        formatUtil.addLine("}");
        List<String> content = formatUtil.getContent();
        System.out.println(String.join("\n", content));
        String expected = "foo{|    here is a long bit of code that should extend across two lines when|    the margin is set to seventy|}";
        String actual = String.join("|", content);
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
    @Test
    void givenVeryLongLines_returnMarginControlledContent(){
        int actual, expected;
        FormatUtil formatUtil = new FormatUtil();
        formatUtil.addLine(longString());
        List<String> content = formatUtil.getContent();

        actual = content.size();
        expected = 1000;
        Assertions.assertEquals(expected, actual);

        expected = 76;
        for(String line : content){
            actual = line.length();
            Assertions.assertEquals(expected, actual);
        }
    }
}