package codedef.modifier;

public enum ENU_BOOLEAN {
    TRUE, FALSE;

    public static boolean isTrue(String boolString){
        return TRUE.toString().equals(boolString);
    }
    public static boolean isFalse(String boolString){
        return FALSE.toString().equals(boolString);
    }
}
