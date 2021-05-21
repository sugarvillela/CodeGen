package runstate.impl;

import datasource.iface.IDataSource;
import langdefalgo.impl.AlgoImplGroupStep1;
import readnode.iface.IReadNode;
import runstate.iface.IRunState;
import runstate.iface.IRunStep;
import stack.iface.IStructStack;

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
    public void initTest(IDataSource dataSource) {
        new AlgoImplGroupStep1().initAlgos();
        currentSourceStep = new RunStep1(dataSource);
    }

    @Override
    public void initStep1() {
        //new AlgoImplGroupStep1().initAlgos();

//        currentSourceStep = new RunStep1(
//            new SourceActiveOnly(
//                        new SourceTextEvent(
//                                new SourceTok(
//                                    new SourceNonEmpty(
//                                        new SourceFile(filePath)
//                                    )
//                                )
//                        )
//            )
//        );
    }

    @Override
    public void initStep2() {

    }

    /*=====SourceStep methods, delegate to currSourceStep=============================================================*/

    @Override
    public void go() {
        currentSourceStep.go();
    }

    @Override
    public void setCurrNode(IReadNode currNode) {
        currentSourceStep.setCurrNode(currNode);
    }

    @Override
    public IReadNode getCurrNode() {
        return currentSourceStep == null? null : currentSourceStep.getCurrNode();
    }

    @Override
    public IStructStack getStack() {
        return currentSourceStep.getStack();
    }

}
