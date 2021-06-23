package tokenizer.impl;

import tokenizer.iface.IStringParser;
import tokenizer.util_iface.ICaseTest;
import tokenizer.util_iface.ISymbolPairs;
import tokenizer.util_iface.IWhitespaceTest;
import tokenizer.util_impl.SymbolPairs;

import java.util.List;
import java.util.Stack;

public abstract class BaseStringParser implements IStringParser {
    protected static final char escape = '\\';
    protected IWhitespaceTest whitespaceTest; // object for char matchers to test for whitespace
    protected ICaseTest caseTest;             // object for all matchers to test case sensitive or insensitive
    protected String delimiters;              // input text, list of delimiters text
    protected ISymbolPairs symbolPairs;       // matched open/close char arrays
    protected Stack<Character> skipStack;     // Closing symbol during skip
    protected boolean caseSensitive;          // affects match utils
    protected boolean tokenizeDelimiter;      // save delimiter to own element
    protected boolean delimiterOnce;          // save delimiter to own element, ignore duplicates
    protected int limit;                      // number of tokens allowed
    protected int startPos;
    protected String text;
    protected boolean escaped;                // state

    protected BaseStringParser(){
        skipStack = new Stack<>();
        tokenizeDelimiter = false;
        caseSensitive = true;
        limit = 0xFFFFFFF;
        this.initCaseTest();
    }

    /*====Set up======================================================================================================*/

    @Override
    public IStringParser setText(String text) {
        this.text = text;
        return this;
    }

    public IStringParser setSkipSymbols(String openingSymbols) {
        symbolPairs = new SymbolPairs(openingSymbols);
        return this;
    }

    @Override
    public IStringParser setSkipSymbols(char oneOpeningSymbol) {
        symbolPairs = new SymbolPairs(oneOpeningSymbol);
        return this;
    }

    @Override
    public IStringParser setSkipSymbols(char openingSymbol, char closingSymbol) {
        symbolPairs = new SymbolPairs(openingSymbol, closingSymbol);
        return this;
    }

    @Override
    public IStringParser setSkipSymbols(char[] oMap, char[] cMap) {
        symbolPairs = new SymbolPairs(oMap, cMap);
        return this;
    }

    @Override
    public IStringParser setSkipSymbols(ISymbolPairs symbolPairs) {
        this.symbolPairs = symbolPairs;
        return this;
    }

    @Override
    public IStringParser setStartPos(int startPos) {
        this.startPos = startPos;
        return this;
    }

    @Override
    public IStringParser setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public IStringParser setCaseSensitive(boolean caseSensitive){
        this.caseSensitive = caseSensitive;
        this.initCaseTest();
        return this;
    }

    @Override
    public IStringParser setTokenizeDelimiter(boolean tokenize) {
        this.tokenizeDelimiter = tokenize;
        return this;
    }

    @Override
    public IStringParser setTokenizeDelimiterOnce(boolean tokenizeOnce) {
        this.tokenizeDelimiter = true;
        this.delimiterOnce = tokenizeOnce;
        return this;
    }

    @Override
    public IStringParser setDelimiter(String delimiters) {
        this.delimiters = delimiters;
        this.initWhitespaceTest();
        return this;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public List<String> toList() {
        return null;
    }

    @Override
    public String[] toArray() {
        return null;
    }

    @Override
    public List<Integer> numericToList() {
        return null;
    }

    @Override
    public int[] numericToArray() {
        return null;
    }

    @Override
    public boolean isError(){
        return false;
    }

    /*====protected setup utils=======================================================================================*/

    protected abstract void initCaseTest();

    protected void initWhitespaceTest(){
        if(this.delimiters.contains(" ")){
            whitespaceTest = new IWhitespaceTest() {
                @Override
                public boolean eq(char symbol) {
                    return ((int)symbol) < 33;
                }
            };
        }
        else{
            whitespaceTest = new IWhitespaceTest() {};
        }
    }

    /*====protected utility methods===================================================================================*/

    protected boolean isEscape(char symbol){
        return symbol == escape;
    }

    protected boolean haveTextToAdd(int i, int j){
        return  i != j;
    }

    protected boolean tryPushSkipSymbol(char symbol){
        char[] oSymbols = symbolPairs.getOSymbols();
        for(int x = 0; x < oSymbols.length; x++){
            if(symbol == oSymbols[x]){
                this.skipStack.push(symbolPairs.getCSymbols()[x]);// important side effect
                return true;
            }
        }
        return false;
    }

    protected boolean isInSkipArea(){
        return !skipStack.isEmpty();
    }

    protected boolean tryPopSkipSymbol(char symbol){
        if(skipStack.peek().equals(symbol)){
            skipStack.pop();
            return true;
        }
        return false;
    }

    /*=====Getters for composite impl=================================================================================*/

    @Override
    public boolean isTokenizeDelimiter() {
        return tokenizeDelimiter;
    }

    @Override
    public boolean isDelimiterOnce() {
        return delimiterOnce;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public int getStartPos() {
        return startPos;
    }

    @Override
    public ISymbolPairs getSymbolPairs() {
        return symbolPairs;
    }

    @Override
    public IStringParser getCompositeImpl() {
        return null;
    }

    /*=====Setters for composite impl=================================================================================*/

    @Override
    public void setNumericArray(int[] numericArray) {}
}
