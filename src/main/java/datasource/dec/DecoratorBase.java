package datasource.dec;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;

public abstract class DecoratorBase implements IDataSource {
    protected final IDataSource dataSource;

    public DecoratorBase(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String sourceName() {
        return dataSource.sourceName();
    }

    @Override
    public boolean hasData() {
        return dataSource != null && dataSource.hasData();
    }

    @Override
    public boolean hasNext() {
        return dataSource.hasNext();
    }

    @Override
    public IReadNode next() {
        return dataSource.next();
    }

}
