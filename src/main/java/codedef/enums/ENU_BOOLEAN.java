package codedef.enums;

public enum ENU_BOOLEAN {
    TRUE, FALSE;

    ENU_BOOLEAN() {
    }

    public static boolean isTrue(String boolString){
        return TRUE.toString().equals(boolString);
    }
    public static boolean isFalse(String boolString){
        return FALSE.toString().equals(boolString);
    }
}
