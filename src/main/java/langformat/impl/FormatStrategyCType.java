package langformat.impl;

import langformat.iface.IFormatStrategy;
import tokenizer.iface.IMatchUtil;
import tokenizer.iface.ISplitUtil;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.MatchUtil;
import tokenizer.impl.SplitUtil;
import tokenizer.impl.Tokenizer;

import static langformat.enu.CONTROL_ENTITIES.*;

public class FormatStrategyCType implements IFormatStrategy {
    private final IMatchUtil matchUtil;

    public FormatStrategyCType() {
        matchUtil = new MatchUtil();
    }

    @Override
    public int checkLine(String text) {
        return matchUtil.setHaystack(text).
                setNeedle("{").parse().numOccurs()
                - matchUtil.setNeedle("}").parse().numOccurs();
    }

    @Override
    public ITokenizer getTokenizer() {
        return Tokenizer.builder().delimiters('\n').keepSkipSymbol().keepEscapeSymbol().skipSymbols("'\"").build();
    }

    @Override
    public ISplitUtil getSplitUtil(int margin) {
        return new SplitUtil("\"").setStartPos(margin);
    }
}
