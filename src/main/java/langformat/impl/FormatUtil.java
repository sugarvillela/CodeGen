package langformat.impl;

import langformat.iface.IFormatUtil;
import langgen.interfaces.IWidget;
import tokenizer.iface.ISplitUtil;
import tokenizer.impl.SplitUtil;

import java.util.ArrayList;
import java.util.List;

public class FormatUtil implements IFormatUtil {
    private final List<String> content;
    private final ISplitUtil splitUtil;
    protected int indent, tab, margin;

    public FormatUtil() {
        this(70, 4);
    }

    public FormatUtil(int margin, int tab) {
        this.content = new ArrayList<>();
        this.margin = margin;
        this.tab = tab;
        indent = 0;
        splitUtil = new SplitUtil("\"").setStartPos(margin);
    }

    @Override
    public final String tab(String text){
        return new String(new char[indent * tab]).replace('\0', ' ') + text;
    }

    @Override
    public final void clear(){this.indent = 0;}

    @Override
    public final void inc(){this.indent++;}

    @Override
    public final void dec(){
        indent = Math.max(0, indent - 1);
    }

    @Override
    public final void add(String text){
        this.content.add( text );
    }

    @Override
    public final void addLine(String text){
        while(text != null){
            text = tab(text);
            String[] pair = splitUtil.split(text);
            this.content.add(pair[0]);
            text = pair[1];
        }
    }

    @Override
    public final void addLines(ArrayList<IWidget> widgets){
        for(IWidget widget : widgets){
            widget.finish(this);
        }
    }

    @Override
    public final void addTabLines(ArrayList<IWidget> widgets){
        inc();
        for(IWidget widget : widgets){
            widget.finish(this);
        }
        dec();
    }

    @Override
    public List<String> getContent(){
        return content;
    }
}
