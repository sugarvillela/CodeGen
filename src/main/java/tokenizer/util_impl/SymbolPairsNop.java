package tokenizer.util_impl;

import tokenizer.util_iface.ISymbolPairs;

/** If symbol pairs are to be ignored, substitute this nop class.
 *  Returns empty array */
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
