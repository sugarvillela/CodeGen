package generictree.iface;

import java.util.List;

public interface ISteadyPathTree<T> {
    IGTreeNode <T> getRoot();
    IGTreeNode <T> getLastAdded();
    IGTreeNode <T> addBranch(T payload, String identifier);
    IGTreeNode <T> addLeaf(T payload, String identifier);
    void clear();
    void finalizeTree();
    void display();

    IGTreeParse<T> getParse();

    void pathClear();
    void pathBackTo(int newLast);
    void pathBack();
    void pathBack(int n);
    void pathBack(String identifier);
    <E extends Enum<E>> void pathBack(E enu);

    void A_();// A_()
    void V_();   // V_()

    List<String> pathAsList();
    String[] pathAsArray();
    String pathAsString();
}
