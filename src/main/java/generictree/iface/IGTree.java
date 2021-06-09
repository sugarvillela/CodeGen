package generictree.iface;

import java.util.List;

public interface IGTree <T>{
    IGTreeNode <T> getRoot();
    IGTreeNode <T> getLastAdded();

    IGTreeNode <T> add(T payload, String... path);
    IGTreeNode <T> add(T payload, List<String> path);
    void clear();

    IGTreeParse<T> getParse();
}
