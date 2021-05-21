package runstate.iface;

import datasource.iface.IDataSource;

public interface IRunState extends IRunStep {
    void setInFilePath(String filePath);
    IRunStep currentSourceStep();

    /* Program state set */
    void initRunState();
    void initTest(IDataSource dataSource);// for testing only
    void initStep1();
    void initStep2();

}
