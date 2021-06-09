package langformat.impl;

import langformat.iface.IFormatStrategy;
import langformat.iface.IFormatter;
import tokenizer.iface.ISplitUtil;
import tokenizer.iface.ITokenizer;

import java.util.ArrayList;
import java.util.List;

public class Formatter implements IFormatter {
    private static Formatter instance;

    public static Formatter initInstance(IFormatStrategy formatStrategy){
        return (instance == null)? (instance = new Formatter(formatStrategy)): instance;
    }
    public static Formatter getInstance(){
        return instance;
    }

    private final ITokenizer tokenizer;
    private final IFormatStrategy formatStrategy;
    private final ISplitUtil splitUtil;
    protected int indent, tab, margin;

    private Formatter(IFormatStrategy formatStrategy){
        this.margin = 70;
        this.tab = 4;
        indent = 0;

        this.formatStrategy = formatStrategy;
        tokenizer = formatStrategy.getTokenizer();
        splitUtil = formatStrategy.getSplitUtil(margin);
    }

    @Override
    public String formatAll(String text) {
        List<String> tok = tokenizer.setText(text).parse().toList();
        List<String> content = new ArrayList<>();
        for(String line : tok){
            int incBy = formatStrategy.checkLine(line);
            if(incBy > 0){
                this.formatLine(content, line);
                this.inc(incBy);
            }
            else if(incBy < 0){
                this.inc(incBy);
                this.formatLine(content, line);
            }
            else{
                this.formatLine(content, line);
            }
        }
        return String.join("\n", content);
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

        text = tab(text);
        String[] pair = splitUtil.split(text);
        content.add(pair[0]);

        if(pair[1] == null){
            return;
        }

        indent++;
        while(pair[1] != null){
            text = tab(pair[1]);
            pair = splitUtil.split(text);
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
            pair = splitUtil.split(text);
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
            pair = splitUtil.split(text);
            addLine(code, pair[0]);
            text = pair[1];
        }
        addLine(code, tab(after));
        splitUtil.setStartPos(margin);
    }
}
