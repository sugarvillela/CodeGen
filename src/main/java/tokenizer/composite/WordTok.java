package tokenizer.composite;

import tokenizer.iface.IStringParser;
import tokenizer.impl.WordMatch;

import java.util.List;

public class WordTok extends BaseTok {
    public WordTok() {
        super(new WordMatch());
    }

    @Override
    public IStringParser parse() {
        parser.parse();
        String text = parser.getText();
        tokens.clear();
        if(parser.numeric() == 0){
            tokens.add(text);
        }
        else{
            List<Integer> hitMap = parser.numericToList();
            int last = 0;
            for(int i = 0; i < hitMap.size(); i += 2){
                int j = hitMap.get(i);
                int k = hitMap.get(i + 1);
                if(last != j){
                    if(!tryAddToken(text, last, j)){
                        return this;
                    }
                }
                if(parser.isTokenizeDelimiter()){
                    if(!tryAddToken(text, j, k)){
                        return this;
                    }
                }
                last = k;
            }
            if(last < text.length()){
                tokens.add(text.substring(last));
            }
        }
        return this;
    }
}
