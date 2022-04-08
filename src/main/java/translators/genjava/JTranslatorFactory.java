package translators.genjava;

import codedef.iface.IAttribModifier;
import codedef.iface.ICodeNode;
import codedef.enums.CODE_NODE;
import codedef.enums.ENU_DATA_TYPE;
import err.ERR_TYPE;
import langformat.iface.*;
import langformat.impl.Accumulator;
import langformat.impl.Formatter;
import langformat.impl.NullableUtil;
import runstate.Glob;
import translators.iface.IPathUtil;
import translators.iface.ITranslator;
import translators.iface.ITranslatorFactory;
import translators.iface.IWriteStrategy;

import java.util.ArrayList;
import java.util.List;

import static codedef.enums.CODE_NODE.NEW_OBJECT;
import static codedef.enums.CODE_NODE.VAR_DEF_OBJECT;
import static codedef.enums.ENU_LANGUAGE.JAVA_;
import static codedef.enums.MODIFIER.*;

public class JTranslatorFactory implements ITranslatorFactory {
    @Override
    public ITranslator get(CODE_NODE codeNodeEnum, ICodeNode codeNode) {
        switch(codeNodeEnum){
            case GLOB:
                return new GlobJava().setCodeNode(codeNode);
            case PACKAGE:
                return new PackageJava().setCodeNode(codeNode);
            case FILE:
                return new FileJava().setCodeNode(codeNode);
            case IMPORT:
                return new ImportJava().setCodeNode(codeNode);
            case IMPORT_ITEM:
                return new ImportItemJava().setCodeNode(codeNode);
            case CLASS:
                return new ClassJava().setCodeNode(codeNode);
            case METHOD:
                return new MethodJava().setCodeNode(codeNode);
            case METHOD_ARGS:
                return new MethodArgsJava().setCodeNode(codeNode);

            case CODE_BLOCK:
                return new CodeBlockJava().setCodeNode(codeNode);
            case BOOL_BLOCK:
                return new BoolBlockJava().setCodeNode(codeNode);
            case STATEMENT:
                return new StatementJava().setCodeNode(codeNode);
//            case EXPR_GRP:
//                return new ExprJava().setCodeNode(codeNode);
            case IF_ELSE:
                return new IfElseJava().setCodeNode(codeNode);
            case CONDITIONAL:
            case PAR_BLOCK:
                return new ParBlockJava().setCodeNode(codeNode);
            case CONJUNCTION:
                return new ConjunctionJava().setCodeNode(codeNode);
            case COMPARISON:
                return new ComparisonJava().setCodeNode(codeNode);
            case ELSE:
                return new ElseJava().setCodeNode(codeNode);
            case WHILE:
                return new WhileJava().setCodeNode(codeNode);
            case DO_WHILE:
                return new DoWhileJava().setCodeNode(codeNode);
            case FOR:
                return new ForJava().setCodeNode(codeNode);
            case FOR_INIT:
            case FOR_INC:
                return new ForJava().setCodeNode(codeNode);
            case FOR_IN:
                return new ForInJava().setCodeNode(codeNode);
            case VAR_DEF_SCALAR:
                return new VarDefScalarJava().setCodeNode(codeNode);
            case VAR_DEF_LIST:
                return new VarDefGenericJava("List", 1).setCodeNode(codeNode);
            case VAR_DEF_MAP:
                return new VarDefGenericJava("Map", 2).setCodeNode(codeNode);
            case VAR_DEF_SET:
                return new VarDefGenericJava("Set", 1).setCodeNode(codeNode);
            case VAR_DEF_OBJECT:
                return new VarDefObjectJava().setCodeNode(codeNode);
            case VAR_DEF_ARRAY:
                return new VarDefArrayJava().setCodeNode(codeNode);
            case FUN_CALL:
                return new FunCallJava().setCodeNode(codeNode);
            case NEW_LIST:
                return new NewGenericObjectJava("ArrayList").setCodeNode(codeNode);
            case NEW_MAP:
                return new NewGenericObjectJava("HashMap").setCodeNode(codeNode);
            case NEW_SET:
                return new NewGenericObjectJava("HashSet").setCodeNode(codeNode);
            case NEW_OBJECT:
                return new NewObjectJava().setCodeNode(codeNode);
            case NEW_ARRAY:
                return new NewArrayJava().setCodeNode(codeNode);
            case SWITCH:
                return new SwitchJava().setCodeNode(codeNode);
            case SWITCH_CASE:
                return new SwitchCaseJava().setCodeNode(codeNode);
            case SWITCH_DEFAULT:
                return new SwitchCaseDefaultJava().setCodeNode(codeNode);
            case COMMENT:
                return new CommentJava().setCodeNode(codeNode);
            case COMMENT_LONG:
                return new CommentLongJava().setCodeNode(codeNode);
            case LIT:
            case ASSIGN:
            case RETURN:
            case BREAK:
                return new LitJava().setCodeNode(codeNode);
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
            formatter = Formatter.initInstance();
            nullableUtil = NullableUtil.initInstance();
        }

        @Override
        public String go() {
            this.initChildren(codeNode);
            code = new StringBuilder();
            return head().body().getCode();
        }

        @Override
        public ITranslator head() {
            return this;
        }

        @Override

        public ITranslator body() {
            for(ICodeNode childNode : bodyChildren){
                formatter.add(code, childNode.translator().go());
            }
            return this;
        }

        @Override
        public String getCode() {
            return code.toString();
        }

        @Override
        public ITranslator setCodeNode(ICodeNode codeNode) {
            this.codeNode = codeNode;
            return this;
        }

        @Override
        public ITranslator setPath(IPathUtil pathStrategy){
            return this;
        }

        @Override
        public ITranslator setWriteStrategy(IWriteStrategy writeStrategy){
            return this;
        }

        protected void initChildren(ICodeNode codeNode){
            headerChildren = new ArrayList<>();
            bodyChildren = new ArrayList<>();

            for(ICodeNode childNode : codeNode.getChildren()){
                if(nullableUtil.extractBoolean(childNode.getAttribModifier().get(IS_HEADER_))){
                    headerChildren.add(childNode);
                }
                else{
                    bodyChildren.add(childNode);
                }
            }
        }

        protected String mapDataTypeEnum(String dataTypeEnum){
            switch(dataTypeEnum){
                case "STRING":
                    return "String";
                case "UNK":
                    Glob.ERR.kill(ERR_TYPE.SYNTAX);
                case "NULL":
                case "INT":
                case "FLOAT":
                case "DOUBLE":
                case "BOOLEAN":
                case "VOID":
                    return dataTypeEnum.toLowerCase();
                default:
                    return dataTypeEnum;
            }
        }

        protected String mapDataTypeEnumGeneric(String dataTypeEnum){
            switch(dataTypeEnum){
                case "STRING":
                    return "String";
                case "INT":
                    return "Integer";
                case "UNK":
                    Glob.ERR.kill(ERR_TYPE.SYNTAX);
                case "NULL":
                case "FLOAT":
                case "DOUBLE":
                case "BOOLEAN":
                    return dataTypeEnum.substring(0, 1).toUpperCase() + dataTypeEnum.substring(1).toLowerCase();
                case "VOID":
                    return dataTypeEnum.toLowerCase();
                default:
                    return dataTypeEnum;
            }
        }

        protected String defPreamble(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value;
            IAccumulator acc = new Accumulator(" ");
            if((value = nullableUtil.extractString(attributes.get(ACCESS_))) != null){
                formatter.addWord(code, value.toLowerCase());
            }
            if(nullableUtil.extractBoolean(attributes.get(STATIC_))){
                formatter.addWord(code, "static");
            }
            if(nullableUtil.extractBoolean(attributes.get(ABSTRACT_))){
                formatter.addWord(code, "abstract");
            }
            if(nullableUtil.extractBoolean(attributes.get(FINAL_))){
                formatter.addWord(code, "final");
            }
            return acc.finish();
        }
    }

