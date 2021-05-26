package langformat.iface;

public interface INullableUtil {
    boolean extractBoolean(String... values);
    String extractString(String... values);
    String[] extractStrings(String... values);
    String extractCsv(String... values);

    String statement(String text);
}
