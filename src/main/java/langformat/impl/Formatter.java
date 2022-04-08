package langformat.impl;

import langformat.iface.IFormatStrategy;
import langformat.iface.IFormatter;
import runstate.Glob;
import tokenizer.composite.WordReplace;
import tokenizer.composite.WordTok;
import tokenizer.composite2.CharSplit;
import tokenizer.composite2.SpaceUtil;
import tokenizer.iface.IStringParser;

import java.util.ArrayList;
import java.util.List;

import static langformat.enu.CONTROL_ENTITIES.*;

public class Formatter implements IFormatter {
    private static IFormatter instance;
    public static IFormatter initInstance(){
        return (instance == null)? (instance = new Formatter()): instance;
    }

    private static final int MARGIN = 70;
    private static final int TAB_DEFAULT = 4;

    private final IStringParser tokenizer;
    private final IStringParser endLineReplace;
    private final TabUtil tabUtil;
    private final IStringParser splitUtil;
    private final LineFormatter lineFormatter;

    private Formatter(){
        this.tokenizer = new WordTok().setDelimiter(N_.entity());
        this.endLineReplace = new WordReplace("\n");

        this.tabUtil = new TabUtil();
        this.splitUtil = new CharSplit().setDelimiter(" ").setSkipSymbols("'\"").setStartPos(MARGIN);
        this.lineFormatter = new LineFormatter(tabUtil, splitUtil);
    }

    @Override
    public void setTab(int tab) {
        tabUtil.setTab(tab);
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

            if(!lineFormatter.incByControlEntity(content, haystack) && !lineFormatter.incByStrategy(content, haystack)){
                lineFormatter.formatLine(content, haystack);
            }
        }
        return String.join("\n", content);
    }


    @Override
    public void addComment(StringBuilder code, String text, String commentSymbol){
        String[] pair;
        splitUtil.setStartPos((int)(MARGIN*0.6));
        while(text != null){
            text = tabUtil.prepend(commentSymbol + ' ' + text);
            pair = splitUtil.setText(text).parse().toArray();
            addLine(code, pair[0]);
            text = pair[1];
        }
        splitUtil.setStartPos(MARGIN);
    }

    @Override
    public void addComment(StringBuilder code, String text, String before, String during, String after){
        String[] pair;
        splitUtil.setStartPos((int)(MARGIN*0.6));
        addLine(code, tabUtil.prepend(before));
        while(text != null){
            text = tabUtil.prepend(during + ' ' + text);
            pair = splitUtil.setText(text).parse().toArray();
            addLine(code, pair[0]);
            text = pair[1];
        }
        addLine(code, tabUtil.prepend(after));
        splitUtil.setStartPos(MARGIN);
    }

    private static class TabUtil {
        private int indent, tab;

        TabUtil(){
            this.tab = TAB_DEFAULT;
            this.indent = 0;
        }
        public void setTab(int tab) {
            this.tab = tab;
        }
        private void inc(int incBy){
            indent = Math.max(0, indent + incBy);
        }
        private String prepend(String text){
            return new String(new char[indent * tab]).replace('\0', ' ') + text;
        }
    }

    private static class LineFormatter {
        private final TabUtil tabUtil;
        private final IStringParser splitUtil;
        private final IStringParser entityRemove;
        private final IStringParser spaceUtil;
        private final IFormatStrategy formatStrategy;

        private LineFormatter(TabUtil tabUtil, IStringParser splitUtil) {
            this.tabUtil = tabUtil;
            this.splitUtil = splitUtil;
            entityRemove = new WordReplace("");
            spaceUtil = new SpaceUtil().setSkipSymbols(splitUtil.getSymbolPairs());
            formatStrategy = Glob.TRANSLATION_CENTER.getFormatStrategy();
        }

        private void formatLine(List<String> content, String text){
            if(text == null){
                return;
            }
            text = spaceUtil.setText(text).parse().getText();
            text = tabUtil.prepend(text);
            String[] pair = splitUtil.setText(text).parse().toArray();
            content.add(pair[0]);

            if(pair[1] == null){
                return;
            }

            tabUtil.inc(1);
            while(pair[1] != null){
                text = tabUtil.prepend(pair[1]);
                pair = splitUtil.setText(text).parse().toArray();
                content.add(pair[0]);
            };
            tabUtil.inc(-1);
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
                tabUtil.inc(incDec);
                this.formatLine(content, haystack);
                return true;
            }
            else if(incDec < 0){    // contains dec entity
                this.formatLine(content, haystack);
                tabUtil.inc(incDec);
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
                tabUtil.inc(incBy);
                return true;
            }
            else if(incBy < 0){
                tabUtil.inc(incBy);
                this.formatLine(content, haystack);
                return true;
            }
            return false;
        }


    }
}
