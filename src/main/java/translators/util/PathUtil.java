package translators.util;

import translators.iface.IPathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PathUtil implements IPathUtil {
    private final List<String> filePath, codePath;
    private String xPath, codePathSeparator;

    public PathUtil() {
        this.filePath = new ArrayList<>();
        this.codePath = new ArrayList<>();
        this.codePathSeparator = ".";
    }

    @Override
    public IPathUtil setCodePathSeparator(String codePathSeparator) {
        this.codePathSeparator = codePathSeparator;
        return this;
    }

    @Override
    public IPathUtil setExternalPath(String xPath) {
        this.xPath = xPath;
        return this;
    }

    @Override
    public IPathUtil reset() {
        filePath.clear();
        codePath.clear();
        if(xPath != null && !xPath.isEmpty()){
            filePath.add(xPath);
        }
        return this;
    }

    @Override
    public void appendFilePath(String dirName) {
        if(dirName != null && !dirName.isEmpty()){
            filePath.add(dirName);
        }
    }

    @Override
    public void appendCodePath(String dirName) {
        if(dirName != null && !dirName.isEmpty()){
            codePath.add(dirName);
        }
    }

    @Override
    public String getFilePath(String fileName) {
        return(fileName == null || fileName.isEmpty())?
            String.join(File.separator, filePath) :
            String.join(File.separator, filePath) + File.separator + fileName;
    }

    @Override
    public String getCodePath(String fileName) {
        return(fileName == null || fileName.isEmpty())?
            String.join(codePathSeparator, codePath) :
            String.join(codePathSeparator, codePath) + codePathSeparator + fileName;
    }
}
