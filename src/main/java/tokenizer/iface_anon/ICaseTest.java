package tokenizer.iface_anon;

/** No formal implementation; implement anonymously.
 *  For making char match case-sensitive or insensitive */
public interface ICaseTest {

    /**Assume impl will be within variable scope of a 'haystack' string.
     * Match a char at index
     * @param c Character to compare
     * @param index position in string to look
     * @return true if equal */
    default boolean isMatch(char c, int index) {
        return false;
    }

    /**Assume impl will be within variable scope of a 'haystack' string.
     * Search for char in string
     * @param c Character to find
     * @return true if c exists in string */
    default boolean contains(char c) {
        return false;
    }

    /**Make lowercase upper or uppercase lower
     * @return opposite case of char */
    default char swapCase(char c) {
        return Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c);
    }
}
