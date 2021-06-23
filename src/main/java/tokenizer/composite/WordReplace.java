package tokenizer.composite;

import tokenizer.iface.IStringParser;
import tokenizer.impl.WordMatch;

import java.util.List;

public class WordReplace extends BaseComposite {
    private final String replacementText;

    public WordReplace(String replacementText) {
        super(new WordMatch());
        this.replacementText = replacementText;
    }

    @Override
    public IStringParser parse() {
        parser.parse();
        List<Integer> hitMap = parser.numericToList();
        if(!hitMap.isEmpty()){
            String text = parser.getText();
            for(int i = hitMap.size() -2; i >= 0; i -= 2){
                text = text.substring(0, hitMap.get(i)) + replacementText + text.substring(hitMap.get(i + 1));
            }
            parser.setText(text);
        }
        return this;
    }
}
