package codedef.codenode;

import err.ERR_TYPE;
import runstate.Glob;

import java.util.ArrayList;
import java.util.List;

/** A class to handle arrays in var arg input.
 *  The workaround is to flatten the array and merge it with
 *  the var arg array.
 *  So [1, 2, [a, b, c], 3] becomes [1, 2, a, b, c, 3]
 *
 *  Supports mixing types, though in some languages there would be no reason to do that.
 *
 *  Limitations: Only flattens one level (not recursive)
 *               Only primitive data types: String, int, long, float, double
 *  Error handling detects invalid arrays.
 *    Other invalid input is caught by NodeWrapUtil
 *
 *  Known issue: Ambiguity between doubles as whole numbers and int.
 *               May confuse 43 with 43.0
 *  Workaround:  Add a 'd' after number to guarantee it is a double
 */
public class FlatArgUtil {
    List<Object> acc;

    public boolean isFlat(Object... objects){
        for(Object object : objects){
            if(object.getClass().isArray() || (object instanceof List)){
                return false;
            }
        }
        return true;
    }
    public Object[] flatten(Object... objects){
        this.acc =  new ArrayList<>();
        try{
            for(Object outer : objects){
                if(!this.parseArray(outer) && !this.parseList(outer)){
                    acc.add(outer);
                }
            }
        }
        catch(IllegalArgumentException e){
            String desc = String.format(
                    "Found %s in array or list [%s] \n%s",
                    e.getMessage(),
                    this.objectsToString(objects),
                    this.usageWarning()
            );
            Glob.ERR.kill(ERR_TYPE.INVALID_ARG, desc);
        }
        return this.acc.toArray(new Object[0]);
    }
    private boolean parseArray(Object outer) throws IllegalArgumentException {
        if(outer.getClass().isArray()){
            Class<?> arrClass = outer.getClass().getComponentType();
            if(arrClass == null){
                throw new IllegalArgumentException(String.valueOf(outer));
            }
            else{
                if("String".equals(arrClass.getSimpleName())){
                    for(String inner : (String[])outer){
                        acc.add(inner);
                    }
                }
                else if(arrClass.equals(Integer.TYPE)){
                    for(int inner : (int[])outer){
                        acc.add(inner);
                    }
                }
                else if(arrClass.equals(Long.TYPE)){
                    for(long inner : (long[])outer){
                        acc.add(inner);
                    }
                }
                else if(arrClass.equals(Double.TYPE)){
                    for(double inner : (double[])outer){
                        acc.add(inner);
                    }
                }
                else if(arrClass.equals(Float.TYPE)){
                    for(float inner : (float[])outer){
                        acc.add(inner);
                    }
                }
                else{
                    throw new IllegalArgumentException(String.valueOf(outer));
                }
                return true;
            }
        }
        return false;
    }

    private boolean parseList(Object outer) throws IllegalArgumentException {
        if(outer instanceof List && validateList((List)outer)){
            acc.addAll((List)outer);
            return true;
        }
        return false;
    }
    private boolean validateList(List<Object> list) throws IllegalArgumentException{
        for(Object o : list){
            if(
                !(o instanceof String) &&
                !(o instanceof Integer) &&
                !(o instanceof Double) &&
                !(o instanceof Float) &&
                !(o instanceof Boolean)
            ){
                throw new IllegalArgumentException(String.valueOf(o));
            }
        }
        return true;
    }

    //=====For error message============================================================================================

    private String objectsToString(Object... objects){
        List<String> list = new ArrayList<>();
        for(Object object : objects){
            list.add(String.valueOf(object));
        }
        return String.join(", ", list);
    }
    private String usageWarning(){
        return  "Array and List args are flattened into the variable arg array.\n" +
                "Allowed types in flattened arg: String, int, long, double, float, boolean";
    }
}
