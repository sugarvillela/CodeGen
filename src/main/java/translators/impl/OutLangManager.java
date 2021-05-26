package translators.impl;

import codedef.modifier.ENU_OUT_LANG;
import translators.genjava.JTranslators;
import translators.iface.IOutLangManager;
import translators.iface.ITranslatorFactory;

import static codedef.modifier.ENU_OUT_LANG.JAVA;

public class OutLangManager implements IOutLangManager {
    private static OutLangManager instance;

    public static OutLangManager initInstance(){
        return (instance == null)? (instance = new OutLangManager()) : instance;
    }

    private OutLangManager(){
        this.outLangEnum = JAVA;
    }

    private ITranslatorFactory translatorFactory;
    private ENU_OUT_LANG outLangEnum;

    @Override
    public void setOutLang(ENU_OUT_LANG outLangEnum) {
        this.outLangEnum = outLangEnum;
    }

    @Override
    public ITranslatorFactory getTranslatorFactory() {
        switch(outLangEnum){
            case JAVA:
                return (translatorFactory == null)? (translatorFactory = new JTranslators()) : translatorFactory;
        }
        return null;
    }
}
