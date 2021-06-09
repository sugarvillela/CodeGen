package langformat.iface;

public interface IAccumulator {
    void add(String text);
    String finish();
}
