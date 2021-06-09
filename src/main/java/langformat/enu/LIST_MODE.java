package langformat.enu;

public enum LIST_MODE {
    CSV(","),
    SSV(" ");

    private final String strVal;

    LIST_MODE(String strVal) {
        this.strVal = strVal;
    }
}
