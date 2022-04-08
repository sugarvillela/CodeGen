package codegen;

import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.ITextOutput;
import com.strobel.decompiler.PlainTextOutput;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
	    System.out.println("Hello world!!!");
        for(int i = 0; i < args.length; i++){
            System.out.println(args[i]);
        }
    }
}
