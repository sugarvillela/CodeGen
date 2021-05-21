package utilfile;

import datasource.iface.IDataSource;
import readnode.iface.IReadNode;

import java.util.ArrayList;

/** All tests do one of two things: iterate for predictable output or overrun in predictable ways.
 *  So the common action happens in the static functions below, with input and output in the tests as expected.
 *
 *  Note: DataSourceBase, line 18 provides a unique identifier for list, array implementations
 *  This can break tests because the identifier is assigned sequentially (thus coupling it with other tests).
 *  Comment out the ++ and the tests will work
 */
public abstract class TestUtil {
    public static final int D_NONE = 0, D_FRIENDLY = 1, D_CSV = 2, D_TEXT = 3, D_EVENT = 4;
    public static final boolean display = false;
    public static final boolean displayCsv = true;

    public static String iterateAndJoin(IDataSource dataSource, int displayMode){
        return iterateAndJoin(dataSource, displayMode,40);
    }

    public static String iterateAndJoin(IDataSource dataSource, int displayMode, int overRun){
        if(display || displayCsv){
            System.out.println("=============================");
        }
        ArrayList<String> data = new ArrayList<>();
        int i = 0;
        while(dataSource.hasNext()){
            IReadNode node = dataSource.next();
            String text;
            switch(displayMode){
                case D_TEXT:
                    text = (node == null)? "null" : node.text();
                    break;
                case D_FRIENDLY:
                    text = (node == null)? "null" : node.friendlyString();
                    break;
                case D_EVENT:
                    text = (node == null)? "null" :
                            (node.hasTextEvent())? node.textEvent().csvString() : node.text();
                    break;
                default:
                    text = (node == null)? "null" : node.csvString();
            }
            data.add(text);
            if(overRun < i++){
                System.out.println("overrun!!!");
                break;
            }
        }
        if(displayMode != D_NONE){
            System.out.println("\"" + String.join("|\" + \n\"", data)  + "\"");
        }
        return String.join("|", data);
    }

    public static String overrunAndJoin(IDataSource dataSource, int overRun){
        ArrayList<String> data = new ArrayList<>();
        for(int i = 0; i< overRun; i++){
            IReadNode node = dataSource.next();
            String text = (node == null)? "null" : node.csvString();
            data.add(text);
            if(display){
                System.out.println(text);
            }
            else if(displayCsv){
                System.out.println("\"" + text + "|\" + ");
            }
        }
        return String.join("|", data);
    }
}
