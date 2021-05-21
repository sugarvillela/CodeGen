package datasource.dec;

import datasource.iface.IDataSource;

public class SourceActiveOnly extends InterceptorBase{
    public SourceActiveOnly(IDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected boolean shouldSkip() {
        return !nextNode.active();
    }
}
