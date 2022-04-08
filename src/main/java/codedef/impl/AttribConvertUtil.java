package codedef.impl;

import codedef.enums.MODIFIER;
import err.ERR_TYPE;
import runstate.Glob;

import java.util.Arrays;

/** For various input (string, number, bool, enum) validate and convert to string.
 *  Expect args in string form as well (for JSON files)
 *  This enables AttribModifier to accept any arg type, subject to the rules
 *  established in CodeDef */
public class AttribConvertUtil {
    private static AttribConvertUtil instance;
    private AttribConvertUtil(){}
    public static AttribConvertUtil initInstance(){
        return (instance == null)? (instance = new AttribConvertUtil()): instance;
    }

    public String[] convert(MODIFIER modifier, Object... objects) {
        String[] out = new String[objects.length];
        int i = 0;

        // Validate number of args against modifier.initArgQuantity
        if(!modifier.getInitArgQuantity().isValidArgs(objects)){
            String desc = String.format(
                    "Found %s with values %s. Should be %s items (%d:%d)",
                    modifier,
                    Arrays.toString(objects),
                    modifier.getInitArgQuantity(),
                    modifier.getInitArgQuantity().getLoRange(),
                    modifier.getInitArgQuantity().getHiRange()
            );
            Glob.ERR.kill(ERR_TYPE.INVALID_QUANTITY, desc);
        }

        // Validate type of arg against modifier.initArgType
        for (Object object : objects) {
            if (object == null){
                out[i++] = "NULL";
            }
            else{
                boolean good = (object instanceof String)?
                    modifier.getInitArgType().assertValid(modifier, (String)object) :
                    modifier.getInitArgType().assertValid(modifier, object);
                if(good){
                    out[i++] = modifier.getInitArgType().getValidString();
                }
                else{
                    String desc = String.format(
                            "Found %s with values %s",
                            modifier,
                            Arrays.toString(objects)
                    );
                    Glob.ERR.kill(modifier.getInitArgType().getErr(), desc);
                }
            }
        }
        return out;
    }
}
