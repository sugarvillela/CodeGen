package package1;
import someDir1.someFile1 ;
import someDir2.someFile2 ;

public class class1 {
    /*
    * This here is a long string comment, which
    * will be split across two or more lines, assuming
    * its length exceeds the the allowed characters
    * in a comment line which is somewhat less
    * than a regular line
    */
    String classField1 = "comprende";
    public void method1 ( int arg1, String arg2, boolean arg3 ) {
        // This here is a short string comment, which
        // will also be split across two or more lines,
        // assuming its length exceeds the the allowed
        // characters in a comment line
        switch ( arg3 ) {
            case true:
                arg3 = false;
                break;
            case null:
            case false:
                arg3 = true;
                break;
            default:
                arg3 = true;
                break;
        }
        if ( ( arg1 <= 48 ) || !( arg2 == 17 ) ) {
            classField1 = "no comprende";
        }
        else {
            arg1 = 69;
        }
    }

}

