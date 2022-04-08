package tokenizer.composite;

import tokenizer.iface.IStringParser;
import tokenizer.impl.CharMatch;

import java.util.List;

public class CharTok extends BaseTok {
    public CharTok() {
        super(new CharMatch());
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
            int prevIndex = 0;

            for(int i = 0; i < hitMap.size(); i++){
                int j = hitMap.get(i);      // index of found delimiter
                int k = j + 1;              // index of next regular text

                if(prevIndex != j){
                    if(!tryAddToken(text, prevIndex, j)){
                        return this;
                    }
                }
                if(parser.isTokenizeDelimiter()){
                    if(!parser.isDelimiterOnce() || prevIndex != j){
                        if(!tryAddToken(text, j, k)){
                            return this;
                        }
                    }
                }

                prevIndex = k;
            }
            if(prevIndex < text.length()){
                tokens.add(text.substring(prevIndex));
            }
        }
        return this;
    }
}
