package generictree.task;

import generictree.iface.IGTreeNode;
import generictree.iface.IGTreeTask;
import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;

public class TaskValidCodeNode implements IGTreeTask<ICodeNode> {
    @Override
    public boolean doTask(IGTreeNode<ICodeNode> treeNode) {
        ICodeNode codeNode = treeNode.getPayload();
        IAttribModifier attribModifier = codeNode.getAttribModifier();
        IAttribStruct attribStruct = codeNode.getAttribStruct();

        attribModifier.assertHaveRequiredModifiers();
        attribStruct.assertHaveRequiredChildren(treeNode.getPayloadChildren());
        return false;
    }
}
