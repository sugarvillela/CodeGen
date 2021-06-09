package tokenizer.iface;

/** A simple string match for finding patterns during char-by-char iteration */
public interface IMatchUtil {
    boolean haveMatch(int i, char curr);
    int indexStart();
    int indexFinish();
}
