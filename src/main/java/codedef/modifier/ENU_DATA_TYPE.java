package codedef.modifier;

import err.ERR_TYPE;

public enum ENU_DATA_TYPE {
    NULL        (new StringTypeUtil()),
    STRING      (new StringTypeUtil()),
    INT         (new IntTypeUtil()),
    FLOAT       (new FloatTypeUtil()),
    DOUBLE      (new DoubleTypeUtil()),
    BOOLEAN     (new BooleanTypeUtil()),
    VOID        (new StringTypeUtil()),
    UNK         (new StringTypeUtil())
    ;

    private final IDataTypeUtil dataTypeUtil;

    ENU_DATA_TYPE(IDataTypeUtil dataTypeUtil) {
        this.dataTypeUtil = dataTypeUtil;
    }

    public static ENU_DATA_TYPE fromString(String value){
        try{
            return valueOf(value);
        }
        catch(IllegalArgumentException e){
            return null;
        }
    }

    public static ENU_DATA_TYPE findCompatibleType(String value, boolean isMethod){
        ENU_DATA_TYPE[] choices = {INT, FLOAT, BOOLEAN};// order is important
        for(ENU_DATA_TYPE choice : choices){
            if(choice.assertValidData(value) == ERR_TYPE.NONE){
                return choice;
            }
        }
        return (isMethod)? VOID : STRING;
    }

    public ERR_TYPE assertValidData(String data) {
        return dataTypeUtil.assertValidDataItem(data);
    }

    public String getDefaultValue() {
        return dataTypeUtil.getDefaultValue();
    }

    private interface IDataTypeUtil{
        ERR_TYPE assertValidDataItem(String data);
        String getDefaultValue();
    }
    private static class StringTypeUtil implements IDataTypeUtil{
        @Override
        public ERR_TYPE assertValidDataItem(String data) {
            return (data == null || data.isEmpty() || "TRUE".equals(data) || "FALSE".equals(data))?
                    ERR_TYPE.INVALID_STRING : ERR_TYPE.NONE;
        }

        @Override
        public String getDefaultValue() {
            return "";
        }
    }
    private static class BooleanTypeUtil implements IDataTypeUtil{
        @Override
        public ERR_TYPE assertValidDataItem(String data) {
            return ("TRUE".equals(data) || "FALSE".equals(data)) ? ERR_TYPE.NONE : ERR_TYPE.INVALID_BOOL;
        }

        @Override
        public String getDefaultValue() {
            return "FALSE";
        }
    }
    private static abstract class NumTypeUtil implements IDataTypeUtil{
        public String getDefaultValue() {
            return "0";
        }
    }
    private static class IntTypeUtil extends NumTypeUtil{
        @Override
        public ERR_TYPE assertValidDataItem(String data) {
            try{
                Integer.parseInt(data);
                return ERR_TYPE.NONE;
            }
            catch(Exception e){
                return ERR_TYPE.INVALID_INT;
            }
        }
    }
    private static class DoubleTypeUtil extends NumTypeUtil{
        @Override
        public ERR_TYPE assertValidDataItem(String data) {
            try{
                Double.parseDouble(data);
                return ERR_TYPE.NONE;
            }
            catch(Exception e){
                return ERR_TYPE.INVALID_FLOAT;
            }
        }
    }
    private static class FloatTypeUtil extends NumTypeUtil{
        @Override
        public ERR_TYPE assertValidDataItem(String data) {
            try{
                Float.parseFloat(data);
                return ERR_TYPE.NONE;
            }
            catch(Exception e){
                return ERR_TYPE.INVALID_FLOAT;
            }
        }
    }

}
