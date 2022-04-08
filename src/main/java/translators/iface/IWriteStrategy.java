package translators.iface;

/** The way to get formatted code out of TranslationCenter:
 *  pass a strategy to write, list, display or ignore */
public interface IWriteStrategy {

    /** Display, file, list, list with paths, nop.
     * @param formattedCode one file in formatted form
     * @param writePath ignored by all but WriteStrategyFile */
    void write(String formattedCode, String writePath);
}
