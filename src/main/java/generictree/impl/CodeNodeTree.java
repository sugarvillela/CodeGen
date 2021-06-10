package generictree.impl;

import codedef.iface.ICodeNode;
import generictree.iface.IGTreeNode;

import static codedef.modifier.MODIFIER.PATH;

public class CodeNodeTree extends SteadyPathTree<ICodeNode> {
    public CodeNodeTree(char splitChar) {
        super(splitChar);
    }

    @Override
    public IGTreeNode<ICodeNode> addBranch(ICodeNode payload, String identifier) {
        steadyPath.add(identifier);
        if(payload.getAttribModifier().isRequired(PATH)){
            payload.getAttribModifier().put(PATH, this.pathAsString());
        }
        int level = steadyPath.size() - 1;
        //System.out.println("Put: " + level + ": " + this.pathString());
        return pathTree.add(payload, steadyPath);
    }

    @Override
    public void finalizeTree() {
        IGTreeNode<ICodeNode> root = pathTree.getRoot();
        if(root != null){
            //root.getPayload().finalizeTree(null);
        }
    }
    @Override
    public void display() {
        IGTreeNode<ICodeNode> root = pathTree.getRoot();
        if(root != null){
            root.getPayload().display(0);
        }
    }

    @Override
    public <E extends Enum<E>> void pathBack(E enu) {
        if(steadyPath.isEmpty()){
            return;
        }
        System.out.println("target = "+ enu);
        System.out.println("path: " + pathAsString());
        IGTreeNode<ICodeNode> parentTreeNode = pathTree.getParse().treeNodeFromPath(pathTree.getRoot(), steadyPath);
        int i = this.recurse(parentTreeNode, enu, steadyPath.size() - 1);
        if(i != -1){
            steadyPath = steadyPath.subList(0, i + 1);
        }
        System.out.println("path: " + pathAsString());
    }

    private IGTreeNode<ICodeNode> recurse(IGTreeNode<ICodeNode> currTreeNode, String identifier){
        if(currTreeNode != null){
            return (currTreeNode.is(identifier))? currTreeNode : recurse(currTreeNode.parent(), identifier);
        }
        return null;
    }

    private <E extends Enum<E>> int recurse(IGTreeNode<ICodeNode> currTreeNode, E enu, int i){
        ICodeNode payload;
        if(currTreeNode != null && (payload = currTreeNode.getPayload()) != null){
            return (enu == payload.codeNodeEnum())? i : recurse(currTreeNode.parent(), enu, i - 1);
        }
        return -1;
    }
}
