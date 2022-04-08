package translators.iface;

/** Holds two values: code path and file path
 *  Code path starts empty and adds directories from code source root
 *  File path starts with path to sources root and adds directories from there */
public interface IPathUtil {
    /** Use only if '.' is not the right path separator
     * @param codePathSeparator single char string to separate code path directories
     * @return self for builder pattern */
    IPathUtil setCodePathSeparator(String codePathSeparator);

    /** Sets absolute path to code source root for building file path
     * @param xPath Path with separators already added
     * @return self for builder pattern */
    IPathUtil setExternalPath(String xPath);

    /** Clears code and file path; leaves external path in file path
     * @return self for builder pattern */
    IPathUtil reset();

    /** Add a directory name to path, for building path to current file
     * @param dirName current directory name */
    void appendFilePath(String dirName);

    /** Add a directory name to path, for building path to current class
     * @param dirName current directory name */
    void appendCodePath(String dirName);

    /**@param fileName fileName to complete the path
     * @return full path with file separators, for writing files */
    String getFilePath(String fileName);

    /**@param fileName fileName to complete the path
     * @return full path with code separators, for adding path to code */
    String getCodePath(String fileName);
}
