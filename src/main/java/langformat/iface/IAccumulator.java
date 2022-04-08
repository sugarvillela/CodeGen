package langformat.iface;

/** Use as pre-formatter where non-space delimiter or other treatment is needed */
public interface IAccumulator {
    void add(String text);
    String finish();
}
