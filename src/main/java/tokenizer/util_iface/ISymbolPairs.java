package tokenizer.util_iface;

/**Use to define skip areas. oSymbols and cSymbols must be equal size. */
public interface ISymbolPairs {
    char[] getOSymbols();
    char[] getCSymbols();
}
