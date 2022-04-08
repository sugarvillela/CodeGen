package codedef.codenode;

import codedef.enums.CODE_NODE;
import codedef.iface.ICodeNode;
import err.ERR_TYPE;
import runstate.Glob;

import java.util.ArrayList;
import java.util.List;

import static codedef.enums.MODIFIER.LIT_;

/**A wrapper for various types, enabling passing of primitives to CodeNode
 * Converts input array of ICodeNode, String, Integer, Long, Double to
 * output list of ICodeNode, configuration dependent on input pattern.
 *
 * If all same type
 *   If code node detected
 *     Add all to child list (node in -> single convert strategy -> acc list -> list out)
 *   Else If wrapped prim detected
 *     Wrap all in single code node literal (prim in -> single convert strategy -> acc single node -> list out)
 *   Else err
 * If not all same type
 *   If code node detected
 *     Add each to child list (node in -> ad hoc convert strategy -> acc list -> list out)
 *   Else If wrapped prim detected
 *     Wrap all in single code node literal (prim in -> ad hoc convert strategy -> acc node and dump -> acc list -> list out)
 *   Else err
 */
public class NodeWrapUtil {
    private static NodeWrapUtil instance;
    public static NodeWrapUtil initInstance(){
        return (instance == null)? (instance = new NodeWrapUtil()): instance;
    }

    /* Four integer enums to describe input type */
    private static final int COMMON_NODE = 0, COMMON_PRIM = 1, NODE_PRIM = 2, PRIM = 3;

    /* Utility to handle arrays in arg list */
    private final FlatArgUtil flatArgUtil;

    private NodeWrapUtil(){
        this.flatArgUtil =  new FlatArgUtil();
    }

    public List<ICodeNode> convert(Object... objects){
        // An initial step not related to conversion
        if(!flatArgUtil.isFlat(objects)){
            objects = flatArgUtil.flatten(objects);
        }

        // Detect pattern and get accumulator strategy
        InputPattern inputPattern = new InputPattern();
        IAccumulateStrategy accumulator = new StrategyFactory().getAccumulateStrategy(
            inputPattern.getPattern(objects),
            objects.length,
            inputPattern.getCommonClass()
        );

        // accumulate
        for(Object object : objects){
            accumulator.add(object);
        }
        return accumulator.getList();
    }

    //=====Class to detect makeup of args===============================================================================

    private static class InputPattern {
        private Class<?> commonClass;

        public int getPattern(Object... objects){
            this.commonClass = this.detectCommonClass(objects);
            boolean haveNode = this.detectNode(objects);

            if(commonClass == null){    // not all same type
                return haveNode? NODE_PRIM : PRIM;
            }
            else{                       // all same type
                return haveNode? COMMON_NODE : COMMON_PRIM;
            }
        }

        Class<?> getCommonClass(){
            return this.commonClass;
        }

        private Class<?> detectCommonClass(Object... objects){
            Class<?> curr, prev = null;
            int len = objects.length;
            if(len != 0){
                prev = objects[0].getClass();
                for(int i = 1; i < len; i++){
                    curr = objects[i].getClass();
                    if(!curr.getName().equals(prev.getName())){
                        return null;
                    }
                    prev = curr;
                }
            }
            return prev;
        }
        private boolean detectNode(Object... objects){
            for(Object object : objects){
                if(object instanceof ICodeNode){
                    return true;
                }
            }
            return false;
        }
    }

    //=====Convert Strategies===========================================================================================

    private interface IConvertStrategy {// Object type already validated
        String getString(Object object);
    }
    private static class StringConvert implements IConvertStrategy {
        @Override
        public String getString(Object object) {
            return (String)object;
        }
    }
    private static class IntConvert implements IConvertStrategy {
        @Override
        public String getString(Object object) {
            return String.format("0x%s", Integer.toHexString((Integer)object));
        }
    }
    private static class LongConvert implements IConvertStrategy {
        @Override
        public String getString(Object object) {
            return String.format("0x%sL", Long.toHexString((Long)object));
        }
    }
    private static class DoubleConvert implements IConvertStrategy {
        @Override
        public String getString(Object object) {
            return Double.toHexString((Double)object);
        }
    }

    //=====Acc Strategies===========================================================================================

    private interface IAccumulateStrategy {
        void add(Object child);
        List<ICodeNode> getList();
    }
    private static class AccCommonNode implements IAccumulateStrategy {
        protected final List<ICodeNode> accList;

