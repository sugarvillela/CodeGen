package langgen.interfaces;

//import toksource.Base_TextSource;

public interface ITextFile extends IWidget{
    public interface ITextFileBuilder{
        ITextFileBuilder setFile(String fileName);
        ITextFileBuilder setFile(Object textSource);//-- (Base_TextSource textSource)
        ITextFile build();
    }
}
