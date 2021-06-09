package tokenizer.impl;

import tokenizer.iface.IMatchUtil;

public class MatchUtilNop implements IMatchUtil {

    @Override
    public boolean haveMatch(int i, char curr) {
        return false;
    }

    @Override
    public int indexStart() {
        return 0;
    }

    @Override
    public int indexFinish() {
        return 0;
    }
}
