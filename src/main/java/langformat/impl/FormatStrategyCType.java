package langformat.impl;

import langformat.iface.IFormatStrategy;
import tokenizer.iface.IStringParser;
import tokenizer.impl.CharMatch;

public class FormatStrategyCType implements IFormatStrategy {
    private final IStringParser incUtil, decUtil;

    public FormatStrategyCType() {
        incUtil = new CharMatch().setDelimiter("{").setSkipSymbols("'\"");
        decUtil = new CharMatch().setDelimiter("}").setSkipSymbols("'\"");
    }

    @Override
    public int checkLine(String text) {
        return incUtil.setText(text).parse().numeric() - decUtil.setText(text).parse().numeric();
    }
}
