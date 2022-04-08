package tokenizer.iface;

import tokenizer.util_iface.ISymbolPairs;

import java.util.List;

/**A string parsing interface that applies 'skip areas' when parsing
 * Define a skip area by its symbols (usually quotes or brackets).
 * Parser skips these areas.
 * Option to keep or discard delimiters, ignore some text or limit number of actions.
 * Use builder-style setters to set options.
 *
 * Notes:
 * There are two implementation stacks
 *   1.  BaseStringParser and its inheritors (CharMatch, WordMatch, PairMatch)
 *       Handle basic parse and find for single char, substring and pairs of chars.
 *       Populate a "hit map" list with findings for composites to use
 *   1.  BaseComposite and its inheritors
 *       Assembles*/
public interface IStringParser {

    /*=====Set up=====================================================================================================*/

    /** Input text (haystack string to be parsed) */
    IStringParser setText(String text);

    /** Whole word or list of characters that causes an action */
    IStringParser setDelimiter(String delimiters);

    /** Should parse all but make no changes within skip area.
     * Set opening symbol; common closing symbols automatically matched.
     * See class SymbolPairs constructors */
    IStringParser setSkipSymbols(String openingSymbols);
    IStringParser setSkipSymbols(char oneOpeningSymbol);
    IStringParser setSkipSymbols(char openingSymbol, char closingSymbol);
    IStringParser setSkipSymbols(char[] oMap, char[] cMap);
    IStringParser setSkipSymbols(ISymbolPairs symbolPairs);

    /** Should parse all but make no changes before start index reached */
    IStringParser setStartPos(int startPos);

    /** Should halt after limit is reached */
    IStringParser setLimit(int limit);

    /** Pass true for case-sensitive char matching, false for case-insensitive */
    IStringParser setCaseSensitive(boolean caseSensitive);

    /**Tokenizer discards delimiters by default
     * Setting tokenizeDelimiter causes each delimiter to be written to
     * its own element (repeated delimiters are not ignored) */
    IStringParser setTokenizeDelimiter(boolean tokenize);

    /**Same as setTokenizeDelimiter() except repeated delimiters are ignored
     * (e.g. three identical adjacent delimiters become one element */
    IStringParser setTokenizeDelimiterOnce(boolean tokenizeOnce);

    /*=====Run========================================================================================================*/

    /**Clears state and runs core task */
    IStringParser parse();

    /*=====State after run============================================================================================*/

    /** Relevant if there is a tokenized string result */
    List<String> toList();
    String[] toArray();

    /** Relevant if there is a numeric result (see implementations) */
    int numeric();

    /** Relevant if there is a multiple numeric result (see implementations) */
    List<Integer> numericToList();
    int[] numericToArray();

    /** Relevant if input text altered */
    String getText();

    /** Relevant if text is checked for errors (only PairMatch implementation) */
    boolean isError();

    /*=====Getters for composite impl=================================================================================*/

    boolean isTokenizeDelimiter();
    boolean isDelimiterOnce();
    int getLimit();
    int getStartPos();
    ISymbolPairs getSymbolPairs();
    IStringParser getCompositeImpl();

    /*=====Setters for composite impl=================================================================================*/

    void setNumericArray(int[] numericArray);
}
