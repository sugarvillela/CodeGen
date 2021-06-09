package translators.iface;

import codedef.modifier.ENU_OUT_LANG;
import langformat.iface.IFormatStrategy;

public interface IOutLangManager {
    void setOutLang(ENU_OUT_LANG outLangEnum);
    ITranslatorFactory getTranslatorFactory();
    IFormatStrategy getFormatStrategy();
}
