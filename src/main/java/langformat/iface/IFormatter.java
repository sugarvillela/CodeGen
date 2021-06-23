package langformat.iface;

import static langformat.enu.CONTROL_ENTITIES.*;

public interface IFormatter {
    default String trimBack(String text) {
        if(text.isEmpty()){
            return text;
        }
        int len = text.length();
        for (; len > 0; len--) {
            if (!Character.isWhitespace(text.charAt(len - 1)))
                break;
        }
        return text.substring(0, len);
    }

    default void add(StringBuilder code, String text){
        if(!text.isEmpty()){
            code.append(text);
        }
    }
    default void addWord(StringBuilder code, String text){
        text = this.trimBack(text);
        this.add(code, text);
    }

    default void addWord_(StringBuilder code, String text){
        this.addWord(code, text);
        code.append(' ');
    }

    default void addLine(StringBuilder code, String text){
        this.addWord(code, text);
        code.append(N_.entity());
    }

    default void addStatement(StringBuilder code, String text){
        this.addWord(code, text);
        code.append(";").append(N_.entity());
    }

    default void addBlank(StringBuilder code){
        code.append(B_.entity());
    }

    default void addInc(StringBuilder code){
        code.append(I_.entity());
    }

    default void addDec(StringBuilder code){
        code.append(D_.entity());
    }

    String formatAll(String text);

    void addComment(StringBuilder code, String text, String commentSymbol);
    void addComment(StringBuilder code, String text, String before, String during, String after);
}
