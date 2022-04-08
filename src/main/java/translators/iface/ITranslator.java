package translators.iface;

import codedef.iface.ICodeNode;

public interface ITranslator {
    String go();        // setup and run
    ITranslator head(); // blank or header text (call header children)
    ITranslator body(); // call body children
    String getCode();   // returns unformatted output

    ITranslator setCodeNode(ICodeNode codeNode);    // link to parent node (for accessing tree structure)
    ITranslator setPath(IPathUtil pathStrategy);    // f
    ITranslator setWriteStrategy(IWriteStrategy writeStrategy);
}
