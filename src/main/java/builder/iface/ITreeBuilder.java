package builder.iface;

import codedef.iface.ICodeNode;
import generictree.iface.ISteadyPathTree;

public interface ITreeBuilder {
    ISteadyPathTree<ICodeNode> getTree();
}
