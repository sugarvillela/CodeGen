package tokenizer.composite2;

import tokenizer.composite.BaseSplit;
import tokenizer.composite.CharTok;

public class CharSplit extends BaseSplit {
    public CharSplit(){
        super(new CharTok());
    }
}
