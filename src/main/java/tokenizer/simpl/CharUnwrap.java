package tokenizer.simpl;

import tokenizer.util_iface.ISymbolPairs;
import tokenizer.util_impl.SymbolPairs;

import java.util.Stack;

public class CharUnwrap {
    private final Stack<WrapNode> wrapStack;// Closing symbol, with index of matching opener
    protected ISymbolPairs symbolPairs;     // matched open/close char arrays
    private int limit;                      // number of tokens allowed
    private String text;

    public CharUnwrap() {
        wrapStack = new Stack<>();
        limit = 0xFFFFFFF;
    }

    public CharUnwrap setText(String text) {
        this.text = text;
        return this;
    }

    public CharUnwrap setWrapSymbols(String openingSymbols) {
        symbolPairs = new SymbolPairs(openingSymbols);
        return this;
    }

    public CharUnwrap setWrapSymbols(char oneOpeningSymbol) {
        symbolPairs = new SymbolPairs(oneOpeningSymbol);
        return this;
    }

    public CharUnwrap setWrapSymbols(char openingSymbol, char closingSymbol) {
        symbolPairs = new SymbolPairs(openingSymbol, closingSymbol);
        return this;
    }

    public CharUnwrap setWrapSymbols(char[] oMap, char[] cMap) {
        symbolPairs = new SymbolPairs(oMap, cMap);
        return this;
    }

    public CharUnwrap setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public CharUnwrap parse() {
        if(symbolPairs != null && text != null){
            int i = 0, j = text.length() - 1;
            this.parseSection(i, j);
            int lim = Math.min(limit, text.length()/2);
            while(i <= lim && this.parseSection(i, j)){
                i++;
                j--;
            }
            text = text.substring(i, j + 1);
        }
        return this;
    }

    public String getText() {
        return text;
    }

    public boolean isError(){
        return !wrapStack.isEmpty();
    }

    /*====parse utility methods=======================================================================================*/

    private boolean parseSection(int start, int end){
        wrapStack.clear();
        if(tryPushSource(start)){
            WrapNode root = wrapStack.peek();
            for(int i = start + 1; i <= end; i++){
                int poppedSource = tryPopSource(i);
                if(poppedSource == -1){
                    tryPushSource(i);
                }
                else if(root.iSource == poppedSource){
                    return i == end;
                }
            }
        }
        return false;
    }

    private boolean tryPushSource(int i){
        char[] oSymbols = symbolPairs.getOSymbols();
        for(int x = 0; x < oSymbols.length; x++){
            if(oSymbols[x] == text.charAt(i)){
                this.wrapStack.push(new WrapNode(i, symbolPairs.getCSymbols()[x]));
                return true;
            }
        }
        return false;
    }

    private int tryPopSource(int i){
        if(!wrapStack.isEmpty() && wrapStack.peek().cChar == text.charAt(i)){
            return wrapStack.pop().iSource;
        }
        return -1;
    }

    private static class WrapNode {
        public final int iSource;
        public final char cChar;

        private WrapNode(int iSource, char cChar) {
            this.iSource = iSource;
            this.cChar = cChar;
        }
    }
}
