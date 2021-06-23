package tokenizer.composite2;

import tokenizer.composite.BaseComposite;
import tokenizer.composite.PairValidator;
import tokenizer.iface.IStringParser;

public class PairUnwrap extends BaseComposite {

    public PairUnwrap(){
        super(new PairValidator());
    }

    @Override
    public IStringParser parse() {
        parser.parse();

        if(!parser.isError()){
            int[] hitMap = parser.numericToArray();
            int i = 0, j = hitMap.length - 1;
            while(hitMap[i] == j && i <= parser.getLimit()){
                i++;
                j--;
            }
            if(i != 0){
                String text = parser.getText();
                parser.setText(text.substring(i, j + 1));
                parser.setNumericArray(this.buildCorrectedHitMap(i));
            }
        }

        return this;
    }
    // if text unwrapped, correct hitMap to reflect changes
    private int[] buildCorrectedHitMap(int n){
        String text = parser.getText();
        int[] oldMap = parser.numericToArray();
        int[] nuMap = new int[text.length()];
        for(int i = 0; i < nuMap.length; i++){
            int oldValue = oldMap[i + n];
            nuMap[i] = ((oldValue >= 0))? oldValue - n : oldValue;
        }
        return nuMap;
    }
}
