package tokenizer.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


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
}