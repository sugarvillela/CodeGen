package generictree.iface;

import java.util.List;

public interface IGTreeParse <T> {
    IGTreeNode<T> treeNodeFromId(IGTreeNode<T> root, String identifier);

    /*====PathTree algorithms: path as array==========================================================================*/

    IGTreeNode<T> treeNodeFromPath(IGTreeNode<T> root, String... path);

    IGTreeNode<T> treeNodeFromPartialPath(IGTreeNode<T> root, String... partialPath);

    boolean isPathToLeaf(IGTreeNode<T> root, String... partialPath);

    IGTreeNode<T> addByPath(T payload, IGTreeNode<T> root, String... path);

    String[] pathFromPartialPath(IGTreeNode<T> root, String... partialPath);

    /*====PathTree algorithms: path as list===========================================================================*/

    IGTreeNode<T> treeNodeFromPath(IGTreeNode<T> root, List<String> path);

    IGTreeNode<T> treeNodeFromPartialPath(IGTreeNode<T> root, List<String> partialPath);

    boolean isPathToLeaf(IGTreeNode<T> root, List<String> partialPath);

    IGTreeNode<T> addByPath(T payload, IGTreeNode<T> root, List<String> path);

    String[] pathFromPartialPath(IGTreeNode<T> root, List<String> partialPath);

    /*====PathTree algorithms: find path==============================================================================*/

    String[] pathFromTreeNode(IGTreeNode<T> root, IGTreeNode<T> treeNode);

    List<String> getAllPaths(IGTreeNode<T> root, char pathSep);

    /*====General tree parse algorithms===============================================================================*/

    boolean preOrder(IGTreeNode<T> root, IGTreeTask<T> task);

    boolean postOrder(IGTreeNode<T> root, IGTreeTask<T> task);

    boolean breadthFirst(IGTreeNode<T> root, IGTreeTask<T> task);

}
