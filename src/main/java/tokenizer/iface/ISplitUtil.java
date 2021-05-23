package tokenizer.iface;

public interface ISplitUtil {
    ISplitUtil setStartPos(int startPos);
    String[] split(String text);

    interface Builder{
        Builder delimiters(char... delimiter);
        Builder skipSymbols(String openingSymbols);
        Builder keepEscapeSymbol(boolean keep);
        ISplitUtil build();
    }
}
