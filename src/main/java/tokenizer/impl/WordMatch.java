package tokenizer.impl;

import tokenizer.iface.IStringParser;
import tokenizer.util_iface.ICaseTest;
import tokenizer.util_impl.SymbolPairsNop;

import java.util.ArrayList;
import java.util.List;

public class WordMatch extends BaseStringParser {
    protected final List<Integer> hitMap;
    private int iStart, k;

    public WordMatch() {
        hitMap = new ArrayList<>();
    }

    @Override
    public IStringParser parse() {
        if(symbolPairs == null){
            symbolPairs = new SymbolPairsNop();
        }
        hitMap.clear();
        skipStack.clear();
        escaped = false;
        this.k = 0;
        for(int i = 0; i < text.length(); i++){
            if(this.parseByChar(i) && this.numeric() >= limit){
                break;
            }
        }
        return this;
    }

    protected boolean parseByChar(int i) {
        char curr = text.charAt(i);

        if(isEscape(curr)){
            k = 0;
            escaped = true;
        }
        else if(escaped){
            escaped = false;
        }
        else{
            if(isInSkipArea()){
                if(!tryPopSkipSymbol(curr)){
                    tryPushSkipSymbol(curr);
                }
            }
            else if(tryPushSkipSymbol(curr)){
                k = 0;
            }
            else{
                if(isDelimiter(curr, k)){
                    k++;
                    if(k == 1){
                        iStart = i;
                    }
                    if(k == delimiters.length()){
                        hitMap.add(iStart);     // start of match
                        hitMap.add(i + 1);    // next index after end of match
                        k = 0;
                        return true;
                    }
                }
                else{
                    k = 0;
                }
            }
        }
        return false;
    }

    @Override
    public int numeric() {
        return hitMap.size()/2;
    }

    @Override
    public List<Integer> numericToList() {
        return hitMap;
    }

    @Override
    public int[] numericToArray() {
        List<Integer> list = numericToList();
        int[] out = new int[list.size()];
        for(int i = 0; i < list.size(); i++){
            out[i] = list.get(i);
        }
        return out;
    }

    protected boolean isDelimiter(char symbol, int index){
        return caseTest.isMatch(symbol, index);//whitespaceTest.eq(symbol) ||
    }

    /*====protected setup utils=======================================================================================*/

    @Override
    protected void initCaseTest() {
        if(this.caseSensitive){
            caseTest = new ICaseTest() {
                @Override
                public boolean isMatch(char c, int index) {
                    return c == delimiters.charAt(index);
                }
            };
        }
        else{
            caseTest = new ICaseTest() {
                @Override
                public boolean isMatch(char c, int index) {
                    return c == delimiters.charAt(index) || swapCase(c) == delimiters.charAt(index);
                }
            };
        }
    }
}
