package translators.strategy;

import translators.iface.IWriteStrategy;

import java.util.List;

public class WriteStrategyList implements IWriteStrategy {
    private final List<String> list;

    public WriteStrategyList(List<String> list) {
        this.list = list;
    }

    @Override
    public void write(String formattedCode, String writePath) {
        list.add(formattedCode);
    }
}
