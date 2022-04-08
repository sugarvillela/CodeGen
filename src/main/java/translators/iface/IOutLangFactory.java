package translators.iface;

import codedef.enums.ENU_LANGUAGE;
import langformat.iface.IFormatStrategy;

/** Set a source code destination language; get factories, strategies tailored to that language */
public interface IOutLangFactory {
    void setOutLang(ENU_LANGUAGE outLangEnum);
    ITranslatorFactory getTranslatorFactory();
    IFormatStrategy getFormatStrategy();
}
