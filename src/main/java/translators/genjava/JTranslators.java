package translators.genjava;

import codedef.iface.ICodeNode;
import codedef.modifier.CODE_NODE;
import codedef.modifier.MODIFIER;
import err.ERR_TYPE;
import langformat.iface.*;
import langformat.impl.Accumulator;
import langformat.impl.FormatStrategyCType;
import langformat.impl.Formatter;
import langformat.impl.NullableUtil;
import runstate.Glob;
import translators.iface.ITranslator;
import translators.iface.ITranslatorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static codedef.modifier.MODIFIER.*;

public class JTranslators implements ITranslatorFactory {
    public JTranslators(){
        Formatter.initInstance(new FormatStrategyCType());
    }

    @Override
    public ITranslator get(CODE_NODE codeNodeEnum) {
        switch(codeNodeEnum){
            case GLOB:
                return new GlobJava();
            case PACKAGE:
                return new PackageJava();
            case FILE:
                return new FileJava();
            case IMPORT:
                return new ImportJava();
            case IMPORT_ITEM:
                return new ImportItemJava();
            case CLASS:
                return new ClassJava();
            case CLASS_FIELD:
                return new ClassFieldJava();
            case METHOD:
            case CONSTRUCTOR:
                return new MethodJava();
            case METHOD_ARGS:
                return new MethodArgsJava();
            case METHOD_ARG:
                return new MethodArgJava();
            case IF_ELSE:
                return new IfElseJava();
            case ELSE:
                return new ElseJava();
            case CODE_BLOCK:
                return new CodeBlockJava();
            case VAR_DEF:
                return new VarDefJava();
            case CONDITIONAL:
            case PAR_BLOCK:
                return new ParBlockJava();
            case BOOL_BLOCK:
                return new BoolBlockJava();
            case STATEMENT:
                return new StatementJava();
            case EXPR:
                return new ExprJava();
            case CONJUNCTION:
                return new ConjunctionJava();
            case COMPARISON:
                return new ComparisonJava();
            case LIT:
            case RETURN:
            case ASSIGN:
            case BREAK:
                return new LitJava();
            case COMMENT:
                return new CommentJava();
            case COMMENT_LONG:
                return new CommentLongJava();
            case SWITCH:
                return new SwitchJava();
            case SWITCH_CASE:
                return new SwitchCaseJava();
            case SWITCH_DEFAULT:
                return new SwitchCaseDefaultJava();
            default:
                Glob.ERR_DEV.kill("Reached default with: " + codeNodeEnum.toString());
                return null;
        }
    }

    public static abstract class TranslatorBase implements ITranslator{
        protected final IFormatter formatter;
        protected final INullableUtil nullableUtil;
        protected List<ICodeNode> headerChildren, bodyChildren;
        protected ICodeNode codeNode;
        protected StringBuilder code;

        public TranslatorBase() {
            formatter = Formatter.getInstance();
            nullableUtil = NullableUtil.initInstance();
        }

        @Override
        public String format(ICodeNode codeNode) {
            return formatter.formatAll(go(codeNode));
        }

        @Override
        public String go(ICodeNode codeNode) {
            this.codeNode = codeNode;
            this.initChildren(codeNode);
            code = new StringBuilder();
            return head().body().foot().finish();
        }
        @Override
        public ITranslator head() {
            return this;
        }
        @Override
        public ITranslator body() {
            for(ICodeNode childNode : bodyChildren){
                String text = childNode.translator().go(childNode);
                code.append(text);
            }
            return this;
        }
        @Override
        public ITranslator foot() {
            return this;
        }
        @Override
        public String finish() {
            return code.toString();
        }

        protected void initChildren(ICodeNode codeNode){
            headerChildren = new ArrayList<>();
            bodyChildren = new ArrayList<>();

            for(ICodeNode childNode : codeNode.getChildren()){
                if(nullableUtil.extractBoolean(childNode.getAttribModifier().get(IS_HEADER))){
                    headerChildren.add(childNode);
                }
                else{
                    bodyChildren.add(childNode);
                }
            }

        }
    }

    public static class GlobJava  extends TranslatorBase {
        @Override
        public ITranslator head() {
            // TODO set formatting options
            return this;
        }
    }

    public static class PackageJava extends TranslatorBase {
        @Override
        public ITranslator head() {
            // TODO check/build directory structure
            return this;
        }
    }

    public static class FileJava extends TranslatorBase {
        @Override
        public ITranslator head() {
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            String[] values;
            String value;
            if((value = nullableUtil.extractString(attributes.get(PATH))) != null){
                formatter.addStatement(code, "package " + value);
            }
            for(ICodeNode childNode : headerChildren){// import
                code.append(childNode.translator().go(childNode));
            }
            return this;
        }
    }

