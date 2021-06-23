package tokenizer.util_impl;

import tokenizer.util_iface.ISymbolPairs;

public class SymbolPairsNop implements ISymbolPairs {
    private final char[] mockPairs;

    public SymbolPairsNop() {
        mockPairs = new char[0];
    }

    @Override
    public char[] getOSymbols() {
        return mockPairs;
    }

    @Override
    public char[] getCSymbols() {
        return mockPairs;
    }
}
