package runstate.impl;

import datasource.iface.IDataSource;
import runstate.Glob;
import stackpayload.iface.IStackPayload;

public class RunStep1 extends RunStepBase {

    public RunStep1(IDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void go() {
        IStackPayload stackTop;

        while(dataSource.hasNext()){
            currNode = dataSource.next();
            if(currNode == null){
                System.out.println("overrun");
                break;
            }
            else{
                System.out.println(currNode.csvString());
            }
            if(structStack.size() == 0){
                stackTop = null;
            }
            else{
                stackTop = structStack.top();
                stackTop.getState().incTimeOnStack();
                //System.out.println(stackTop.toString());
            }
            if(!Glob.PUSH_POP_UTIL.handleTextEvent(stackTop)){
                System.out.println("stack top data");
                stackTop.go();
            }
        }

        System.out.println("\nfinished step 1");
        System.out.println(structStack.getStackLog().friendlyString());
        System.out.println("_____\n");

//        RUNTIME_ATTRIB.props.display();
    }

}
