package translators.strategy;

import translators.iface.IWriteStrategy;

import java.util.List;

public class WriteStrategyListWPaths implements IWriteStrategy {
    private final List<String> list;

    public WriteStrategyListWPaths(List<String> list) {
        this.list = list;
    }

    @Override
    public void write(String formattedCode, String writePath) {
        list.add(writePath);
        list.add(formattedCode);
    }
}
