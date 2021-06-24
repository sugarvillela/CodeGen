package langformat.impl;

import langformat.iface.INullableUtil;

public class NullableUtil implements INullableUtil {
    private static NullableUtil instance;

    public static NullableUtil initInstance(){
        return(instance ==  null)? (instance = new NullableUtil()) : instance;
    }

    private NullableUtil(){}
}
