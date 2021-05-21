package langdef.util;

import langdef.*;
import langdefalgo.iface.LANG_STRUCT;

import java.util.ArrayList;
import java.util.Collections;

import static langdef.STRUCT_KEYWORD.LANG_ROOT_1;
import static langdef.STRUCT_SYMBOL.*;


/** Keeping hard-coded lang def names in langDef package, this class exports
 * lists by interface type or by any structure needed */
public class EnumsByType {
    private static EnumsByType instance;

    public static EnumsByType initInstance(){
        return (instance == null)? (instance = new EnumsByType()): instance;
    }

    private EnumsByType(){}

    public ArrayList<LANG_STRUCT> allFrontEndLangStruct(){
        ArrayList<LANG_STRUCT> list = new ArrayList<>();
        Collections.addAll(list, STRUCT_SYMBOL.values());
        return list;
    }

    public ArrayList<LANG_STRUCT> allLangStructures(){
        ArrayList<LANG_STRUCT> list = new ArrayList<>();
        Collections.addAll(list, STRUCT_SYMBOL.values());
        return list;
    }
    public LANG_STRUCT enumLangRoot1(){
        return LANG_ROOT_1;
    }
    public LANG_STRUCT jsonSwap(){
        return JSON_SWAP;
    }
    public LANG_STRUCT jsonObj(){
        return JSON_OBJ;
    }
    public LANG_STRUCT jsonArr(){
        return JSON_ARR;
    }

}
