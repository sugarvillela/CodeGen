package codedef.enums;

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

    public static ENU_QUANTIFIER fromString(String text){
        try{
            return valueOf(text);
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }

    public boolean isValidArgs(Object... args){
        int len = args.length;
        return (lo <= len && len <= hi);
    }

    public int getLoRange(){
        return lo;
    }
    public int getHiRange(){
        return hi;
    }
}
