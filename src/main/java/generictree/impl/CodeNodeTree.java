package generictree.impl;

import codedef.iface.ICodeNode;

import static codedef.modifier.MODIFIER.PATH;

public class CodeNodeTree extends SteadyPathTree<ICodeNode> {
    public CodeNodeTree(char splitChar) {
        super(splitChar);
    }

    @Override
    public boolean put(ICodeNode payload, String identifier) {
        steadyPath.add(identifier);
        if(payload.getAttribModifier().isRequired(PATH)){
            payload.getAttribModifier().put(PATH, this.pathString());
        }
        int level = steadyPath.size() - 1;
        System.out.println("Put: " + level + ": " + this.pathString());
        return pathTree.put(payload, this.currPath());
    }
}
