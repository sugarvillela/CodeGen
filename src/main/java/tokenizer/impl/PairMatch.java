package tokenizer.impl;

import tokenizer.iface.IStringParser;
import tokenizer.util_iface.ISymbolPairs;
import tokenizer.util_impl.SymbolPairs;
import tokenizer.util_impl.SymbolPairsNop;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class PairMatch extends BaseStringParser {
    public static final int NOT_SET = -1, ORPHAN_SYMBOL = -2;
    private final Stack<WrapNode> wrapStack;
    private ISymbolPairs delimiterPairs;      // matched open/close char arrays
    private int[] hitMap;

    public PairMatch() {
        wrapStack = new Stack<>();
    }

    @Override
    public IStringParser setDelimiter(String delimiters) {
        this.delimiters = delimiters;
        delimiterPairs = new SymbolPairs(delimiters);
        return this;
    }

    @Override
    public IStringParser parse() {
        if(delimiterPairs != null){
            if(symbolPairs == null){
                symbolPairs = new SymbolPairsNop();
            }
            this.initHitMap();
            skipStack.clear();
            wrapStack.clear();
            escaped = false;
            for(int i = 0; i < text.length(); i++){
                this.parseByChar(i);
            }
        }
        return this;
    }
    @Override
    public List<Integer> numericToList() {
        return Arrays.stream(this.numericToArray()).boxed().collect(Collectors.toList());
    }

    @Override
    public int[] numericToArray() {
        return hitMap;
    }

    protected void parseByChar(int i) {
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
            else if(!tryPushSkipSymbol(curr) && i >= startPos){
                int poppedSource = tryPopSource(i);
                if(poppedSource == -1){
                    if(tryPushSource(i) || isOrphanCloser(i)){
                        hitMap[i] = ORPHAN_SYMBOL;
                    }
                }
                else {
                    hitMap[poppedSource] = i;
                    hitMap[i] = poppedSource;
                }
            }
        }
    }

    @Override
    public int numeric() {
        return 0;
    }

    @Override
    protected void initCaseTest() { }

    private void initHitMap(){
        hitMap = new int[text.length()];
        for(int i = 0; i < text.length(); i++){
            hitMap[i] = NOT_SET;
        }
    }

    private int tryPopSource(int i){
        if(!wrapStack.isEmpty() && wrapStack.peek().cChar == text.charAt(i)){
            return wrapStack.pop().iSource;
        }
        return -1;
    }

    private boolean tryPushSource(int i){
        char[] oSymbols = delimiterPairs.getOSymbols();
        for(int x = 0; x < oSymbols.length; x++){
            if(oSymbols[x] == text.charAt(i)){
                this.wrapStack.push(new WrapNode(i, delimiterPairs.getCSymbols()[x]));
                return true;
            }
        }
        return false;
    }

    private boolean isOrphanCloser(int i){
        char[] cSymbols = delimiterPairs.getCSymbols();
        for(int x = 0; x < cSymbols.length; x++){
            if(cSymbols[x] == text.charAt(i)){
                return true;
            }
        }
        return false;
    }

    private static class WrapNode {
        public final int iSource;
        public final char cChar;

        private WrapNode(int iSource, char cChar) {
            this.iSource = iSource;
            this.cChar = cChar;
        }
    }

    @Override
    public void setNumericArray(int[] numericArray) {
        this.hitMap = numericArray;
    }
}
