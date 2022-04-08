package translators.strategy;

import translators.iface.IWriteStrategy;

public class WriteStrategyNop implements IWriteStrategy {
    @Override
    public void write(String formattedCode, String writePath) { }
}
