package translators.iface;

import codedef.iface.ICodeNode;

/** Entry point for json import, export, code generation */
public interface ITranslationCenter extends IOutLangFactory{
    /**Set root with json obj from file using JCodeBuilder
     * @param jsonPath path to the json file to be read
     * @return self for builder pattern */
    ITranslationCenter importJson(String jsonPath);

    /**Set root directly
     * @param root CodeNode initialized as GLOB is root of tree
     * @return self for builder pattern */
    ITranslationCenter setRoot(ICodeNode root);

    /**Write root to a Json file
     * @param jsonPath path to the json file to be written */
    void exportJson(String jsonPath);

    /** @return CodeNode initialized as GLOB is root of tree */
    ICodeNode getRoot();

    /**Need path for building internal file path
     * @param xPath path to the root directory where files will be generated
     * @return self for builder pattern */
    ITranslationCenter setExternalPath(String xPath);// path to code source root, for building file path

    /**Can write to file, print to screen etc
     * @param writeStrategy the strategy for writing formatter output
     * @return self for builder pattern */
    ITranslationCenter setWriteStrategy(IWriteStrategy writeStrategy);

    /**Translator always returns unformatted text
     * @param keepUnFormatted set whether to keep unformatted or set null
     * @return self for builder pattern */
    ITranslationCenter setKeepUnFormatted(boolean keepUnFormatted);

    /**Overwrites default settings (language, indent) with attributes from json file
     * @return self for builder pattern */
    ITranslationCenter readAttribFromRoot();

    /** Parses tree, generates code, outputs per write strategy
     * @return self for builder pattern */
    ITranslationCenter go();

    /**This is null unless setKeepUnFormatted is set true
     * @return unformatted output from translator */
    String getUnformatted();

    IOutLangFactory getOutLangFactory();
}

