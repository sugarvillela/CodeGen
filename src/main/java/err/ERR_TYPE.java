package err;

/** If resources are scarce, a switch for messaging would be better;
 * Instantiating all messages up front violates the 'plan for the common case' rule */

public enum ERR_TYPE {
    NONE                ("", false),

    // Attrib errors
    NOT_KEY_VAL         ("Expected 'key=value' format here"),
    UNKNOWN_KEY         ("Unknown attribute key"),
    INVALID_BOOL        ("Expected (uppercase) TRUE FALSE boolean here"),
    INVALID_INT         ("Expected integer value here"),
    INVALID_LONG        ("Expected long integer value here"),
    INVALID_FLOAT       ("Expected floating-point value here"),
    INVALID_STRING      ("Expected non-empty text here"),
    INVALID_NUMBER      ("Expected the proper numeric type here"),
    INVALID_QUANTITY    ("Expected the correct number of items here"),
    INVALID_ENUM        ("Expected an enumerated value here"),
    INVALID_ARG         ("Expected the correct arg type here"),

    // Attrib or structure
    MISSING_REQUIRED    ("A required field is missing"),
    DISALLOWED_NESTING  ("Disallowed nesting"),
    DISALLOWED_ATTRIB   ("Disallowed attribute"),
    LANGUAGE_ERR        ("Disallowed in target language"),

    // Text Event errors
    BAD_JSON            ("JSON object improperly formed"),
    MISPLACED_CHAR      ("Misplaced character"),
    DUPLICATE_ID        ("Identifier already exists"),//delete these
    UNKNOWN_ID          ("Identifier is not defined"),

    // General errors
    INTERNAL_ERROR      ("An internal error occurred"),
    DEV_ERROR           ("Developer error"),
    SYNTAX              ("Syntax error"),
    FILE_ERROR          ("Something went wrong with a file"),
    ;

    private final String message;
    private final boolean isErr;

    ERR_TYPE(String message, boolean isErr) {
        this.message = message;
        this.isErr = isErr;
    }
    ERR_TYPE(String message) {
        this(message, true);
    }
    
    public String message(){
        return (message == null)? this.toString() : message;
    }
    public boolean isErr(){
        return isErr;
    }
}
