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

    /**@param text any length
     * @param wrap false if you don't want text to wrap */
    void addLine(String text, boolean wrap);

    /**The primary way to addLine content; takes care of formatting and line splitting.
     * If text wraps, additional lines are indented.
     * @param text any length */
    void addLine(String text);

    ILineUtil lineUtil();

    /**Add multiple lines directly from IWidget object
     * @param widgets nested objects containing widgets or text */
    void addLines(ArrayList<IWidget> widgets);

    void addTabLines(ArrayList<IWidget> widgets);

    /** @return the final result of all operations: element = line */
    public List<String> getContent();
}
