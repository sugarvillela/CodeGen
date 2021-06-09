package langformat.impl;

import langformat.iface.IAccumulator;

import java.util.ArrayList;
import java.util.List;

public class Accumulator implements IAccumulator {
    private final List<String> acc;
    private final String delimiter;

    public Accumulator(String delimiter) {
        this.delimiter = delimiter;
        acc = new ArrayList<>();
    }

    @Override
    public void add(String text) {
        acc.add(text);
    }

    @Override
    public String finish() {
        return String.join(delimiter, acc);
    }
}