    public static class GlobJava extends TranslatorBase {
        @Override
        public ITranslator setPath(IPathUtil pathStrategy) {
            if(pathStrategy != null){
                pathStrategy.setCodePathSeparator(".");
                for(ICodeNode childNode : codeNode.getChildren()){
                    childNode.translator().setPath(pathStrategy.reset());
                }
            }
            return this;
        }

        @Override
        public ITranslator setWriteStrategy(IWriteStrategy writeStrategy){
            for(ICodeNode childNode : codeNode.getChildren()){
                childNode.translator().setWriteStrategy(writeStrategy);
            }
            return this;
        }
    }

    public static class PackageJava extends TranslatorBase {
        @Override
        public ITranslator setPath(IPathUtil pathStrategy) {
            String name = nullableUtil.extractString(
                codeNode.getAttribModifier().getAttributes().get(NAME_)
            );
            pathStrategy.appendFilePath(name);
            pathStrategy.appendCodePath(name);
            for(ICodeNode childNode : codeNode.getChildren()){
                childNode.translator().setPath(pathStrategy);
            }
            return this;
        }
        @Override
        public ITranslator setWriteStrategy(IWriteStrategy writeStrategy){
            for(ICodeNode childNode : codeNode.getChildren()){
                childNode.translator().setWriteStrategy(writeStrategy);
            }
            return this;
        }
    }

