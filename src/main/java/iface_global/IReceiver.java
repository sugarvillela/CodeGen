package iface_global;

public interface IReceiver {
    default void receive(String... data){}
    default void receive(int... data){}
    default void receive(boolean... data){}
    default void receive(float... data){}
    default void receive(double... data){}
    default void receive(byte... data){}
    default void receive(Object... data){}
}
