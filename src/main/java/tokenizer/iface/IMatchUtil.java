package tokenizer.iface;

import java.util.List;

/** A simple string match for finding patterns during char-by-char iteration.+
 *  Skips quoted text.
 *  Also use to map multiple match points or remove all */
public interface IMatchUtil {
    // setup
    IMatchUtil setNeedle(String needle);
    IMatchUtil setHaystack(String haystack);
    IMatchUtil setDeleteNeedle(boolean deleteNeedle);
    IMatchUtil setMatchOnce(boolean matchOnce);

    // run
    IMatchUtil parse();
    boolean parseByChar(int i);// core method

    // state after run
    int numOccurs();
    boolean haveMatch();
    int getIndexStart();        // describes the last match found
    int getIndexFinish();       // describes the last match found
    List<Integer> getHitMap();  // all matches
    String getHaystack();       // different from input if deleteNeedle is true
}
