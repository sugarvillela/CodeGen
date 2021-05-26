package langformat.impl;

import codedef.modifier.ENU_BOOLEAN;
import langformat.iface.INullableUtil;

public class NullableUtil implements INullableUtil {
    private static NullableUtil instance;

    public static NullableUtil initInstance(){
        return(instance ==  null)? (instance = new NullableUtil()) : instance;
    }

    private NullableUtil(){}

    @Override
    public boolean extractBoolean(String... values) {
        return (
            values != null &&
            values.length > 0 &&
            ENU_BOOLEAN.isTrue(values[0])
        );
    }

    @Override
    public String extractString(String... values) {
        return (values != null && values.length > 0)?
                values[0] : null;
    }

    @Override
    public String[] extractStrings(String... values) {
        return (values != null && values.length > 0)?
                values : null;
    }

    @Override
    public String extractCsv(String... values) {
        return (values != null && values.length > 0)?
                String.join(", ", values) : null;
    }

    @Override
    public String statement(String text) {
        return text + ';';
    }



}
