package generictree.task;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;
import codedef.iface.ICodeNode;

public class TaskCodeNodeParent implements IGTreeTask<ICodeNode> {
    @Override
    public boolean doTask(IGTreeNode<ICodeNode> treeNode) {
        treeNode.getPayload().setParentTreeNode(treeNode);
        return false;
    }
}
