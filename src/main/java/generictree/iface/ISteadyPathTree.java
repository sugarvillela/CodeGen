package generictree.iface;

public interface ISteadyPathTree<T> {
    IGTreeNode <T> getRoot();
    boolean put(T payload, String identifier);
    void clear();

    IGTreeParse<T> getParse();

    void pathClear();
    void pathSetLast(int newLast);
    void pathBack();
    void pathBack(int n);
    void pathBack(String s);

    void setRestore();
    void restorePath();

    String[] currPath();
    String pathString();
}
