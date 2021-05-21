package codedef.modifier;

public enum ENU_VISIBILITY {
    PUBLIC, PRIVATE, PROTECTED
    ;

    public static boolean isEnum(String text){
        try{
            valueOf(text);
            return true;
        }
        catch(IllegalArgumentException e){
            return false;
        }
    }
    public static ENU_VISIBILITY fromString(String text){
        try{
            return valueOf(text);
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }
}
