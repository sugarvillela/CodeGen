package langformat.enu;

public enum CONTROL_ENTITIES {
    INC_,
    DEC_,
    BLANK_
    ;

    public String entity() {
        return "<_" + super.toString() + ">";
    }
}
