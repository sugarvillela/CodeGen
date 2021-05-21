package codedef.iface;

import codedef.modifier.ENU_DATA_TYPE;
import codedef.modifier.ENU_QUANTIFIER;

public interface IModifierEnum {
    ENU_DATA_TYPE initArgType();
    ENU_QUANTIFIER initArgQuantity();
    boolean isEnumerated();
    Class<?> enumeratedType();
}
