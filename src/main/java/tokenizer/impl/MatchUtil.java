package tokenizer.impl;

import tokenizer.iface.IMatchUtil;

import java.util.ArrayList;
import java.util.List;

public class MatchUtil implements IMatchUtil {
    private static final char escape = '\\';
    private static final char SKIP_CHAR = '"';
    private final List<Integer> hitMap;
    private String needle, haystack;
    private int iStart, iFinish, k;
    private boolean deleteNeedle, matchOnce;    // user settings
    private boolean escaped, skip;              // algo states

    public MatchUtil() {
        hitMap = new ArrayList<>();
    }

    @Override
    public IMatchUtil setNeedle(String targetText) {
        this.needle = targetText;
        this.k = 0;
        return this;
    }

    @Override
    public IMatchUtil setHaystack(String haystack) {
        this.haystack = haystack;
        return this;
    }

    @Override
    public IMatchUtil setDeleteNeedle(boolean deleteNeedle) {
        this.deleteNeedle = deleteNeedle;
        return this;
    }

    @Override
    public IMatchUtil setMatchOnce(boolean matchOnce) {
        this.matchOnce = matchOnce;
        return this;
    }

    @Override
    public IMatchUtil parse() {
        hitMap.clear();
        skip = false;
        for(int i = 0; i < haystack.length(); i++){
            if(this.parseByChar(i) && matchOnce){
                break;
            }
        }
        if(deleteNeedle){
            for(int i = hitMap.size() -2; i >= 0; i -= 2){
                haystack = haystack.substring(0, hitMap.get(i)) + haystack.substring(hitMap.get(i + 1));
            }
        }
        return this;
    }

    @Override
    public boolean parseByChar(int i) {
        char curr = haystack.charAt(i);

        if(isEscape(curr)){
            escaped = true;
        }
        else if(escaped){
            escaped = false;
        }
        else if(curr == SKIP_CHAR){
            skip = !skip;
        }
        else if (!skip){
            if(needle.charAt(k) == curr){
                k++;
                if(k == 1){
                    iStart = i;
                }
                if(k == needle.length()){
                    iFinish = i + 1;
                    hitMap.add(iStart);     // start of match
                    hitMap.add(iFinish);    // next index after end of match
                    k = 0;
                    return true;
                }
            }
            else{
                k = 0;
            }
        }
        return false;
    }

    @Override
    public int numOccurs() {
        return hitMap.size()/2;
    }

    @Override
    public boolean haveMatch() {
        return !hitMap.isEmpty();
    }


    @Override
    public int getIndexStart() {
        return iStart;
    }

    @Override
    public int getIndexFinish() {
        return iFinish;
    }

    @Override
    public List<Integer> getHitMap() {
        return hitMap;
    }

    @Override
    public String getHaystack() {
        return haystack;
    }

    private boolean isEscape(char symbol){
        return symbol == escape;
    }
}
