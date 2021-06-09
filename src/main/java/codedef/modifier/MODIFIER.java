package codedef.modifier;

import codedef.iface.IModifierEnum;

import static codedef.modifier.ENU_DATA_TYPE.*;
import static codedef.modifier.ENU_QUANTIFIER.*;

public enum MODIFIER implements IModifierEnum {
    // mod name     argType     argNum      enumeratedType
    ATTRIB_SCOPE    (STRING,    ONE,        null),
    OUT_LANG        (STRING,    ONE,        ENU_OUT_LANG.class),
    FORMAT_INDENT   (INT,       ONE,        null),
    NAME            (STRING,    ONE,        null),
    LIT_VAL         (STRING,    ONE,        null),
    VISIBILITY      (STRING,    ONE,        ENU_VISIBILITY.class),
    ABSTRACT        (BOOLEAN,   ONE,        ENU_BOOLEAN.class),
    FINAL           (BOOLEAN,   ONE,        ENU_BOOLEAN.class),
    STATIC          (BOOLEAN,   ONE,        ENU_BOOLEAN.class),
    EXTENDS         (STRING,    ANY,        null),
    IMPLEMENTS      (STRING,    ANY,        null),
    OVERRIDE        (BOOLEAN,   ONE,        ENU_BOOLEAN.class),
    DATA_TYPE       (STRING,    ONE,        ENU_DATA_TYPE.class),
    VAR_VALUE       (UNK,       ONE,        null),
    PATH            (STRING,    ONE,        null),
    QUANTIFIER      (STRING,    ONE,        ENU_QUANTIFIER.class),
    COLLECTION      (STRING,    ONE,        ENU_COLLECTION.class),
    CONJUNCTION_TYPE(STRING,    ONE,        ENU_CONJUNCTION.class),
    COMPARISON_TYPE (STRING,    ONE,        ENU_COMPARISON.class),
    NEGATE          (BOOLEAN,   ONE,        ENU_BOOLEAN.class),
    CODE_NODE_TYPE  (STRING,    ONE,        CODE_NODE.class),
    IS_HEADER       (BOOLEAN,   ONE,        ENU_BOOLEAN.class),

    ;

    private final ENU_DATA_TYPE initArgType;
    private final ENU_QUANTIFIER initArgQuantity;
    private final Class<?> enuType;

    MODIFIER(ENU_DATA_TYPE initArgType, ENU_QUANTIFIER initArgQuantity, Class<?> enuType) {
        this.enuType = enuType;
        this.initArgType = initArgType;
        this.initArgQuantity = initArgQuantity;
    }

    @Override
    public boolean isEnumerated() {
        return enuType != null;
    }

    @Override
    public Class<?> enumeratedType() {
        return enuType;
    }

    @Override
    public ENU_DATA_TYPE initArgType() {
        return initArgType;
    }

    @Override
    public ENU_QUANTIFIER initArgQuantity() {
        return initArgQuantity;
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
