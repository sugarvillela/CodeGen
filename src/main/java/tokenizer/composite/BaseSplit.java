package tokenizer.composite;

import tokenizer.iface.IStringParser;

import java.util.List;

/**A char-based or word-based splitter using the CharTok/WordTok algorithm.
 * Returns a pair of size 2 (toArray or toList).
 * Element 0 populates with pre-split text; element 1 has post-split text.
 * If no splits are possible, element 1 contains null.
 *
 * Bad usages:
 * If tokenizeDelimiter is true, element 1 contains the delimiter and
 * post-split text is discarded
 *
 * You can override the size with setLimit().
 * SplitUtil always pads unfilled elements with null
 */
public class BaseSplit extends BaseComposite {
    protected BaseSplit(IStringParser parser) {
        super(parser);
        parser.setLimit(2);
    }
    @Override
    public IStringParser parse() {
        parser.parse();
        List<String> list = parser.toList();
        while(list.size() < parser.getLimit()){
            list.add(null);
        }
        return this;
    }

    @Override
    public String getText() {
        return parser.toList().get(0);
    }
}
