package translators.iface;

import codedef.iface.ICodeNode;

public interface IImportScraper {
    void autoAddImports(ICodeNode codeNode);
    //void parseForImports(ICodeNode codeNode, String currPathInfo, Set<CODE_NODE> currAcc);
    //Map<String, List<String>> getImports();
}
