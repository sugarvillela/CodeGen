package utilfile;

import java.io.File;

public class FileNameUtil {
    public static final String DEFAULT_PATH = "src"+ File.separator+"test"+File.separator+"resources";

    private static FileNameUtil instance;

    public static FileNameUtil initInstance(){
        return (instance == null)? (instance = new FileNameUtil()): instance;
    }

    private FileNameUtil(){}

    private String extension;

    public FileNameUtil setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public String fixFileName(String fileName){
        return (fileName.endsWith(extension))?
                fileName : fileName + extension;
    }

    public String mergePath(String... pathParts){
        return String.join(File.separator, pathParts);
    }

    public String fixAndMergePath(String... pathParts){
        int last = pathParts.length - 1;
        pathParts[last] = fixFileName(pathParts[last]);
        return mergePath(pathParts);
    }

    public String mergeDefaultPath(String fileName){
        return DEFAULT_PATH + File.separator + fileName;
    }

    public String getFileNameFromPath(String path){
        int index = path.lastIndexOf(File.separator);
        return (index == -1)? path : path.substring(index + 1);
    }
}
