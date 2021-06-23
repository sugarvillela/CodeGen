package tokenizer.impl;

import tokenizer.iface.IStringParser;
import tokenizer.util_iface.ICaseTest;
import tokenizer.util_impl.SymbolPairsNop;

import java.util.ArrayList;
import java.util.List;

public class CharMatch extends BaseStringParser {
    protected final List<Integer> hitMap;

    public CharMatch() {
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
            else if(!tryPushSkipSymbol(curr) && i >= startPos && isDelimiter(curr)){
                hitMap.add(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public int numeric() {
        return hitMap.size();
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

    protected boolean isDelimiter(char symbol){
        return whitespaceTest.eq(symbol) || caseTest.contains(symbol);
    }

    /*====protected setup utils=======================================================================================*/

    @Override
    protected void initCaseTest(){
        if(this.caseSensitive){
            caseTest = new ICaseTest() {
                @Override
                public boolean contains(char c) {
                    return delimiters.indexOf(c) != -1;
                }
            };
        }
        else{
            caseTest = new ICaseTest() {
                @Override
                public boolean contains(char c) {
                    return delimiters.indexOf(c) != -1 || delimiters.indexOf(swapCase(c)) != -1;
                }
            };
        }
    }

}
