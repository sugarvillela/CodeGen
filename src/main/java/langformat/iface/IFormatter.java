package langformat.iface;

public interface IFormatter {
    default String trimBack(String text) {
        int len = text.length();
        for (; len > 0; len--) {
            if (!Character.isWhitespace(text.charAt(len - 1)))
                break;
        }
        return text.substring(0, len);
    }

    default void addStatement(StringBuilder code, String text){
        code.append(this.trimBack(text)).append(";\n");
    }

    default void addBlank(StringBuilder code){
        code.append('\n');
    }

    default void addLine(StringBuilder code, String text){
        code.append(this.trimBack(text)).append('\n');
    }

    default void addWord_(StringBuilder code, String text){
        code.append(this.trimBack(text)).append(' ');
    }

    default void addWord(StringBuilder code, String text){
        code.append(this.trimBack(text));
    }

    default void add(StringBuilder code, String text){
        code.append(text);
    }

    String formatAll(String text);

    void addComment(StringBuilder code, String text, String commentSymbol);
    void addComment(StringBuilder code, String text, String before, String during, String after);
}
