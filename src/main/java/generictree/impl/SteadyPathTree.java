package generictree.impl;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.ISteadyPathTree;
import langdef.LangConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SteadyPathTree <T> implements ISteadyPathTree <T> {
    protected final PathTree<T> pathTree;
    protected final List<String> steadyPath;
    protected final String pathTreeSep;
    protected int restorePoint;

    public SteadyPathTree(char splitChar) {
        pathTree = new PathTree<>(splitChar);
        steadyPath = new ArrayList<>();
        pathTreeSep = String.valueOf(LangConstants.PATH_TREE_SEP);
    }

    @Override
    public IGTreeNode<T> getRoot() {
        return pathTree.getRoot();
    }

    @Override
    public boolean put(T payload, String identifier) {
        System.out.println("Put: " + identifier);
        steadyPath.add(identifier);
        String[] currPath = this.currPath();

        return pathTree.put(payload, this.currPath());
    }

    @Override
    public void clear() {
        pathTree.clear();
        steadyPath.clear();
    }

    @Override
    public IGTreeParse<T> getParse() {
        return pathTree.getParse();
    }

    @Override
    public void pathClear() {
        steadyPath.clear();
    }

    @Override
    public void pathSetLast(int newLast) {
        int last = steadyPath.size() - 1;
        while(!steadyPath.isEmpty() && last > newLast){
            steadyPath.remove(last);
            last --;
        }
    }

    @Override
    public void pathBack() {
        this.pathBack(1);
    }

    @Override
    public void pathBack(int n) {
        int last = steadyPath.size() - 1;
        while(!steadyPath.isEmpty() && n > 0){
            steadyPath.remove(last);
            n--;
            last --;
        }
    }

    @Override
    public void pathBack(String s) {
        int last = steadyPath.size() - 1;
        while(!steadyPath.isEmpty() && !steadyPath.get(last).equals(s)){
            steadyPath.remove(last);
            last --;
        }
    }

    @Override
    public void setRestore() {
        restorePoint = steadyPath.size() - 1;
    }

    @Override
    public void restorePath() {
        int last = steadyPath.size() - 1;
        while(!steadyPath.isEmpty() && last != restorePoint){
            steadyPath.remove(last);
            last --;
        }
    }

    @Override
    public String[] currPath() {
        String[] pathAsArray = steadyPath.toArray(new String[steadyPath.size()]);
        System.out.println("steadyPath: " + Arrays.toString(pathAsArray));
        return pathAsArray;
    }

    @Override
    public String pathString() {
        return String.join(pathTreeSep, steadyPath);
    }
}
