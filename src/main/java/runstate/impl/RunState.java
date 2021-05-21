package runstate.impl;

import runstate.iface.IRunState;
import runstate.iface.IRunStep;

public class RunState implements IRunState {
    private static RunState instance;

    public static RunState initInstance(){
        return (instance == null)? (instance = new RunState()): instance;
    }

    private RunState(){}

    private String filePath;
    private IRunStep currentSourceStep;

    @Override
    public void setInFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public IRunStep currentSourceStep() {
        return currentSourceStep;
    }

    /* Program state hooks */

    @Override
    public void initRunState() {

    }

    @Override
    public void initStep1() {

    }

    /*=====SourceStep methods, delegate to currSourceStep=============================================================*/

    @Override
    public void go() {
        currentSourceStep.go();
    }
}
