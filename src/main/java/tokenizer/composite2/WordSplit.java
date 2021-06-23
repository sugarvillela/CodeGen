package tokenizer.composite2;

import tokenizer.composite.BaseSplit;
import tokenizer.composite.WordTok;

public class WordSplit extends BaseSplit {
    public WordSplit(){
        super(new WordTok());
    }
}
