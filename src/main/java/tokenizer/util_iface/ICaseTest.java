package tokenizer.util_iface;

/**Use for making char match case-insensitive */
public interface ICaseTest {
    /**Implement anonymously with access to class field 'text'
     *
     * @param c Character to compare
     * @param index position in text field to look
     * @return true if equal
     */
    default boolean isMatch(char c, int index) {
        return false;
    }

    /**Implement anonymously with access to class field 'delimiters'
     *
     * @param c Character to find
     * @return true if c exists in delimiter field
     */
    default boolean contains(char c) {
        return false;
    }

    default char swapCase(char c) {
        return Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c);
    }
}
