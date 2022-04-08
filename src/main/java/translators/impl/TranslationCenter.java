package translators.impl;

import codedef.iface.IAttribModifier;
import codedef.iface.ICodeNode;
import codedef.enums.ENU_LANGUAGE;
import codejson.impl.JCodeBuilder;
import langformat.iface.IFormatStrategy;
import langformat.iface.INullableUtil;
import langformat.impl.Formatter;
import langformat.impl.NullableUtil;
import runstate.Glob;
import translators.genjava.ImportScraperJava;
import translators.iface.IOutLangFactory;
import translators.iface.ITranslationCenter;
import translators.iface.ITranslatorFactory;
import translators.iface.IWriteStrategy;
import translators.util.PathUtil;
import utilfile.UtilFileJson;

import static codedef.enums.CODE_NODE.GLOB;
import static codedef.enums.ENU_LANGUAGE.JAVA_;
import static codedef.enums.MODIFIER.*;

public class TranslationCenter implements ITranslationCenter {
    private static TranslationCenter instance;

    public static TranslationCenter initInstance(){
        return (instance == null)? (instance = new TranslationCenter()) : instance;
    }

    private final IOutLangFactory outLangFactory;   // provides handlers for chosen out language
    private final INullableUtil nullableUtil;       // get values from string array from json element
    ICodeNode root;                 // tree root
    IWriteStrategy writeStrategy;   // to return/process formatted output code
    String unformatted;             // to return unformatted code
    String xPath;                   // absolute path to code source root directory for path util, file strategy
    boolean keepUnFormatted;        // set true if you want to use unformatted

    private TranslationCenter(){
        outLangFactory = new OutLangFactory();
        outLangFactory.setOutLang(JAVA_);
        nullableUtil = NullableUtil.initInstance();
    }

    /*=====Root create, set, get======================================================================================*/

    @Override
    public ITranslationCenter importJson(String jsonPath) {
        root = new JCodeBuilder().build(jsonPath).getRoot();
        return this;
    }

    @Override
    public void exportJson(String jsonPath) {
        if(root != null){
            UtilFileJson.initInstance().put(root.exportJson(), jsonPath);
        }
    }

    @Override
    public ITranslationCenter setRoot(ICodeNode root) {
        this.root = root;
        return this;
    }

    @Override
    public ICodeNode getRoot() {
        return root;
    }

    /*=====Translator Set up==========================================================================================*/

    @Override
    public ITranslationCenter setExternalPath(String xPath) {
        this.xPath = xPath;
        return this;
    }

    @Override
    public ITranslationCenter setWriteStrategy(IWriteStrategy writeStrategy) {
        this.writeStrategy = writeStrategy;
        return this;
    }

    @Override
    public ITranslationCenter setKeepUnFormatted(boolean keepUnFormatted) {
        this.keepUnFormatted = keepUnFormatted;
        return this;
    }

    @Override
    public ITranslationCenter readAttribFromRoot() {
        IAttribModifier attributes = root.getAttribModifier();
        String value;
        int tab;
        if((tab = nullableUtil.extractNumber(attributes.get(INDENT_))) != Integer.MIN_VALUE){
            Formatter.initInstance().setTab(tab);
        }
        if((value = nullableUtil.extractString(attributes.get(LANGUAGE_))) != null){
            this.setOutLang(Glob.UTIL_ENUM.fromStringOrKill(ENU_LANGUAGE.class, value));
        }
        return this;
    }

    @Override
    public ITranslationCenter go() {
        if(root != null){
            if(root.getCodeNodeEnum() != GLOB){
                Glob.ERR_DEV.kill("Require " + GLOB.toString() + " as tree root");
            }

            root.translator().setPath(new PathUtil().setExternalPath(xPath));
            new ImportScraperJava().autoAddImports(root);
            root.translator().setWriteStrategy(writeStrategy);

            if(keepUnFormatted){
                unformatted = root.translator().go();
            }
            else{
                unformatted = null;
                root.translator().go();
            }
        }
        return this;
    }

    @Override
    public String getUnformatted() {
        return unformatted;
    }



    /*=====Getters====================================================================================================*/

    @Override
    public IOutLangFactory getOutLangFactory() {
        return outLangFactory;
    }

    /*=====IOutLangFactory wrappers===================================================================================*/

    @Override
    public void setOutLang(ENU_LANGUAGE outLangEnum) {
        outLangFactory.setOutLang(outLangEnum);
    }

    @Override
    public ITranslatorFactory getTranslatorFactory() {
        return outLangFactory.getTranslatorFactory();
    }

    @Override
    public IFormatStrategy getFormatStrategy() {
        return outLangFactory.getFormatStrategy();
    }
}
