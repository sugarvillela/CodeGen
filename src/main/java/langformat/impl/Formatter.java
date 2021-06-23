package langformat.impl;

import langformat.iface.IFormatStrategy;
import langformat.iface.IFormatter;
import tokenizer.composite.WordReplace;
import tokenizer.composite.WordTok;
import tokenizer.composite2.CharSplit;
import tokenizer.composite2.SpaceUtil;
import tokenizer.iface.IStringParser;

import java.util.ArrayList;
import java.util.List;

import static langformat.enu.CONTROL_ENTITIES.*;

public class Formatter implements IFormatter {
    private static Formatter instance;

    public static Formatter initInstance(IFormatStrategy formatStrategy){
        return (instance == null)? (instance = new Formatter(formatStrategy)): instance;
    }
    public static Formatter getInstance(){
        return instance;
    }

    private final IFormatStrategy formatStrategy;
    private final IStringParser tokenizer;
    private final IStringParser endLineReplace;
    private final IStringParser entityRemove;
    private final IStringParser splitUtil;
    private final IStringParser spaceUtil;
    protected int indent, tab, margin;

    private Formatter(IFormatStrategy formatStrategy){
        this.margin = 70;
        this.tab = 4;
        indent = 0;
        this.formatStrategy = formatStrategy;
        tokenizer = new WordTok().setDelimiter(N_.entity());
        endLineReplace = new WordReplace("\n");
        entityRemove = new WordReplace("");
        splitUtil = new CharSplit().setDelimiter(" ").setSkipSymbols("'\"").setStartPos(margin);
        spaceUtil = new SpaceUtil().setSkipSymbols(splitUtil.getSymbolPairs());
    }

    @Override
    public String formatAll(String text) {
        List<String> tok = tokenizer.setText(text).parse().toList();
        List<String> content = new ArrayList<>();

        for(String haystack : tok){
            // add blank lines if Control Entity
            if(endLineReplace.setText(haystack).setDelimiter(B_.entity()).parse().numeric() != 0){
                haystack = endLineReplace.getText();
            }

            if(!incByControlEntity(content, haystack) && !incByStrategy(content, haystack)){
                this.formatLine(content, haystack);
            }
        }
        return String.join("\n", content);
    }
    private boolean incByControlEntity(List<String> content, String haystack){
        // inc tab if Control Entity
        int origLen = haystack.length();
        int incBy = entityRemove.setText(haystack).setDelimiter(I_.entity()).parse().numeric();
        if(incBy != 0){
            haystack = entityRemove.getText();
        }

        // dec tab if Control Entity
        int decBy = entityRemove.setText(haystack).setDelimiter(D_.entity()).parse().numeric();
        if(decBy != 0){
            haystack = entityRemove.getText();
        }

        int incDec = incBy - decBy;

        if(incDec > 0){         // contains inc entity
            this.inc(incDec);
            this.formatLine(content, haystack);
            return true;
        }
        else if(incDec < 0){    // contains dec entity
            this.formatLine(content, haystack);
            this.inc(incDec);
            return true;
        }
        else if(haystack.length() != origLen){  // special case: inc, dec and no text: remove entities
            this.formatLine(content, haystack);
            return true;
        }
        return false;
    }
    private boolean incByStrategy(List<String> content, String haystack){
        // inc/dec tab if language-specific triggers
        int incBy = formatStrategy.checkLine(haystack);
        if(incBy > 0){
            this.formatLine(content, haystack);
            this.inc(incBy);
            return true;
        }
        else if(incBy < 0){
            this.inc(incBy);
            this.formatLine(content, haystack);
            return true;
        }
        return false;
    }

    private void inc(int incBy){
        indent = Math.max(0, indent + incBy);
    }

    private String tab(String text){
        return new String(new char[indent * tab]).replace('\0', ' ') + text;
    }

    private void formatLine(List<String> content, String text){
        if(text == null){
            return;
        }
        text = spaceUtil.setText(text).parse().getText();
        text = tab(text);
        String[] pair = splitUtil.setText(text).parse().toArray();
        content.add(pair[0]);

        if(pair[1] == null){
            return;
        }

        indent++;
        while(pair[1] != null){
            text = tab(pair[1]);
            pair = splitUtil.setText(text).parse().toArray();
            content.add(pair[0]);
        };
        indent--;
    }

    @Override
    public void addComment(StringBuilder code, String text, String commentSymbol){
        String[] pair;
        splitUtil.setStartPos((int)(margin*0.6));
        while(text != null){
            text = tab(commentSymbol + ' ' + text);
            pair = splitUtil.setText(text).parse().toArray();
            addLine(code, pair[0]);
            text = pair[1];
        }
        splitUtil.setStartPos(margin);
    }

    @Override
    public void addComment(StringBuilder code, String text, String before, String during, String after){
        String[] pair;
        splitUtil.setStartPos((int)(margin*0.6));
        addLine(code, tab(before));
        while(text != null){
            text = tab(during + ' ' + text);
            pair = splitUtil.setText(text).parse().toArray();
            addLine(code, pair[0]);
            text = pair[1];
        }
        addLine(code, tab(after));
        splitUtil.setStartPos(margin);
    }
}
