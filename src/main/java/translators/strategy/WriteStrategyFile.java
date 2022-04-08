package translators.strategy;

import translators.iface.IWriteStrategy;
import utilfile.UtilFileJson;

public class WriteStrategyFile implements IWriteStrategy {
    @Override
    public void write(String formattedCode, String writePath) {
        UtilFileJson.initInstance().put(formattedCode, writePath);
    }
}
