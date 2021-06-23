package tokenizer.composite2;

import tokenizer.composite.BaseTok;
import tokenizer.composite.CharTok;
import tokenizer.iface.IStringParser;
import tokenizer.impl.CharMatch;
import tokenizer.util_iface.ISymbolPairs;

import java.util.List;

public class SpaceUtil extends BaseTok {
    public SpaceUtil() {
        super(new CharMatch().setDelimiter(" "));
    }

    @Override
    public IStringParser setDelimiter(String delimiters){
        return this;
    }

    @Override
    public IStringParser parse() {
        parser.parse();
        List<Integer> hitMap = parser.numericToList();
        if(!hitMap.isEmpty()){
            int i = 0;

            // skip spaces at front
            for(; i < hitMap.size(); i++){
                if(i != hitMap.get(i)){
                    break;
                }
            }
            String text = parser.getText();
            IStringParser tokenizer = new CharTok().setStartPos(i).setDelimiter(" ")
                    .setSkipSymbols(parser.getSymbolPairs()).setText(text);
            parser.setText(String.join(" ", tokenizer.parse().toList()));
        }
        return this;
    }
}
