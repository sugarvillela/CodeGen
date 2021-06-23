package tokenizer.composite2;

import tokenizer.composite.BaseComposite;
import tokenizer.composite.CharReplace;
import tokenizer.iface.IStringParser;

public class TabUtil extends BaseComposite {
    public TabUtil() {
        super(new CharReplace("    ").setDelimiter("\t"));
    }

    // override to prevent setting a different delimiter
    @Override
    public IStringParser setDelimiter(String delimiters) {
        return this;
    }
}