    public static class ImportJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            for(ICodeNode childNode : bodyChildren){// importItem
                code.append(childNode.translator().go(childNode));
            }
            formatter.addBlank(code);
            return this;
        }
    }

    public static class ImportItemJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            String value;
            formatter.addWord_(code, "import");
            if(nullableUtil.extractBoolean(attributes.get(STATIC))){
                formatter.addWord_(code, "static");
            }
            if((value = nullableUtil.extractString(attributes.get(LIT_VAL))) != null){
                formatter.addStatement(code, value);
            }
            return this;
        }
    }

    public static class ClassJava extends TranslatorBase {
        @Override
        public ITranslator head() {
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            String value;
            if((value = nullableUtil.extractString(attributes.get(VISIBILITY))) != null){
                formatter.addWord_(code, value.toLowerCase());
            }
            if(nullableUtil.extractBoolean(attributes.get(STATIC))){
                ICodeNode parentNode = codeNode.getParentNode();
                if(parentNode != null && parentNode.codeNodeEnum() != CODE_NODE.CLASS){
                    Glob.ERR.check(ERR_TYPE.LANGUAGE_ERR, "static");
                }
                formatter.addWord_(code, "static");
            }
            if(nullableUtil.extractBoolean(attributes.get(ABSTRACT))){
                formatter.addWord_(code, "abstract");
            }
            if(nullableUtil.extractBoolean(attributes.get(FINAL))){
                formatter.addWord_(code, "final");
            }
            formatter.addWord_(code, "class");
            if((value = nullableUtil.extractString(attributes.get(NAME))) != null){
                formatter.addWord_(code, value);
            }
            if((value = nullableUtil.extractCsv(attributes.get(EXTENDS))) != null){
                formatter.addWord_(code, "extends " + value);
            }
            if((value = nullableUtil.extractCsv(attributes.get(IMPLEMENTS))) != null){
                formatter.addWord_(code, "implements " + value);
            }
            formatter.addLine(code, "{");
            return this;
        }
        @Override
        public ITranslator foot() {
            formatter.addLine(code, "}");
            return this;
        }
    }

    public static class MethodJava extends TranslatorBase {
        @Override
        public ITranslator head() {
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            String value;
            if(nullableUtil.extractBoolean(attributes.get(OVERRIDE))){
                formatter.addLine(code, "@Override");
            }
            if((value = nullableUtil.extractString(attributes.get(VISIBILITY))) != null){
                formatter.addWord_(code, value.toLowerCase());
            }
            if(nullableUtil.extractBoolean(attributes.get(STATIC))){
                formatter.addWord_(code, "static");
            }
            if(nullableUtil.extractBoolean(attributes.get(ABSTRACT))){
                formatter.addWord_(code, "abstract");
            }
            if(nullableUtil.extractBoolean(attributes.get(FINAL))){
                formatter.addWord_(code, "final");
            }
            if((value = nullableUtil.extractString(attributes.get(DATA_TYPE))) != null){
                formatter.addWord_(code, value.toLowerCase());
            }
            if((value = nullableUtil.extractString(attributes.get(NAME))) != null){
                formatter.addWord_(code, value);
            }

            for(ICodeNode childNode : headerChildren){// method args
                formatter.addWord_(code, childNode.translator().go(childNode));
            }
            return this;
        }
    }

    public static class MethodArgsJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            formatter.addWord_(code, "(");
            IAccumulator acc = new Accumulator(", ");
            for(ICodeNode childNode : bodyChildren){
                acc.add(childNode.translator().go(childNode).trim());
            }
            formatter.addWord_(code, acc.finish());
            formatter.addWord_(code, ")");
            return this;
        }
    }

    public static class IfElseJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            formatter.addWord_(code, "if");
            for(ICodeNode childNode : headerChildren){//conditional, connector
                formatter.addWord_(code, childNode.translator().go(childNode));
            }
            return this;
        }
    }

    public static class ElseJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            formatter.addWord_(code, "else");
            return this;
        }
    }

    public static class CodeBlockJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            formatter.addLine(code, "{");
            for(ICodeNode childNode : bodyChildren){
                formatter.addLine(code, childNode.translator().go(childNode));
            }
            formatter.addLine(code, "}");
            return this;
        }
    }

    public static class ParBlockJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            formatter.addWord_(code, "(");
            IAccumulator acc = new Accumulator(" ");
            for(ICodeNode childNode : bodyChildren){//conditional, connector
                acc.add(childNode.translator().go(childNode));
            }
            formatter.addWord_(code, acc.finish());
            formatter.addWord_(code, ")");
            return this;
        }
    }

    public static class BoolBlockJava extends ParBlockJava {
        @Override
        public ITranslator head(){
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            if(nullableUtil.extractBoolean(attributes.get(NEGATE))){
                formatter.addWord(code, "!");
            }
            return this;
        }
    }

    public static class StatementJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            IAccumulator acc = new Accumulator(" ");
            for(ICodeNode childNode : bodyChildren){//conditional, connector
                acc.add(childNode.translator().go(childNode));
            }
            formatter.addStatement(code, acc.finish());
            return this;
        }
    }

    public static class ExprJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            IAccumulator acc = new Accumulator(" ");
            for(ICodeNode childNode : bodyChildren){//conditional, connector
                acc.add(childNode.translator().go(childNode));
            }
            formatter.addWord_(code, acc.finish());
            return this;
        }
    }

    public static class LitJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            String value;
            if((value = nullableUtil.extractString(attributes.get(LIT_VAL))) != null){
                formatter.addWord_(code, value);
            }
            return this;
        }
    }

    public static class ConjunctionJava extends TranslatorBase {
        protected String mapEnumeratedValue(String delimiterEnum){
            switch(delimiterEnum){
                case "AND":
                    return "&&";
                case "OR":
                    return "||";
                default:
                    return null;
            }
        }
        @Override
        public ITranslator body(){
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            String value;
            if((value = nullableUtil.extractString(attributes.get(CONJUNCTION_TYPE))) != null){
                formatter.addWord_(code, mapEnumeratedValue(value));
            }
            return this;
        }
    }

    public static class ComparisonJava extends ConjunctionJava {
        @Override
        protected String mapEnumeratedValue(String delimiterEnum){
            switch(delimiterEnum){
                case "EQ":
                    return "==";
                case "GT":
                    return ">";
                case "LT":
                    return "<";
                case "GTE":
                    return ">=";
                case "LTE":
                    return "<=";
                case "NE":
                    return "!=";
                default:
                    return null;
            }
        }
        @Override
        public ITranslator body(){
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            String value;
            if((value = nullableUtil.extractString(attributes.get(COMPARISON_TYPE))) != null){
                formatter.addWord_(code, mapEnumeratedValue(value));
            }
            return this;
        }
    }

    public static class VarDefJava extends TranslatorBase {
        protected String mapDataTypeEnum(String dataTypeEnum){
            switch(dataTypeEnum){
                case "STRING":
                    return "String";
                case "UNK":
                    Glob.ERR.kill(ERR_TYPE.SYNTAX);
                default:
                    return dataTypeEnum.toLowerCase();
            }
        }
        protected String bodyText(){
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            String value;
            IAccumulator acc = new Accumulator(" ");
            if((value = nullableUtil.extractString(attributes.get(DATA_TYPE))) != null){
                acc.add(this.mapDataTypeEnum(value));
            }
            if((value = nullableUtil.extractString(attributes.get(NAME))) != null){
                acc.add(value);
            }
            if((value = nullableUtil.extractString(attributes.get(VAR_VALUE))) != null){
                acc.add("= " + value);
            }
            return acc.finish();
        }
        @Override
        public ITranslator body(){
            formatter.addWord_(code, bodyText());
            return this;
        }
    }

    public static class ClassFieldJava extends VarDefJava {
        @Override
        public ITranslator body(){
            formatter.addStatement(code, bodyText());
            return this;
        }
    }

    public static class MethodArgJava extends VarDefJava {
        protected String bodyText(){
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            String value;
            IAccumulator acc = new Accumulator(" ");
            if((value = nullableUtil.extractString(attributes.get(DATA_TYPE))) != null){
                formatter.addWord_(code, this.mapDataTypeEnum(value));
            }
            if((value = nullableUtil.extractString(attributes.get(NAME))) != null){
                formatter.addWord_(code, value);
            }
            return acc.finish();
        }
    }

    public static class CommentJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            String value = nullableUtil.extractString(attributes.get(LIT_VAL));
            if(value != null){
                formatter.addComment(code, value, "//");
            }
            return this;
        }
    }

    public static class CommentLongJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            String value = nullableUtil.extractString(attributes.get(LIT_VAL));
            if(value != null){
                formatter.addComment(code, value, "/*", "*", "*/");
            }
            return this;
        }
    }

    public static class SwitchJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            String value = nullableUtil.extractString(codeNode.getAttribModifier().get(LIT_VAL));
            formatter.addWord_(code, "switch ( " + value + " )");
            return this;
        }

        @Override
        public ITranslator body(){
            formatter.addLine(code, "{");
            for(ICodeNode childNode : bodyChildren){
                formatter.addLine(code, childNode.translator().go(childNode));
            }
            formatter.addLine(code, "}");
            return this;
        }
    }

    public static class SwitchCaseJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            String value = nullableUtil.extractString(codeNode.getAttribModifier().get(LIT_VAL));
            formatter.addLine(code, "case " + value + ":");
            formatter.addInc(code);

            for(ICodeNode childNode : bodyChildren){
                formatter.addLine(code, childNode.translator().go(childNode));
            }
            formatter.addDec(code);
            return this;
        }
    }

    public static class SwitchCaseDefaultJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            String value = nullableUtil.extractString(codeNode.getAttribModifier().get(LIT_VAL));
            formatter.addLine(code, "default:");

            for(ICodeNode childNode : bodyChildren){
                formatter.addLine(code, childNode.translator().go(childNode));
            }
            return this;
        }
    }
}
