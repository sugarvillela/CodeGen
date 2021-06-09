package generictree.impl;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.ISteadyPathTree;
import langdef.LangConstants;
import runstate.Glob;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SteadyPathTree <T> implements ISteadyPathTree <T> {
    protected final PathTree<T> pathTree;
    protected final String pathTreeSep;
    protected final Stack<Integer> restoreStack;
    protected List<String> steadyPath;

    public SteadyPathTree(char splitChar) {
        pathTree = new PathTree<>(splitChar);
        pathTreeSep = String.valueOf(LangConstants.PATH_TREE_SEP);
        restoreStack = new Stack<>();
        steadyPath = new ArrayList<>();
    }

    @Override
    public IGTreeNode<T> getRoot() {
        return pathTree.getRoot();
    }

    @Override
    public IGTreeNode<T> getLastAdded() {
        return pathTree.getLastAdded();
    }

    @Override
    public IGTreeNode<T> addBranch(T payload, String identifier) {
        steadyPath.add(identifier);
        return pathTree.add(payload, steadyPath);
    }

    @Override
    public IGTreeNode<T> addLeaf(T payload, String identifier) {
        steadyPath.add(identifier);
        IGTreeNode<T> added = pathTree.add(payload, steadyPath);
        this.pathBack();
        return added;
    }

    @Override
    public void clear() {
        pathTree.clear();
        steadyPath.clear();
    }

    @Override
    public void finalizeTree() {}

    @Override
    public void display() {
        this.display(pathTree.getRoot());
    }

    private void display(IGTreeNode<T> root) {
        System.out.println(root.csvString());
        for(IGTreeNode<T> child : root.getChildren()){
            display(child);
        }
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
    public void pathBackTo(int newLast) {
        steadyPath = steadyPath.subList(0, newLast + 1);
    }

    @Override
    public void pathBack() {
        steadyPath.remove(steadyPath.size() - 1);
    }

    @Override
    public void pathBack(int n) {
        steadyPath = steadyPath.subList(0, steadyPath.size() - n);
    }

    @Override
    public void pathBack(String identifier) {
        for(int i = steadyPath.size() - 1; i >= 0; i--){
            if(steadyPath.get(i).equals(identifier)){
                steadyPath = steadyPath.subList(0, i + 1);
                break;
            }
        }
    }

    @Override
    public <E extends Enum<E>> void pathBack(E enu) {
        Glob.ERR_DEV.kill("not implemented");
    }

    @Override
    public void A_() {

        restoreStack.push(steadyPath.size() - 1);
        //statusPush();
    }

    @Override
    public void V_() {
        if(!restoreStack.isEmpty()){
            //statusPop();
            int n = restoreStack.pop();
            steadyPath = steadyPath.subList(0, n + 1);

        }
    }

    protected void statusPush(){
        String last = (getLastAdded() == null)? "empty" : getLastAdded().identifier();
        System.out.println(restoreStack.size() + " + " + last);
    }

    protected void statusPop(){
        String last = (getLastAdded() == null)? "empty" : getLastAdded().identifier();
        System.out.println(restoreStack.size() + " - " + last);
    }

    @Override
    public List<String> pathAsList() {
        return steadyPath;
    }

    @Override
    public String[] pathAsArray() {
        return steadyPath.toArray(new String[steadyPath.size()]);
    }

    @Override
    public String pathAsString() {
        return String.join(pathTreeSep, steadyPath);
    }
}
