package tokenizer.composite;

import tokenizer.iface.IStringParser;
import tokenizer.impl.CharMatch;

import java.util.List;

public class CharReplace extends BaseComposite {
    private final String replacementText;

    public CharReplace(String replacementText) {
        super(new CharMatch());
        this.replacementText = replacementText;
    }

    @Override
    public IStringParser parse() {
        parser.parse();
        List<Integer> hitMap = parser.numericToList();
        if(!hitMap.isEmpty()){
            String text = parser.getText();
            for(int i = hitMap.size() -1; i >= 0; i--){
                int j = hitMap.get(i);
                text = text.substring(0, j) + replacementText + text.substring(j + 1);
            }
            parser.setText(text);
        }
        return this;
    }
}
