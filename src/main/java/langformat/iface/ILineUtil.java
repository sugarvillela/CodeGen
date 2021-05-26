package langformat.iface;

public interface ILineUtil {
    ILineUtil prepare();
    ILineUtil setHeader(String header);
    ILineUtil appendLine(String text);
    ILineUtil setFooter(String footer);
    void toCsv();
    void toSsv();
}
