package langformat.iface;

import codedef.modifier.ENU_BOOLEAN;

/** A class for accessing nullable string arrays, with nulls and empties
 *  mapped to false or null
 *
 * Expectation for encoding to follow JSON rules in IErrCatch:
 * Numbers, booleans and strings are all encoded as strings, which are further
 * encoded as single-element String arrays.
 * Thus, String access involves passing an array and returning element 0.
 */
public interface INullableUtil {
    default boolean extractBoolean(String... values){
        return (
            values != null &&
            values.length > 0 &&
            ENU_BOOLEAN.isTrue(values[0])
        );
    }

    default String extractString(String... values){
        return (values != null && values.length > 0)?
                values[0] : null;
    }

    default String extractCsv(String... values){
        return (values != null && values.length > 0)?
                String.join(", ", values) : null;
    }
}
