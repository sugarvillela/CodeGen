package langformat.iface;

import tokenizer.iface.ISplitUtil;
import tokenizer.iface.ITokenizer;

public interface IFormatStrategy {
    int checkLine(String text);
    ITokenizer getTokenizer();
    ISplitUtil getSplitUtil(int margin);
}
