package codedef.enums;

import codedef.iface.IAttrValUtil;
import err.ERR_TYPE;
import runstate.Glob;

import java.util.Locale;

public enum ENU_DATA_TYPE implements IAttrValUtil {
    NULL    (ValidNop.instance),
    ENU     (ValidEnum.instance),
    STRING  (ValidNop.instance),
    INT     (ValidInt.instance),
    LONG    (ValidInt.instance),
    FLOAT   (ValidFloat.instance),
    DOUBLE  (ValidFloat.instance),
    BOOLEAN (ValidBool.instance),
    VOID    (ValidNop.instance),
    UNK     (ValidNop.instance);


    private final IAttrValUtil validator;

    ENU_DATA_TYPE(IAttrValUtil validator) {
        this.validator = validator;
    }

    public static ENU_DATA_TYPE fromString(String value){
        try{
            return valueOf(value);
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public boolean assertValid(MODIFIER modifier, String string) {
        return validator.assertValid(modifier, string);
    }

    @Override
    public boolean assertValid(MODIFIER modifier, Object object) {
        return validator.assertValid(modifier, object);
    }

    @Override
    public ERR_TYPE getErr() {
        return validator.getErr();
    }

    @Override
    public String getValidString() {
        return validator.getValidString();
    }

    private static abstract class AttrValBase implements IAttrValUtil {
        protected String validString;
        protected ERR_TYPE errType;

        public AttrValBase(){
            errType = ERR_TYPE.NONE;
        }

        @Override
        public boolean assertValid(MODIFIER modifier, String string) {
            Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR, "not implemented");
            return false;
        }

        @Override
        public boolean assertValid(MODIFIER modifier, Object object) {
            Glob.ERR_DEV.kill(ERR_TYPE.DEV_ERROR, "not implemented");
            return false;
        }

        @Override
        public String getValidString() {
            return validString;
        }

        @Override
        public ERR_TYPE getErr() {
            return this.errType;
        }
    }
    private static class ValidNop extends AttrValBase {
        static final IAttrValUtil instance = new ValidNop();

        @Override
        public boolean assertValid(MODIFIER modifier, String string) {
            validString = string;
            errType = ERR_TYPE.NONE;
            return true;
        }

        @Override
        public boolean assertValid(MODIFIER modifier, Object object) {
            validString = String.valueOf(object);
            errType = ERR_TYPE.NONE;
            return true;
        }
    }
    private static class ValidInt extends AttrValBase {
        static final IAttrValUtil instance = new ValidInt();

        @Override
        public boolean assertValid(MODIFIER modifier, String string) {
            try{
                Integer.parseInt(string);
                validString = string;
                errType = ERR_TYPE.NONE;
                return true;
            }
            catch(NumberFormatException e){
                validString = null;
                errType = ERR_TYPE.INVALID_INT;
                return false;
            }
        }

        @Override
        public boolean assertValid(MODIFIER modifier, Object object) {
            if(object instanceof Integer){
                validString = object.toString();
                errType = ERR_TYPE.NONE;
                return true;
            }
            else{
                validString = null;
                errType = ERR_TYPE.INVALID_INT;
                return false;
            }
        }
    }
    private static class ValidLong extends AttrValBase {
        static final IAttrValUtil instance = new ValidInt();

        @Override
        public boolean assertValid(MODIFIER modifier, String string) {
            try{
                Long.parseLong(string);
                validString = string;
                errType = ERR_TYPE.NONE;
                return true;
            }
            catch(NumberFormatException e){
                validString = null;
                errType = ERR_TYPE.INVALID_LONG;
                return false;
            }
        }

        @Override
        public boolean assertValid(MODIFIER modifier, Object object) {
            if(object instanceof Long){
                validString = object.toString();
                errType = ERR_TYPE.NONE;
                return true;
            }
            else{
                validString = null;
                errType = ERR_TYPE.INVALID_LONG;
                return false;
            }
        }
    }
    private static class ValidFloat extends AttrValBase{
        static final IAttrValUtil instance = new ValidFloat();

        @Override
        public boolean assertValid(MODIFIER modifier, String string) {
            try{
                Double.parseDouble(string);
                validString = string;
                errType = ERR_TYPE.NONE;
                return true;
            }
            catch(NumberFormatException e){
                validString = null;
                errType = ERR_TYPE.INVALID_FLOAT;
                return false;
            }
        }

        @Override
        public boolean assertValid(MODIFIER modifier, Object object) {
            if((object instanceof Double) || (object instanceof Float)){
                validString = object.toString();
                errType = ERR_TYPE.NONE;
                return true;
            }
            else{
                validString = null;
                errType = ERR_TYPE.INVALID_FLOAT;
                return false;
            }
        }
    }
    private static class ValidBool extends AttrValBase{
        static final IAttrValUtil instance = new ValidBool();

        @Override
        public boolean assertValid(MODIFIER modifier, String string) {
            switch(string){
                case "TRUE":
                case "FALSE":
                    validString = string;
                    errType = ERR_TYPE.NONE;
                    return true;
                default:
                    validString = null;
                    errType = ERR_TYPE.INVALID_BOOL;
                    return false;
            }
        }

        @Override
        public boolean assertValid(MODIFIER modifier, Object object) {
            if((object instanceof Boolean)){
                validString = object.toString().toUpperCase(Locale.ROOT);
                errType = ERR_TYPE.NONE;
                return true;
            }
            else{
                validString = null;
                errType = ERR_TYPE.INVALID_FLOAT;
                return false;
            }
        }
    }
    private static class ValidEnum extends AttrValBase{
        static final IAttrValUtil instance = new ValidEnum();

        @Override
        public boolean assertValid(MODIFIER modifier, String string) {
            // Only allow strings that match an enum
            if(Glob.UTIL_ENUM.isValid(modifier.getEnuType(), string.toUpperCase())){
                validString = string;
                errType = ERR_TYPE.NONE;
                return true;
            }
            else{
                validString = null;
                errType = ERR_TYPE.INVALID_ENUM;
                return false;
            }
        }

        @Override
        public boolean assertValid(MODIFIER modifier, Object object) {
            Class<?> enumClass = modifier.getEnuType();
            if(enumClass.getSimpleName().equals(object.getClass().getSimpleName())){
                validString = object.toString();
                errType = ERR_TYPE.NONE;
                return true;
            }
            else{
                validString = null;
                errType = ERR_TYPE.INVALID_ENUM;
                return false;
            }
        }
    }
}
