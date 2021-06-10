package codedef.codenode;

import codedef.iface.ICodeNode;
import codedef.impl.PrototypeFactory;
import mock.MockSource;
import org.junit.jupiter.api.Test;
import runstate.Glob;

class CodeNodeTest {
    MockSource mockSource = new MockSource();
    @Test
    void testInternalTreeBuild() {
        ICodeNode root = mockSource.mockCodeNodeTree();
        root.display(0);
        String actual = root.translator().format(root);
        System.out.println(actual);
    }
}