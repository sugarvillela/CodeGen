package generictree.iface;

import iface_global.ICsv;

import java.util.ArrayList;

public interface IGTreeNode <T> extends ICsv {
    String identifier();
    void setIdentifier(String identifier);
    boolean is(String identifier);

    void setPayload(T payload);
    T getPayload();

    void setLevel(int level);
    int level();

    void setParent(IGTreeNode <T> parent);
    IGTreeNode<T> parent();

    boolean isRoot();
    boolean isLeaf();

    ArrayList<IGTreeNode <T>> getChildren();
    ArrayList<T> getPayloadChildren();

    IGTreeNode <T> addChild(IGTreeNode <T> child);

    IGTreeNode <T> addChild(String identifier, T payload);// addLine child of same subtype

    void setOp(char op);
    char op();

    void setNegated(boolean negated);
    boolean negated();

    void setWrapped(boolean wrapped);
    boolean wrapped();

    // ICsv
    //String friendlyString();
    //String csvString();
}
