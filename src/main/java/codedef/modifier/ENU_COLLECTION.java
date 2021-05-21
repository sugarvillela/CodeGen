package codedef.modifier;

public enum ENU_COLLECTION {
    SCALAR,
    ARRAY,
    VECTOR
    ;

    public static ENU_COLLECTION fromString(String text){
        try{
            return valueOf(text);
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }
}
