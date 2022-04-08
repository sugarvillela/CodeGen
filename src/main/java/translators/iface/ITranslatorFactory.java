package translators.iface;

import codedef.iface.ICodeNode;
import codedef.enums.CODE_NODE;

public interface ITranslatorFactory {
    ITranslator get(CODE_NODE codeNodeEnum, ICodeNode codeNode);
}
