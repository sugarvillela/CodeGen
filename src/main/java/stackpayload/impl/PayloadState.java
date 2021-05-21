package stackpayload.impl;

import stackpayload.iface.IPayloadState;
import stackpayload.iface.IStackPayload;

public class PayloadState implements IPayloadState {
    protected IStackPayload parentPayload;
    protected int intState, timeOnStack;
    protected String stringState;

    public PayloadState() {}

    @Override
    public void init(IStackPayload parentPayload) {
        this.parentPayload = parentPayload;
    }

    @Override
    public void set(int state) {
        this.intState = state;
    }

    @Override
    public void set(String string) {
        this.stringState = string;
    }

    @Override
    public int getInt() {
        return intState;
    }

    @Override
    public String getString() {
        return stringState;
    }

    @Override
    public void incTimeOnStack() {
        timeOnStack++;
        IStackPayload below = parentPayload.getBelow();
        if(below != null){
            below.getState().incTimeOnStack();
        }
    }

    @Override
    public int timeOnStack() {
        return timeOnStack;
    }

    @Override
    public String toString() {
        return "PayloadState{" +
                ", timeOnStack=" + timeOnStack +
                ", intState=" + intState +
                ", stringState='" + stringState + '\'' +
                '}';
    }
}