        private AccCommonNode(int size) {
            accList = new ArrayList<>(size);
        }

        @Override
        public void add(Object child) {
            accList.add((ICodeNode)child);
        }

        @Override
        public List<ICodeNode> getList() {
            return accList;
        }
    }
    private static class AccCommonPrim implements IAccumulateStrategy {
        private final IConvertStrategy convertStrategy;
        protected final String[] accList;
        protected int i;

        private AccCommonPrim(StrategyFactory convertFactory, Class<?> commonClass, int size) {
            this.convertStrategy = convertFactory.getConvertStrategy(commonClass);
            accList = new String[size];
            i = 0;
        }

        @Override
        public void add(Object child) {
            accList[i++] = convertStrategy.getString(child);
        }

        @Override
        public List<ICodeNode> getList() {
            ICodeNode node = Glob.PROTOTYPE_FACTORY.get(CODE_NODE.LIT).putAttrib(LIT_, accList);
            List<ICodeNode> out = new ArrayList<>(1);
            out.add(node);
            return out;
        }
    }
    private static class AccNodeAndPrim extends AccCommonNode {
        protected final StrategyFactory convertFactory;

        private AccNodeAndPrim(StrategyFactory strategyFactory, int size) {
            super(size);
            this.convertFactory = strategyFactory;
        }

        @Override
        public void add(Object child) {
            if(child instanceof ICodeNode){
                accList.add((ICodeNode)child);
            }
            else{
                String value = this.convertFactory.getConvertStrategy(child.getClass()).getString(child);
                ICodeNode wrapperNode = Glob.PROTOTYPE_FACTORY.get(CODE_NODE.LIT).putAttrib(LIT_, value);
                accList.add(wrapperNode);
            }
        }
    }
    private static class AccPrim implements IAccumulateStrategy {
        protected final StrategyFactory strategyFactory;
        protected final String[] accList;
        protected int i;

        private AccPrim(StrategyFactory strategyFactory, int size) {
            this.strategyFactory = strategyFactory;
            accList = new String[size];
            i = 0;
        }

        @Override
        public void add(Object child) {
            accList[i++] = this.strategyFactory.getConvertStrategy(child.getClass()).getString(child);
        }
        @Override
        public List<ICodeNode> getList() {
            ICodeNode node = Glob.PROTOTYPE_FACTORY.get(CODE_NODE.LIT).putAttrib(LIT_, accList);
            List<ICodeNode> out = new ArrayList<>(1);
            out.add(node);
            return out;
        }
    }

    //=====Strategy Factory=============================================================================================

    private static class StrategyFactory {
        IConvertStrategy s, i, l, d;

        public IConvertStrategy getConvertStrategy(Class<?> classType) {
            String boris = classType.getSimpleName();
            switch(classType.getSimpleName()){
                case "String":
                    return (s == null)? (s = new StringConvert()) : s;
                case "Integer":
                    return (i == null)? (i = new IntConvert()) : i;
                case "Long":
                    return (l == null)? (l = new LongConvert()) : l;
                case "Double":
                    return (d == null)? (d = new DoubleConvert()) : d;
                default:
                    Glob.ERR.kill(
                            ERR_TYPE.DISALLOWED_NESTING,
                            String.format("Can't convert %s to CodeNode", classType.getSimpleName())
                    );
                    return null;
            }
        }

        public IAccumulateStrategy getAccumulateStrategy(int inputPattern, int len, Class<?> commonClass) {
            switch(inputPattern){
                case COMMON_NODE:   // all ICodeNode
                    return new AccCommonNode(len);
                case COMMON_PRIM:   // all same prim type
                    return new AccCommonPrim(this, commonClass, len);
                case NODE_PRIM:     // various ICodeNode and prim
                    return new AccNodeAndPrim(this, len);
                default://PRIM      // various prim
                    return new AccPrim(this, len);
            }
        }
    }

    //=====An extra step not related to conversion: detect array args and flatten=======================================

    private boolean isFlat(Object... objects){
        return flatArgUtil.isFlat(objects);
    }

    private Object[] flatten(Object... objects){
        try{
            return flatArgUtil.flatten(objects);
        }
        catch(IllegalArgumentException e){
            String desc = String.format(
                    "Found %s \nTake care when passing array");
            Glob.ERR.kill(ERR_TYPE.INVALID_ARG, desc);
            return null;
        }
    }
}
