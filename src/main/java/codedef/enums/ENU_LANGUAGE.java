package codedef.enums;

public enum ENU_LANGUAGE {
    JAVA_   (".java"),
    CPP_    (".cpp", ".h")
    ;

    private final String ext1, ext2;

    ENU_LANGUAGE(String ext1) {
        this.ext1 = ext1;
        this.ext2 = null;
    }

    ENU_LANGUAGE(String ext1, String ext2) {
        this.ext1 = ext1;
        this.ext2 = ext2;
    }

    public String getExt1(){
        return ext1;
    }
    public String getExt2(){
        return ext2;
    }
}
