package translators.iface;

import codedef.iface.ICodeNode;

public interface ITranslator {
    String format(ICodeNode codeNode);
    String go(ICodeNode codeNode);
    ITranslator head();
    ITranslator body();
    ITranslator foot();
    String finish();
}
