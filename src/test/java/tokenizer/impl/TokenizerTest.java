package tokenizer.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tokenizer.iface.ITokenizer;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    @Test
    void parse() {
        String text = "[{color:\"red\",value:\"#f00\"}]";
        List<String> tok = Tokenizer.builder().delimiters('[', ']', '{', '}', ':', ',').skipSymbols("\"").
                tokenizeDelimiter().keepSkipSymbol().keepEscapeSymbol().build().setText(text).parse().toList();

        for(String t : tok){
            System.out.println(t);
        }
        String unTok = String.join("|", tok);
        System.out.println(unTok);
        assertEquals("[|{|color|:|\"red\"|,|value|:|\"#f00\"|}|]", unTok);
    }
    @Test
    void givenTextWithNewLineAndQuotes_splitOnNewLines(){
        ITokenizer tokenizer = Tokenizer.builder().delimiters('\n').keepSkipSymbol().keepEscapeSymbol().skipSymbols("'\"").build();
        String text = "String classField1 = \"comprende\";\n    public";
        List<String> tok = tokenizer.setText(text).parse().toList();
        for(String t : tok){
            System.out.println(t);
        }
    }
}