    public static class FileJava extends TranslatorBase {
        private IWriteStrategy writeStrategy;
        private String codePath, filePath;

        @Override
        public ITranslator setPath(IPathUtil pathStrategy) {
            String name = nullableUtil.extractString(
                codeNode.getAttribModifier().getAttributes().get(NAME_)
            );
            this.codePath = pathStrategy.getCodePath(null);
            this.filePath = pathStrategy.getFilePath(name) + JAVA_.getExt1();

            // Save unique identifier in codeNode object for ImportScraper.
            // Only file's codeNode has this info; all others null
            codeNode.setPathInfo(pathStrategy.getCodePath(name));
            return this;
        }

        @Override
        public ITranslator setWriteStrategy(IWriteStrategy writeStrategy){
            this.writeStrategy = writeStrategy;
            return this;
        }

        @Override
        public ITranslator head() {
            formatter.addStatement(code, "package " + this.codePath);

            for(ICodeNode childNode : headerChildren){// import
                formatter.add(code, childNode.translator().go());
            }
            return this;
        }

        @Override
        public String getCode() {
            String rawText = code.toString();
            if(writeStrategy != null){
                String formattedText = formatter.formatAll(rawText);
                writeStrategy.write(formattedText, filePath);
            }
            return rawText;
        }
    }

