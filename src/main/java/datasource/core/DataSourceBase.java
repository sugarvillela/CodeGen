package datasource.core;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;

/** Most inner implementations inherit from this class, while most decorator implementations
 *  inherit from DecoratorBase or InterceptorBase.
 *  Any class that inherits from this can support peekBack and peekForward without a peek decorator,
 *  as long as no other decorator is applied (decorators take internal and external state out of sync).
 *  These implementations are list-based or array-based, so peeking is easily done */
public abstract class DataSourceBase implements IDataSource {
    private static int uqValue = 0;
    protected final int numericIdentifier;  // for impl without fileName, uq prefix allows to tell sources apart
    protected String stringIdentifier;      // short file name, or just 'array' or 'list'

    protected int row;

    public DataSourceBase() {
        //this.numericIdentifier = uqValue++; // comment out for tests
        this.numericIdentifier = uqValue; // uncomment for tests
        this.row = -1;
    }

    // need adaptor for array and list impl
    protected abstract int dataSize();
    protected abstract IReadNode getData(int index);

    protected DataSourceBase tick(){
        row ++;
        return this;
    }

    @Override
    public String sourceName() {
        return stringIdentifier + "@" + numericIdentifier;
    }

    @Override
    public boolean hasNext() {
        return row < this.dataSize() - 1;
    }

    @Override
    public IReadNode next() {
        //IReadNode nextNode = this.tick().getData(row);
        return this.tick().getData(row);
    }

}
