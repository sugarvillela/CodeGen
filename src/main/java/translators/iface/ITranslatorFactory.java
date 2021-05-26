package translators.iface;

import codedef.modifier.CODE_NODE;

public interface ITranslatorFactory {
    ITranslator get(CODE_NODE codeNodeEnum);
}
