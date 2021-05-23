package langformat.iface;

import langgen.interfaces.IWidget;

import java.util.ArrayList;
import java.util.List;

public interface IFormatUtil {
    /**Indents text to current indent level
     * @param text raw text, any length
     * @return text with indent prepended */
    String tab(String text);

    /**Set indent to 0 */
    void clear();

    /**Increase indent by 1 tab */
    void inc();

    /**Decrease indent by 1 tab */
    void dec();

    /** To add content as-is, no formatting or line splitting
     * @param text exact text, no indent */
    void add(String text);

    /**The primary way to add content; takes care of formatting and line splitting
     * @param text any length */
    void addLine(String text);

    /**Add multiple lines directly from IWidget object
     * @param widgets nested objects containing widgets or text */
    void addLines(ArrayList<IWidget> widgets);

    void addTabLines(ArrayList<IWidget> widgets);

    /** @return the final result of all operations: element = line */
    public List<String> getContent();
}
