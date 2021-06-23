package langformat.enu;

public enum CONTROL_ENTITIES {
    I_, // increment
    D_, // decrement
    N_, // new line
    B_  // blank line
    ;

    public String entity() {
        return "[" + super.toString().charAt(0) + ']';
    }
}
