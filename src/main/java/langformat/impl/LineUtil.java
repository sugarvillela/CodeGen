package langformat.impl;

import langformat.iface.IFormatUtil;
import langformat.iface.ILineUtil;

import java.util.ArrayList;
import java.util.List;

public class LineUtil implements ILineUtil {
    private final List<String> line;
    private String header, footer;
    private final IFormatUtil formatUtil;

    public LineUtil(IFormatUtil formatUtil) {
        this.formatUtil = formatUtil;
        line = new ArrayList<>();
    }

    @Override
    public ILineUtil prepare() {
        line.clear();
        header = "";
        footer = "";
        return this;
    }

    @Override
    public ILineUtil setHeader(String header) {
        this.header = header;
        return this;
    }

    @Override
    public ILineUtil appendLine(String text) {
        line.add(text);
        return this;
    }

    @Override
    public ILineUtil setFooter(String footer) {
        this.footer = footer;
        return this;
    }

    @Override
    public void toCsv() {
        formatUtil.addLine(header + String.join(", ", line) + footer);
    }

    @Override
    public void toSsv() {
        formatUtil.addLine(header + String.join(" ", line) + footer);
    }
}
