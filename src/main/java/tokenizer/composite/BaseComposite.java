package tokenizer.composite;

import tokenizer.iface.IStringParser;
import tokenizer.util_iface.ISymbolPairs;

import java.util.List;

public abstract class BaseComposite implements IStringParser {
    protected final IStringParser parser;

    protected BaseComposite(IStringParser parser) {
        this.parser = parser;
    }

    @Override
    public IStringParser setText(String text) {
        parser.setText(text);
        return this;
    }

    @Override
    public IStringParser setDelimiter(String delimiters) {
        parser.setDelimiter(delimiters);
        return this;
    }

    @Override
    public IStringParser setSkipSymbols(String openingSymbols) {
        parser.setSkipSymbols(openingSymbols);
        return this;
    }

    @Override
    public IStringParser setSkipSymbols(char oneOpeningSymbol) {
        parser.setSkipSymbols(oneOpeningSymbol);
        return this;
    }

    @Override
    public IStringParser setSkipSymbols(char openingSymbol, char closingSymbol) {
        parser.setSkipSymbols(openingSymbol, closingSymbol);
        return this;
    }

    @Override
    public IStringParser setSkipSymbols(char[] oMap, char[] cMap) {
        parser.setSkipSymbols(oMap, cMap);
        return this;
    }

    @Override
    public IStringParser setSkipSymbols(ISymbolPairs symbolPairs) {
        parser.setSkipSymbols(symbolPairs);
        return this;
    }

    @Override
    public IStringParser setStartPos(int startPos) {
        parser.setStartPos(startPos);
        return this;
    }

    @Override
    public IStringParser setLimit(int limit) {
        parser.setLimit(limit);
        return this;
    }

    @Override
    public IStringParser setCaseSensitive(boolean caseSensitive) {
        parser.setCaseSensitive(caseSensitive);
        return this;
    }

    @Override
    public IStringParser setTokenizeDelimiter(boolean tokenize) {
        parser.setTokenizeDelimiter(tokenize);
        return this;
    }

    @Override
    public IStringParser setTokenizeDelimiterOnce(boolean tokenizeOnce) {
        parser.setTokenizeDelimiterOnce(tokenizeOnce);
        return this;
    }

    @Override
    public IStringParser parse() {
        parser.parse();
        return this;
    }

    @Override
    public int numeric() {
        return parser.numeric();
    }

    @Override
    public List<String> toList() {
        return parser.toList();
    }

    @Override
    public String[] toArray() {
        return parser.toArray();
    }

    @Override
    public List<Integer> numericToList() {
        return parser.numericToList();
    }

    @Override
    public int[] numericToArray() {
        return parser.numericToArray();
    }

    @Override
    public String getText() {
        return parser.getText();
    }

    @Override
    public boolean isError() {
        return parser.isError();
    }

    /*=====Getters for composite impl=================================================================================*/

    @Override
    public boolean isTokenizeDelimiter() {
        return parser.isTokenizeDelimiter();
    }

    @Override
    public boolean isDelimiterOnce() {
        return parser.isDelimiterOnce();
    }

    @Override
    public int getLimit() {
        return parser.getLimit();
    }

    @Override
    public int getStartPos() {
        return parser.getStartPos();
    }

    @Override
    public ISymbolPairs getSymbolPairs() {
        return parser.getSymbolPairs();
    }

    @Override
    public IStringParser getCompositeImpl() {
        return parser;
    }

    /*=====Setters for composite impl=================================================================================*/

    @Override
    public void setNumericArray(int[] numericArray) {
        parser.setNumericArray(numericArray);
    }
}
