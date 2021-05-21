package stackpayload.iface;

public interface IPayloadState {
    void init(IStackPayload parentPayload);

    void set(int state);
    void set(String string);

    int getInt();
    String getString();

    void incTimeOnStack();
    int timeOnStack();
}