    public static class ImportJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            for(ICodeNode childNode : bodyChildren){// importItem
                formatter.addStatement(code, childNode.translator().go());
            }
            formatter.addBlank(code);
            return this;
        }
    }

    public static class ImportItemJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value;
            formatter.addWord(code, "import");
            if(nullableUtil.extractBoolean(attributes.get(STATIC_))){
                formatter.addWord(code, "static");
            }
            if((value = nullableUtil.extractString(attributes.get(LIT_))) != null){
                formatter.addWord(code, value);
            }
            return this;
        }
    }

    public static class ClassJava extends TranslatorBase {
        @Override
        public ITranslator head() {
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value;
            formatter.addWord(code, this.defPreamble());    // public static abstract final
            formatter.addWord(code, "class");
            if((value = nullableUtil.extractString(attributes.get(NAME_))) != null){
                formatter.addWord(code, value);
            }
            if((value = nullableUtil.extractCsv(attributes.get(EXTENDS_))) != null){
                formatter.addWord(code, "extends " + value);
            }
            if((value = nullableUtil.extractCsv(attributes.get(IMPLEMENTS_))) != null){
                formatter.addWord(code, "implements " + value);
            }

            return this;
        }
        @Override
        public ITranslator body() {
            formatter.addLine(code, "{");
            super.body();
            formatter.addLine(code, "}");
            formatter.addBlank(code);
            return this;
        }
    }

    public static class MethodJava extends TranslatorBase {
        @Override
        public ITranslator head() {
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value;
            if(nullableUtil.extractBoolean(attributes.get(OVERRIDE_))){
                formatter.addLine(code, "@Override");
            }
            formatter.addWord(code, this.defPreamble()); // public static abstract final
            if((value = nullableUtil.extractString(attributes.get(TYPE_RETURN_))) != null){
                formatter.addWord(code, value.toLowerCase());
            }
            if((value = nullableUtil.extractString(attributes.get(NAME_))) != null){
                formatter.addWord(code, value);
            }

            for(ICodeNode childNode : headerChildren){// method args
                formatter.addWord(code, childNode.translator().go());
            }
            return this;
        }
        @Override
        public ITranslator body() {
            super.body();
            formatter.addBlank(code);
            return this;
        }
    }

    public static class MethodArgsJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            formatter.addWord(code, "(");
            IAccumulator acc = new Accumulator(", ");
            for(ICodeNode childNode : bodyChildren){
                acc.add(childNode.translator().go().trim());
            }
            formatter.addWord(code, acc.finish());
            formatter.addWord(code, ")");
            return this;
        }
    }

    public static class CodeBlockJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            formatter.addLine(code, "{");
            for(ICodeNode childNode : bodyChildren){
                formatter.addLine(code, childNode.translator().go());
            }
            formatter.addLine(code, "}");
            return this;
        }
    }

    public static class ParBlockJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            formatter.addWord(code, "(");
            for(ICodeNode childNode : bodyChildren){//conditional, connector
                formatter.addWord(code, childNode.translator().go());
            }
            formatter.addWord(code, ")");
            return this;
        }
    }

    public static class BoolBlockJava extends ParBlockJava {
        @Override
        public ITranslator head(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            if(nullableUtil.extractBoolean(attributes.get(IS_NEGATE_))){
                formatter.add(code, "!");
            }
            return this;
        }
    }

    public static class StatementJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            IAccumulator acc = new Accumulator(" ");
            for(ICodeNode childNode : bodyChildren){//conditional, connector
                acc.add(childNode.translator().go());
            }
            formatter.addStatement(code, acc.finish().trim());
            return this;
        }
    }

