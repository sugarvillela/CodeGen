package util_enu;

import err.ERR_TYPE;
import runstate.Glob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UtilEnum {
    private static UtilEnum instance;

    public static UtilEnum initInstance(){
        return(instance ==  null)? (instance = new UtilEnum()) : instance;
    }

    private UtilEnum(){}

    public <T extends Enum<T>> T fromString(Class<?> enumClass, String value){
        try{
            Method method = enumClass.getMethod("valueOf", String.class);
            return (T)method.invoke(null, value);
        }
        catch(
                NoSuchMethodException | InvocationTargetException |
                        IllegalAccessException | ClassCastException |
                        IllegalArgumentException e
        ){
            return null;
        }
    }

    public <T extends Enum<T>> boolean isValid(Class<?> enumClass, String value){
        return (fromString(enumClass, value) != null);
    }

    public <T extends Enum<T>> ERR_TYPE assertValid(Class<?> enumClass, String value){
        return (fromString(enumClass, value) == null)? ERR_TYPE.SYNTAX : ERR_TYPE.NONE;
    }

    public <T extends Enum<T>> T fromStringOrKill(Class<?> enumClass, String value){
        T out = fromString(enumClass, value);
        if(out == null){
            Glob.ERR.kill(ERR_TYPE.UNKNOWN_KEY, value);
        }
        return out;
    }

    public <T extends Enum<T>> List<String> enumListToStringList(List<T> source){
        List<String> dest =  new ArrayList<>(source.size());
        for(T enu : source){
            dest.add(String.valueOf(enu));
        }
        return dest;
    }
}
