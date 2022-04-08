package tokenizer.util_impl;

import tokenizer.util_iface.ISymbolPairs;

/** A class to manage opening/closing symbol pairs */
public class SymbolPairs implements ISymbolPairs {
    private final char[] oSymbols, cSymbols;

    /** Pass a string of openers, like "[{" to populate closers "]}"
     *  Passing a quote as opener gives the same quote as closer.
     *  For unusual symbols, use the char array constructor version.
     * @param openerList All desired opening symbols */
    public SymbolPairs(String openerList) {
        int len = openerList.length();
        oSymbols = new char[len];
        cSymbols = new char[len];
        for(int i = 0; i < len; i++){
            oSymbols[i] = openerList.charAt(i);
            cSymbols[i] = this.mapOpenToClose(openerList.charAt(i));
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