//    public static class ExprJava extends TranslatorBase {
//        @Override
//        public ITranslator body(){
//            for(ICodeNode childNode : bodyChildren){//conditional, connector
//                formatter.addWord(code, childNode.translator().go());
//            }
//            return this;
//        }
//    }

    public static class IfElseJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            formatter.addWord(code, "if");
            for(ICodeNode childNode : headerChildren){//conditional, connector
                formatter.addWord(code, childNode.translator().go());
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
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value;
            if((value = nullableUtil.extractString(attributes.get(TYPE_CONJ_))) != null){
                formatter.addWord(code, mapEnumeratedValue(value));
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
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value;
            if((value = nullableUtil.extractString(attributes.get(TYPE_COMP_))) != null){
                formatter.addWord(code, mapEnumeratedValue(value));
            }
            return this;
        }
    }

    public static class ElseJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            formatter.addWord(code, "else");
            return this;
        }
    }

    public static class WhileJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            formatter.addWord(code, "while");
            for(ICodeNode childNode : headerChildren){//conditional, connector
                formatter.addWord(code, childNode.translator().go());
            }
            return this;
        }
    }
    public static class DoWhileJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            formatter.addWord(code, "do");
            for(ICodeNode childNode : bodyChildren){
                formatter.addWord(code, childNode.translator().go());
            }
            return this;
        }
        @Override
        public ITranslator body(){
            formatter.addWord(code, "while");
            IAccumulator acc = new Accumulator(" ");
            for(ICodeNode childNode : headerChildren){//conditional, connector
                acc.add(childNode.translator().go());
            }
            formatter.addStatement(code, acc.finish());
            return this;
        }
    }
    public static class ForJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            formatter.add(code, "for (");
            IAccumulator acc = new Accumulator("; ");
            for(ICodeNode childNode : headerChildren){
                acc.add(childNode.translator().go());
            }
            formatter.addWord(code, acc.finish() +")");
            return this;
        }
    }
    public static class ForInitJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            IAccumulator acc = new Accumulator(" ");
            acc.add("int");
            acc.add(nullableUtil.extractString(attributes.get(NAME_)));
            acc.add("=");

            for(ICodeNode childNode : headerChildren){
                acc.add(childNode.translator().go());
            }
            formatter.addWord(code, acc.finish());
            return this;
        }
    }
    public static class ForInJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            return this;
        }
    }

    public static class VarDefScalarJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value;
            formatter.addWord(code, this.defPreamble());    // public static abstract final
            if((value = nullableUtil.extractString(attributes.get(TYPE_DATA_))) != null){
                formatter.addWord(code, this.mapDataTypeEnum(value));
            }
            else{
                Glob.ERR_DEV.kill("variable definition requires datatype");
            }
            if((value = nullableUtil.extractString(attributes.get(NAME_))) != null){
                formatter.addWord(code, value);
            }
            else{
                Glob.ERR_DEV.kill("variable definition requires name");
            }
            return this;
        }
    }

    public static class VarDefGenericJava extends TranslatorBase {
        private final String genericObjectType;
        private final int requiredTypeArgs;

        VarDefGenericJava(String genericObjectType, int requiredTypeArgs){
            this.genericObjectType = genericObjectType;
            this.requiredTypeArgs = requiredTypeArgs;
        }
        private void mapTypes(String[] argList){
            for(int i = 0; i < argList.length; i++){
                argList[i] = this.mapDataTypeEnumGeneric(argList[i]);
            }
        }

        @Override
        public ITranslator body(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value;
            String[] types;
            formatter.addWord(code, this.defPreamble());    // public static abstract final
            formatter.add(code, genericObjectType);
            if(
                (types = attributes.get(TYPE_DATA_)) != null &&
                types.length == requiredTypeArgs
            ){
                this.mapTypes(types);
                formatter.addWord(code, "<" + String.join(", ", types) + ">");
            }
            else{
                Glob.ERR_DEV.kill(String.format("%s definition requires %d type arguments", genericObjectType, requiredTypeArgs));
            }
            if((value = nullableUtil.extractString(attributes.get(NAME_))) != null){
                formatter.addWord(code, value);
            }
            else{
                Glob.ERR_DEV.kill(String.format("%s definition requires name", genericObjectType));
            }
            return this;
        }
    }

    public static class VarDefObjectJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value;
            formatter.addWord(code, this.defPreamble());    // public static abstract final
            if((value = nullableUtil.extractString(attributes.get(TYPE_DATA_))) != null){
                formatter.addWord(code, value);
            }
            else{
                Glob.ERR_DEV.kill(VAR_DEF_OBJECT + " requires object type");
            }
            if((value = nullableUtil.extractString(attributes.get(NAME_))) != null){
                formatter.addWord(code, value);
            }
            else{
                Glob.ERR_DEV.kill("variable definition requires name");
            }
            return this;
        }
    }

    public static class VarDefArrayJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value;
            formatter.addWord(code, this.defPreamble());    // public static abstract final
            if((value = nullableUtil.extractString(attributes.get(TYPE_DATA_))) != null){
                formatter.add(code, this.mapDataTypeEnum(value));
            }
            else{
                Glob.ERR_DEV.kill("array definition requires datatype");
            }
            formatter.addWord(code, "[]");
            if((value = nullableUtil.extractString(attributes.get(NAME_))) != null){
                formatter.addWord(code, value);
            }
            else{
                Glob.ERR_DEV.kill("variable definition requires name");
            }
            return this;
        }
    }

    public static class FunCallJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value = nullableUtil.extractString(attributes.get(NAME_));
            if(value != null){
                formatter.add(code, value);
            }
            else{
                Glob.ERR_DEV.kill("funCall requires name");
            }

            IAccumulator acc = new Accumulator(", ");

            // handle complex args first
            for(ICodeNode childNode : headerChildren){
                acc.add(childNode.translator().go());
            }

            String[] argList = attributes.get(ARGS_);
            if(argList != null){
                acc.add(String.join(", ", argList));
            }

            formatter.add(code, "(" + acc.finish() + ")");
            return this;
        }
    }

    public static class NewGenericObjectJava extends TranslatorBase {
        private final String genericObjectType;

        NewGenericObjectJava(String genericObjectType){
            this.genericObjectType = genericObjectType;
        }

        @Override
        public ITranslator head(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            formatter.addWord(code, "new");
            formatter.add(code, genericObjectType + "<>");

            IAccumulator acc = new Accumulator(", ");

            for(ICodeNode childNode : headerChildren){
                acc.add(childNode.translator().go());
            }

            String[] argList = attributes.get(ARGS_);
            if(argList != null){
                acc.add(String.join(", ", argList));
            }

            formatter.add(code, "(" + acc.finish() + ")");
            return this;
        }
    }

    public static class NewObjectJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            IAttribModifier attributes = codeNode.getAttribModifier();

            formatter.addWord(code, "new");
            String value = nullableUtil.extractString(attributes.get(TYPE_DATA_));
            if(value != null){
                formatter.add(code, value);
            }
            else{
                Glob.ERR_DEV.kill(NEW_OBJECT + " requires object type");
            }
            if(nullableUtil.extractBoolean(attributes.get(IS_GENERIC_))){
                formatter.add(code, "<>");
            }

            IAccumulator acc = new Accumulator(", ");

            for(ICodeNode childNode : headerChildren){
                acc.add(childNode.translator().go());
            }

            String[] argList = attributes.get(ARGS_);
            if(argList != null){
                acc.add(String.join(", ", argList));
            }

            formatter.add(code, "(" + acc.finish() + ")");
            return this;
        }
    }

    public static class NewArrayJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value, dataType;
            formatter.addWord(code, "new");
            if((dataType = nullableUtil.extractString(attributes.get(TYPE_DATA_))) != null){
                formatter.add(code, this.mapDataTypeEnum(dataType));
            }
            else{
                Glob.ERR_DEV.kill("new array requires data type");
            }
            if((value = nullableUtil.extractString(attributes.get(SIZE_))) != null){
                formatter.addWord(code, "[" + value + "]");
            }
            else{
                formatter.addWord(code, "[]");
            }

            IAccumulator acc = new Accumulator(", ");
            for(ICodeNode childNode : headerChildren){
                acc.add(childNode.translator().go());
            }

            String[] argList;
            if((argList = attributes.get(ARGS_)) != null){
                acc.add(String.join(", ", argList));
            }

            formatter.add(code, "{" + acc.finish() + "}");
            return this;
        }
    }

    public static class SwitchJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            String value = nullableUtil.extractString(codeNode.getAttribModifier().get(LIT_));
            formatter.addWord(code, "switch ( " + value + " )");
            return this;
        }

        @Override
        public ITranslator body(){
            formatter.addLine(code, "{");
            for(ICodeNode childNode : bodyChildren){
                formatter.addLine(code, childNode.translator().go());
            }
            formatter.addLine(code, "}");
            return this;
        }
    }

    public static class SwitchCaseJava extends TranslatorBase {
        @Override
        public ITranslator head(){
            String value = nullableUtil.extractString(codeNode.getAttribModifier().get(LIT_));
            formatter.addLine(code, "case " + value + ":");
            return this;
        }
        @Override
        public ITranslator body(){
            if(!bodyChildren.isEmpty()){
                formatter.addInc(code);
                for(ICodeNode childNode : bodyChildren){
                    formatter.add(code, childNode.translator().go());
                }
                formatter.addDec(code);
            }
            return this;
        }
    }

    public static class SwitchCaseDefaultJava extends SwitchCaseJava {
        @Override
        public ITranslator head(){
            formatter.addLine(code, "default:");
            return this;
        }
    }

    public static class CommentJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value = nullableUtil.extractString(attributes.get(LIT_));
            if(value != null){
                formatter.addComment(code, value, "//");
            }
            return this;
        }
    }

    public static class CommentLongJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            String value = nullableUtil.extractString(attributes.get(LIT_));
            if(value != null){
                formatter.addComment(code, value, "/*", "*", "*/");
            }
            return this;
        }
    }

    public static class LitJava extends TranslatorBase {
        @Override
        public ITranslator body(){
            IAttribModifier attributes = codeNode.getAttribModifier();
            String[] values = attributes.get(LIT_);
            if(values != null){
                formatter.addWord(code, String.join(", ", values));
            }
            return this;
        }
    }
}
