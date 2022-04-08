package translators.impl;

import codedef.enums.ENU_LANGUAGE;
import langformat.iface.IFormatStrategy;
import langformat.impl.FormatStrategyCType;
import translators.genjava.JTranslatorFactory;
import translators.iface.IOutLangFactory;
import translators.iface.ITranslatorFactory;

public class OutLangFactory implements IOutLangFactory {
    private ITranslatorFactory translatorFactory;
    private ENU_LANGUAGE outLangEnum;
    private IFormatStrategy formatStrategy;

    @Override
    public void setOutLang(ENU_LANGUAGE outLangEnum) {
        this.outLangEnum = outLangEnum;
    }

    @Override
    public ITranslatorFactory getTranslatorFactory() {
        if((translatorFactory == null)){
            switch(outLangEnum){
//                case JAVA_:
//                    return (translatorFactory = new JTranslatorFactory());
                default:
                    return (translatorFactory = new JTranslatorFactory());
            }
        }
        else{
            return translatorFactory;
        }
    }

    @Override
    public IFormatStrategy getFormatStrategy() {
        return (formatStrategy == null)?
                (formatStrategy = new FormatStrategyCType())
                : formatStrategy;
    }
}
