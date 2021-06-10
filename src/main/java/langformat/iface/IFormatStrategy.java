package langformat.iface;

import tokenizer.iface.ISplitUtil;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.SplitUtil;
import tokenizer.impl.Tokenizer;

public interface IFormatStrategy {
    int checkLine(String text);
    ITokenizer getTokenizer();
    ISplitUtil getSplitUtil(int margin);
}
