package langformat.iface;

import static langformat.enu.CONTROL_ENTITIES.*;

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
        code.append(this.trimBack(text));
        code.append(";\n");
    }

    default void addBlank(StringBuilder code){
        code.append(BLANK_.entity());
    }

    default void addInc(StringBuilder code){
        code.append(INC_.entity());
    }

    default void addDec(StringBuilder code){
        code.append(DEC_.entity());
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
