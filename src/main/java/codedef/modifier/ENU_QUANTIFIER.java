package codedef.modifier;

import err.ERR_TYPE;

public enum ENU_QUANTIFIER {
    ZERO    (0),
    ONE     (1),
    MANY    (1, 0xFFFFFFF),
    ANY     (0, 0xFFFFFFF),
    ;

    private final int lo, hi;

    ENU_QUANTIFIER(int n) {
        this.lo = n;
        this.hi = n;
    }
    ENU_QUANTIFIER(int lo, int hi) {
        this.lo = lo;
        this.hi = hi;
    }

    public static boolean isEnum(String text){
        try{
            valueOf(text);
            return true;
        }
        catch(IllegalArgumentException e){
            return false;
        }
    }

    public static ENU_QUANTIFIER fromString(String text){
        try{
            return valueOf(text);
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }

    public ERR_TYPE assertValidQuantity(int n){
        return (lo <= n && n <= hi)? ERR_TYPE.NONE : ERR_TYPE.INVALID_QUANTITY;
    }
}
