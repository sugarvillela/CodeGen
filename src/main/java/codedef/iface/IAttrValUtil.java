package codedef.iface;

import codedef.enums.MODIFIER;
import err.ERR_TYPE;

/** Implemented by ENU_DATA_TYPE.enums for arg validation
 *  Called by AttribConvertUtil */
public interface IAttrValUtil {
    boolean assertValid(MODIFIER modifier, String string);
    boolean assertValid(MODIFIER modifier, Object object);
    String getValidString();
    ERR_TYPE getErr();
}
