package tokenizer.util_impl;

import tokenizer.util_iface.ISymbolPairs;

public class SymbolPairs implements ISymbolPairs {
    private final char[] oSymbols, cSymbols;

    public SymbolPairs(String symbolList) {
        int len = symbolList.length();
        oSymbols = new char[len];
        cSymbols = new char[len];
        for(int i = 0; i < len; i++){
            oSymbols[i] = symbolList.charAt(i);
            cSymbols[i] = this.mapOpenToClose(symbolList.charAt(i));
        }
    }

    public SymbolPairs(char oneOpenSymbol) {
        oSymbols = new char[]{oneOpenSymbol};
        cSymbols = new char[]{this.mapOpenToClose(oneOpenSymbol)};
    }

    public SymbolPairs(char openingSymbol, char closingSymbol) {
        oSymbols = new char[]{openingSymbol};
        cSymbols = new char[]{closingSymbol};
    }

    public SymbolPairs(char[] oSymbols, char[] cSymbols) {
        this.oSymbols = oSymbols;
        this.cSymbols = cSymbols;
    }

    private char mapOpenToClose(char open){
        switch (open){
            case '{':
                return '}';
            case '[':
                return ']';
            case '(':
                return ')';
            case '<':
                return '>';

            default:
                return open;
        }
    }

    @Override
    public char[] getOSymbols(){
        return oSymbols;
    }

    @Override
    public char[] getCSymbols(){
        return cSymbols;
    }
}
