package translators.genjava;

import codedef.iface.ICodeNode;
import mock.MockSource;
import org.junit.jupiter.api.Test;
import runstate.Glob;
import translators.iface.IImportScraper;
import translators.strategy.WriteStrategyDisplay;
import translators.util.PathUtil;

class ImportScraperJavaTest {
    MockSource mockSource = new MockSource();

    @Test
    void givenVarDefs_accumulateImports() {
        ICodeNode root = mockSource.mockVarDefMultiFile();
        root.translator().setPath(new PathUtil().setExternalPath(""));

        IImportScraper scraper = new ImportScraperJava();
        scraper.autoAddImports(root);
        Glob.TRANSLATION_CENTER.setRoot(root).setWriteStrategy(
                new WriteStrategyDisplay()
        ).go();
     }

}