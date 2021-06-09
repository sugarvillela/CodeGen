package tokenizer.impl;

import tokenizer.iface.IMatchUtil;

public class MatchUtil implements IMatchUtil {
    private final String targetText;
    private int iStart, iFinish, k;

    public MatchUtil(String targetText) {
        this.targetText = targetText;
        this.k = 0;
    }

    @Override
    public boolean haveMatch(int i, char curr) {
        if(targetText.charAt(k) == curr){
            k++;
            if(k == 1){
                iStart = i;
            }
            else if(k == targetText.length()){
                iFinish = i + 1;
                k = 0;
                return true;
            }
        }
        else{
            k = 0;
        }
        return false;
    }

    @Override
    public int indexStart() {
        return iStart;
    }

    @Override
    public int indexFinish() {
        return iFinish;
    }
}
