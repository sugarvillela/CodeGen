package stack.iface;

import iface_global.ICsv;
import langdefalgo.iface.LANG_STRUCT;

import java.util.ArrayList;

public interface IStackLog extends ICsv {//2-d
    void addIteration(ArrayList<LANG_STRUCT> iteration);
    ArrayList<LANG_STRUCT> lastIteration();
    ArrayList<LANG_STRUCT> lastIteration(int stackLevel);
    LANG_STRUCT lastIterationItem(int stackLevel);
    ArrayList<ArrayList<LANG_STRUCT>> allIterations();
    ArrayList<String> reportArray();

    // ICsv
    //String friendlyString();
    //String csvString();
}