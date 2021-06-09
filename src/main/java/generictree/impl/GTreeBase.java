package generictree.impl;

import generictree.iface.IGTree;
import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;
import generictree.parse.GTreeParse;
import generictree.task.TaskToList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GTreeBase <T> implements IGTree<T> {
    protected final IGTreeParse<T> parseObject;
    protected IGTreeNode<T> root, lastAdded;

    public GTreeBase() {
        parseObject = new GTreeParse<>();
    }

    @Override
    public IGTreeNode<T> getRoot() {
        return root;
    }

    @Override
    public IGTreeNode<T> getLastAdded() {
        return lastAdded;
    }

    @Override
    public void clear() {
        if(root != null){
            List<IGTreeNode<T>> list = new ArrayList<>();
            IGTreeTask<T> task = new TaskToList<T>(list);
            this.getParse().breadthFirst(this.getRoot(), task);
            for(int i = list.size() -1; i >= 0; i--){
                IGTreeNode<T> currNode = list.get(i);
                if(!currNode.isLeaf()){
                    currNode.getChildren().clear();
                }
            }
            root = null;
        }
    }

    @Override
    public IGTreeParse<T> getParse() {
        return parseObject;
    }

    protected String[] tokenizePathOnSingle(char splitChar, String... path){
        return path[0].split("[" + splitChar + "]");
    }
    protected List<String> tokenizePathOnSingle(char splitChar, List<String> path){
        return Arrays.asList(path.get(0).split("[" + splitChar + "]"));
    }
}
