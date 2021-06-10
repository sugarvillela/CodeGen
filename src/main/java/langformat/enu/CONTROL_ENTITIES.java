package langformat.enu;

public enum CONTROL_ENTITIES {
    INC_,
    DEC_,
    BLANK_
    ;

    @Override
    public String toString() {
        return "<_" + super.toString() + "_>";
    }
}
