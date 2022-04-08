package translators.strategy;

import translators.iface.IWriteStrategy;

public class WriteStrategyDisplay implements IWriteStrategy {
    @Override
    public void write(String formattedCode, String writePath) {
        System.out.println(formattedCode);
    }
}
