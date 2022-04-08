package codedef.enums;

import static codedef.enums.ENU_DATA_TYPE.*;
import static codedef.enums.ENU_QUANTIFIER.*;

/**Enums for adding attributes to CodeNode objects.
 * Handled by IAttributeModifier (validating and maintaining)
 * Some are required, some are allowed (set in CodeDef)
 * First four are general settings; others used by certain node types.
 *
 * Enum constructor: Used during AttribModifier put() operation
 *   initArgType: validate attribute for numeric, boolean
 *   initArgQuantity: validate proper number of attributes set in one operation
 *   enuType: string should resolve to given Enum, unless enuType is set null.
 *
 *   Caveat: if initArgType is ENU, enuType must not be null
 */
public enum MODIFIER {
    NAME_           (STRING,    ONE,        null),      // Every node has this (set by user or default)
    TYPE_           (ENU,       ONE,        CODE_NODE.class),   // Every node has this (CODE_NODE enum as string)
    IS_HEADER_      (ENU,       ONE,        ENU_BOOLEAN.class), // Set header or body (See JTranslatorFactory)
    IS_GENERIC_     (ENU,       ONE,        ENU_BOOLEAN.class), // Generate <> symbol?

    LANGUAGE_       (STRING,    ONE,        ENU_LANGUAGE.class),// Glob (always java)
    INDENT_         (INT,       ONE,        null),      // Glob, default 4

    ACCESS_         (ENU,       ONE,        ENU_VISIBILITY.class),// Pubic, private, protected
    ABSTRACT_       (ENU,       ONE,        ENU_BOOLEAN.class),
    FINAL_          (ENU,       ONE,        ENU_BOOLEAN.class),
    STATIC_         (ENU,       ONE,        ENU_BOOLEAN.class),
    EXTENDS_        (STRING,    ANY,        null),
    IMPLEMENTS_     (STRING,    ANY,        null),
    OVERRIDE_       (ENU,       ONE,        ENU_BOOLEAN.class), // if true, add @Override annotation

    TYPE_CONJ_      (ENU,       ONE,        ENU_CONJUNCTION.class),// && || in COMPARISON
    TYPE_COMP_      (ENU,       ONE,        ENU_COMPARISON.class), // > < <= == etc
    IS_NEGATE_      (ENU,       ONE,        ENU_BOOLEAN.class),    // puts ! in front of a BOOL_BLOCK

    TYPE_DATA_      (STRING,    MANY,       null),// Used by VAR_DEF and NEW_* (not enumerated, to allow custom types)
    TYPE_RETURN_    (STRING,    ONE,        null),// data type for methods, so named to avoid confusion

    ARGS_           (STRING,    MANY,       null),// for method call with leaf values; also pass with setChildren()

    SIZE_           (INT,       ONE,        null),  // Used by NEW_ARRAY

    LIT_            (STRING,    MANY,       null),  // text for IMPORT_ITEM, SWITCH, COMMENT, LIT etc
    FOR_INIT_       (INT,       ONE,        null),  // i = value, for simple for init
    FOR_INC_        (INT,       ONE,        null),  // i += value, for simple for increment

    QUANTIFIER      (ENU,       ONE,        ENU_QUANTIFIER.class),
    ;

    private final ENU_DATA_TYPE initArgType;
    private final ENU_QUANTIFIER initArgQuantity;
    private final Class<?> enuType;

    MODIFIER(ENU_DATA_TYPE initArgType, ENU_QUANTIFIER initArgQuantity, Class<?> enuType) {
        this.initArgType = initArgType;
        this.initArgQuantity = initArgQuantity;
        this.enuType = enuType;
    }

    public ENU_DATA_TYPE getInitArgType() {
        return initArgType;
    }
    public ENU_QUANTIFIER getInitArgQuantity() {
        return initArgQuantity;
    }
    public Class<?> getEnuType(){
        return this.enuType;
    }

    public static MODIFIER fromString(String text){
        try{
            return valueOf(text);
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }
}
