package tokenizer.composite2;

import tokenizer.composite.BaseComposite;
import tokenizer.composite.CharTok;
import tokenizer.iface.IStringParser;

import java.util.List;

public class IndentUtil extends BaseComposite {
    private int[] indentMap;

    public IndentUtil() {
        super(new CharTok().setDelimiter(" "));
    }

    // override to prevent setting a different delimiter
    @Override
    public IStringParser setDelimiter(String delimiters) {
        return this;
    }

    @Override
    public IStringParser parse() {
        parser.parse();
        List<Integer> hitMap = parser.numericToList();
        List<String> tokens = parser.toList();
        int len = hitMap.size();

        indentMap = new int[tokens.size()];
        int k = -1, last = -len;
        for(int i = 0; i < len; i++){
            int curr = hitMap.get(i);
            if(curr - last > 1){
                k++;
            }
            indentMap[k]++;
            last = curr;
        }
        return this;
    }
    public int[] getIndentMap(){
        return indentMap;
    }
}
