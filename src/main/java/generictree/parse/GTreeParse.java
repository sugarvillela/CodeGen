package generictree.parse;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeParse;
import generictree.iface.IGTreeTask;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class GTreeParse<T> implements IGTreeParse<T> {
    @Override
    public IGTreeNode<T> treeNodeFromId(IGTreeNode<T> root, String identifier) {
        if(root.is(identifier)){
            return root;
        }
        else{
            IGTreeNode<T> foundNode;
            for(IGTreeNode<T> child : root.getChildren()){
                if((foundNode = treeNodeFromId(child, identifier)) != null){
                    return foundNode;
                }
            }
        }
        return null;
    }

    /*====PathTree algorithms: path as array==========================================================================*/

    @Override
    public IGTreeNode<T> treeNodeFromPath(IGTreeNode<T> root, String... path) {
        return treeNodeFromPath_recurse(0, root, path);
    }

    @Override
    public IGTreeNode<T> treeNodeFromPartialPath(IGTreeNode<T> root, String... partialPath) {
        return treeNodeFromPartialPath_recurse(0, root, partialPath);
    }

    @Override
    public boolean isPathToLeaf(IGTreeNode<T> root, String... partialPath) {
        IGTreeNode<T> found = this.treeNodeFromPartialPath_recurse(0, root, partialPath);
        return found != null && found.isLeaf();
    }

    @Override
    public IGTreeNode<T> addByPath(T payload, IGTreeNode<T> root, String... path) {
        return addByPath_recurse(payload, 0, root, path);
    }

    @Override
    public String[] pathFromPartialPath(IGTreeNode<T> root, String... partialPath) {
        IGTreeNode<T> found = this.treeNodeFromPartialPath_recurse(0, root, partialPath);
        return (found == null)? null : this.pathFromTreeNode(root, found);
    }

    private IGTreeNode<T> treeNodeFromPath_recurse(int index, IGTreeNode<T> root, String... path) {
        if(index < path.length && root.is(path[index])){
            if(index == path.length - 1){
                return root;
            }
            IGTreeNode<T> foundNode;
            for(IGTreeNode<T> child : root.getChildren()){
                if((foundNode = treeNodeFromPath_recurse(index + 1, child, path)) != null){
                    return foundNode;
                }
            }
        }
        return null;
    }
    private IGTreeNode<T> treeNodeFromPartialPath_recurse(int index, IGTreeNode<T> root, String... partialPath) {
        if(index < partialPath.length){
            if(root.is(partialPath[index])){
                index++;
                if(index == partialPath.length){
                    return root;
                }
            }
            IGTreeNode<T> foundNode;
            for(IGTreeNode<T> child : root.getChildren()){
                if((foundNode = treeNodeFromPartialPath_recurse(index, child, partialPath)) != null){
                    return foundNode;
                }
            }
        }
        return null;
    }
    private IGTreeNode<T> addByPath_recurse(T payload, int level, IGTreeNode<T> root, String... path) {
        if(level < path.length - 1){
            if(level == path.length - 2){
                if(root.is(path[level])){
                    return root.addChild(path[level + 1], payload);
                }
            }
            else{
                for(IGTreeNode<T> child : root.getChildren()){
                    if(child.is(path[level + 1])){
                        return addByPath_recurse(payload, level + 1, child, path);
                    }
                }
            }
        }
        return null;
    }

    /*====PathTree algorithms: path as list===========================================================================*/

    @Override
    public IGTreeNode<T> treeNodeFromPath(IGTreeNode<T> root, List<String> path) {
        return treeNodeFromPath_recurse(0, root, path);
    }

    @Override
    public IGTreeNode<T> treeNodeFromPartialPath(IGTreeNode<T> root, List<String> partialPath) {
        return treeNodeFromPartialPath_recurse(0, root, partialPath);
    }

    @Override
    public boolean isPathToLeaf(IGTreeNode<T> root, List<String> partialPath) {
        IGTreeNode<T> found = this.treeNodeFromPartialPath(root, partialPath);
        return found != null && found.isLeaf();
    }

    @Override
    public IGTreeNode<T> addByPath(T payload, IGTreeNode<T> root, List<String> path) {
        return addByPath_recurse(payload, 0, root, path);
    }

    @Override
    public String[] pathFromPartialPath(IGTreeNode<T> root, List<String> partialPath) {
        IGTreeNode<T> found = this.treeNodeFromPartialPath(root, partialPath);
        return (found == null)? null : this.pathFromTreeNode(root, found);
    }

    private IGTreeNode<T> treeNodeFromPath_recurse(int index, IGTreeNode<T> root, List<String> path) {
        if(index < path.size() && root.is(path.get(index))){
            if(index == path.size() - 1){
                return root;
            }
            IGTreeNode<T> foundNode;
            for(IGTreeNode<T> child : root.getChildren()){
                if((foundNode = treeNodeFromPath_recurse(index + 1, child, path)) != null){
                    return foundNode;
                }
            }
        }
        return null;
    }

    private IGTreeNode<T> treeNodeFromPartialPath_recurse(int index, IGTreeNode<T> root, List<String> partialPath) {
        if(index < partialPath.size()){
            if(root.is(partialPath.get(index))){
                index++;
                if(index == partialPath.size()){
                    return root;
                }
            }
            IGTreeNode<T> foundNode;
            for(IGTreeNode<T> child : root.getChildren()){
                if((foundNode = treeNodeFromPartialPath_recurse(index, child, partialPath)) != null){
                    return foundNode;
                }
            }
        }
        return null;
    }

    private IGTreeNode<T> addByPath_recurse(T payload, int level, IGTreeNode<T> root, List<String> path) {
        if(level < path.size() - 1){
            if(level == path.size() - 2){
                if(root.is(path.get(level))){
                    return root.addChild(path.get(level + 1), payload);
                }
            }
            else{
                for(IGTreeNode<T> child : root.getChildren()){
                    if(child.is(path.get(level + 1))){
                        return addByPath_recurse(payload, level + 1, child, path);
                    }
                }
            }
        }
        return null;
    }

    /*====PathTree algorithms: find path==============================================================================*/

    @Override
    public String[] pathFromTreeNode(IGTreeNode<T> root, IGTreeNode<T> treeNode) {
        String[] fullPath = new String[treeNode.level() + 1];
        do{
            fullPath[treeNode.level()] = treeNode.identifier();
        }
        while((treeNode = treeNode.parent()) != null);
        return fullPath;
    }

    @Override
    public List<String> getAllPaths(IGTreeNode<T> root, char pathSep) {
        return new AllPathsUtil<>(root, pathSep).getPaths();
    }

    /*====General tree parse algorithms===============================================================================*/

    @Override
    public boolean preOrder(IGTreeNode<T> root, IGTreeTask<T> task) {
        boolean result = task.doTask(root);
        for(IGTreeNode<T> child : root.getChildren()){
            result |= preOrder(child, task);
        }
        return result;
    }

    @Override
    public boolean postOrder(IGTreeNode<T> root, IGTreeTask<T> task) {
        boolean result = false;
        for(IGTreeNode<T> child : root.getChildren()){
            result |= postOrder(child, task);
        }
        result |= task.doTask(root);
        return result;
    }

    @Override
    public boolean breadthFirst (IGTreeNode<T> root, IGTreeTask<T> task) {
        boolean result = false;
        final Queue<IGTreeNode<T>> queue = new ArrayDeque<>();

        queue.add(root);

        while(!queue.isEmpty()) {
            IGTreeNode<T> curr = queue.remove();
            result |= task.doTask(curr);
            if(!curr.isLeaf()){
                queue.addAll(curr.getChildren());
            }
        }

        return result;
    }

    private static class AllPathsUtil <T>{
        private final IGTreeNode<T> root;
        private final List<String> paths;
        private final char pathSep;

        AllPathsUtil(IGTreeNode<T> root, char pathSep){
            this.root = root;
            this.pathSep = pathSep;
            this.paths = new ArrayList<>();
            if(root != null){
                this.recurse(root, "");
            }
        }
        private void recurse(IGTreeNode<T> curr, String currPath) {
            if(curr.isLeaf()){
                paths.add(currPath + pathSep + curr.identifier());
            }
            else{
                for(IGTreeNode<T> child : curr.getChildren()){
                    String newPath = (curr.isRoot())?
                        curr.identifier() : currPath + pathSep + curr.identifier();
                    recurse(child, newPath);
                }
            }
        }
        public List<String> getPaths(){
            return paths;
        }
    }
}
