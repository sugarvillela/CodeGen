package tokenizer.composite;

import tokenizer.iface.IStringParser;
import tokenizer.impl.PairMatch;

public class PairValidator extends BaseComposite {
    private boolean errState;
    private int numSymbols;

    public PairValidator(){
        super(new PairMatch());
    }

    @Override
    public IStringParser parse() {
        parser.parse();

        int[] hitMap = parser.numericToArray();
        errState = false;
        numSymbols = 0;
        for(int i = 0; i < hitMap.length; i++){
            if(hitMap[i] == PairMatch.ORPHAN_SYMBOL){
                errState = true;
            }
            else if (hitMap[i] != PairMatch.NOT_SET){
                numSymbols++;
            }
        }

        return this;
    }

    @Override
    public int numeric() {
        return numSymbols;
    }

    @Override
    public boolean isError() {
        return errState;
    }
}
