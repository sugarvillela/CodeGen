package langformat.iface;

import tokenizer.iface.ISplitUtil;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.SplitUtil;
import tokenizer.impl.Tokenizer;

public interface IFormatStrategy {
    default int checkLine(String text){
        int indent = 0;
        boolean skip = false;
        for(int i = 0; i < text.length(); i++){
            char curr = text.charAt(i);
            if(curr == '"'){
                skip = !skip;
            }
            else if (!skip){
                if(curr == '{'){
                    indent++;
                }
                else if(curr == '}'){
                    indent--;
                }
            }
        }
        return indent;
    }
    default ITokenizer getTokenizer(){
        return Tokenizer.builder().delimiters('\n').keepEscapeSymbol().skipSymbols("'\"").build();
    }
    default ISplitUtil getSplitUtil(int margin){
        return new SplitUtil("\"").setStartPos(margin);
    }
}
