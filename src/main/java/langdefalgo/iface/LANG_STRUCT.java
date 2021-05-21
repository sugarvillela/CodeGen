package langdefalgo.iface;

import stackpayload.iface.IStackPayload;

public interface LANG_STRUCT {
    boolean go(IStackPayload stackTop);

    void onPush(IStackPayload stackTop);
    void onPop(IStackPayload stackTop);
    void onNest(IStackPayload newTop);

    IStackPayload newStackPayload();

}
