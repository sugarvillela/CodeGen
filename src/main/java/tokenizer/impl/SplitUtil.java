package tokenizer.impl;

import tokenizer.iface.ISplitUtil;
import tokenizer.iface.IWhitespace;

import java.util.Stack;

public class SplitUtil implements ISplitUtil {
    private static final char escape = '\\';
    private IWhitespace whitespace;
    private String delimiters;              // input text, list of delimiters text
    private int startPos;                   // start parsing here if set; else 0
    private char[] oMap, cMap;              // matched open/close skip char arrays
    private Stack<Character> cSymbols;      // Closing symbol during skip
    private boolean keepSkipSymbol;         // obey skip symbols and leave in (default out)
    private boolean keepEscapeSymbol;       // obey escape symbol and leave in for later processing
    private String text;

    public SplitUtil(){
        keepSkipSymbol = true;
        keepEscapeSymbol = true;
    }
    public SplitUtil(String skipSymbol){
        keepSkipSymbol = true;
        keepEscapeSymbol = true;
        setDelimiter(' ');
        setMap(skipSymbol);
    }

    @Override
    public ISplitUtil setStartPos(int startPos) {
        this.startPos = startPos;
        return this;
    }

    @Override
    public String[] split(String text) {
        if(startPos >= text.length()){
            return new String[]{text, null};
        }
        cSymbols = new Stack<>();
        String[] pair = new String[2];
        int i = 0, j = 0, k = 0, len = text.length();
        boolean escaped = false;
        for (i = 0; i < len && k < 2; i++) {
            char curr = text.charAt(i);
            if(isEscape(text.charAt(i))){
                escaped = true;
                if(!keepEscapeSymbol){
                    text = text.substring(0, i) + text.substring(i + 1);
                    len--;
                    i--;
                }
            }
            else if(escaped){
                escaped = false;
            }
            else{
                if(inSkipArea()){
                    if(leaveSkipArea(curr) || enterSkipArea(curr)){}
                }
                else if(!enterSkipArea(curr) && i >= startPos && isDelimiter(curr)){
                    if(haveText(i, j)){
                        if(k > 0){
                            pair[k] = text.substring(j);
                            return pair;
                        }
                        else{
                            pair[k++] = text.substring(j, i);
                        }
                    }
                    j = i + 1;
                }
            }
        }

        if(k < 2 && haveText(i, j)){
            pair[k] = (text.substring(j));
        }
        return pair;
    }

    private ISplitUtil setDelimiter(char... delimiter) {
        this.delimiters = new String(delimiter);
        if(this.delimiters.contains(" ")){
            whitespace = new IWhitespace() {
                @Override
                public boolean isWhitespace(char symbol) {
                    return ((int)symbol) < 33;
                }
            };
        }
        else{
            whitespace = new IWhitespace() {
                @Override
                public boolean isWhitespace(char symbol) {
                    return false;
                }
            };
        }
        return this;
    }

    private void setMap(String skips){
        // map openers to closers, using symbols from arg
        // if you want different symbols, pass arrays with Builder
        oMap =  new char[skips.length()];
        cMap =  new char[skips.length()];
        char[] openers = new char[]{'"','\'', '(','{','[','<'};
        char[] closers = new char[]{'"','\'', ')','}',']','>'};
        int to = 0;
        for (int i = 0; i < openers.length; i++) {
            if(skips.indexOf(openers[i])!=-1){
                oMap[to]=openers[i];
                cMap[to]=closers[i];
                to++;
            }
        }
    }

    private boolean isEscape(char symbol){
        return symbol == escape;
    }

    private boolean isDelimiter(char symb){
        return delimiters.indexOf(symb) != -1 || whitespace.isWhitespace(symb);
    }

    private boolean haveText(int i, int j){
        return i != j;
    }

    private boolean enterSkipArea(char symbol){
        for(int i=0; i<oMap.length; i++){
            if(symbol == oMap[i]){
                this.cSymbols.push(cMap[i]);// important side effect
                return true;
            }
        }
        return false;
    }

    private boolean inSkipArea(){
        return !cSymbols.isEmpty();
    }

    private boolean leaveSkipArea(char symbol){
        if(cSymbols.peek().equals(symbol)){
            cSymbols.pop();
            return true;
        }
        return false;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder implements ISplitUtil.Builder {
        SplitUtil built;

        private Builder(){
            built = new SplitUtil();
        }

        @Override
        public Builder delimiters(char... delimiter) {
            built.setDelimiter(delimiter);
            return this;
        }

        @Override
        public Builder skipSymbols(String openingSymbols) {
            built.setMap(openingSymbols);
            return this;
        }

        @Override
        public Builder keepEscapeSymbol(boolean keep) {
            built.keepEscapeSymbol = keep;
            return this;
        }

        @Override
        public SplitUtil build() {
            if(built.oMap == null){
                built.oMap = new char[0];
            }
            if(built.delimiters == null){
                built.setDelimiter(' ');
            }
            return built;
        }
    }
}
