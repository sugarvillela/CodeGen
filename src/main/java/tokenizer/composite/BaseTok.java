package tokenizer.composite;

import tokenizer.iface.IStringParser;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTok extends BaseComposite {
    protected final List<String> tokens;

    protected BaseTok(IStringParser parser) {
        super(parser);
        tokens = new ArrayList<>();
    }

    @Override
    public List<String> toList() {
        return this.tokens;
    }

    @Override
    public String[] toArray() {
        return this.tokens.toArray(new String[tokens.size()]);
    }

    /**If adding current token does not reach limit, add it and return true;
     * else add rest of string as single token and return false
     * @return true unless current add reaches limit */
    protected boolean tryAddToken(String text, int begin, int end){
        if((tokens.size() + 1) >= parser.getLimit()){
            tokens.add(text.substring(begin));
            return false;
        }
        else{
            tokens.add(text.substring(begin, end));
            return true;
        }
    }
}
