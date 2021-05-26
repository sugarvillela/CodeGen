package translators.genjava;

import codedef.iface.ICodeNode;
import codedef.modifier.CODE_NODE;
import codedef.modifier.ENU_DATA_TYPE;
import codedef.modifier.MODIFIER;
import err.ERR_TYPE;
import generictree.iface.IGTreeNode;
import langformat.iface.IFormatUtil;
import langformat.iface.INullableUtil;
import langformat.impl.NullableUtil;
import runstate.Glob;
import translators.iface.ITranslator;
import translators.iface.ITranslatorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static codedef.modifier.MODIFIER.*;

public class JTranslators implements ITranslatorFactory {
    @Override
    public ITranslator get(CODE_NODE codeNodeEnum) {
        switch(codeNodeEnum){
            case GLOB:
                return new GlobJava();
            case PACKAGE:
                return new PackageJava();
            case FILE:
                return new FileJava();
            case CLASS:
                return new ClassJava();
            case METHOD:
                return new MethodJava();
            case METHOD_ARGS:
                return new MethodArgsJava();
            case METHOD_ARG:
                return new MethodArgJava();
            default:
                return null;
        }
    }

    public static abstract class TranslatorBase implements ITranslator{
        protected final INullableUtil nullableUtil;
        public TranslatorBase() {
            nullableUtil = NullableUtil.initInstance();
        }

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
    }
    public static abstract class IndentedBase extends TranslatorBase{
        @Override
        public void inc(IFormatUtil formatUtil) {
            formatUtil.inc();
        }
        @Override
        public void dec(IFormatUtil formatUtil) {
            formatUtil.dec();
        }
    }

    public static class GlobJava  extends TranslatorBase {
        @Override
        public void buildHeader(IFormatUtil formatUtil, ICodeNode codeNode) {
            // TODO set formatting options
        }
    }

    public static class PackageJava extends TranslatorBase {
        @Override
        public void buildHeader(IFormatUtil formatUtil, ICodeNode codeNode) {
            // TODO make directory or check directory exists
        }
    }

    public static class FileJava extends TranslatorBase {
        @Override
        public void buildHeader(IFormatUtil formatUtil, ICodeNode codeNode) {
            formatUtil.clear();

            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            String[] values;
            String value;
            if((value = nullableUtil.extractString(attributes.get(PATH))) != null){
                formatUtil.addLine(nullableUtil.statement("package " + value));
            }
            if((values = nullableUtil.extractStrings(attributes.get(IMPORTS))) != null){
                for(String item : values){
                    formatUtil.addLine(nullableUtil.statement("import " + item));
                }
            }
        }

        @Override
        public void dec(IFormatUtil formatUtil) {
            formatUtil.clear();
        }
    }

    public static class ClassJava extends IndentedBase {
        @Override
        public void buildHeader(IFormatUtil formatUtil, ICodeNode codeNode){
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            List<String> header = new ArrayList<>();
            String value;
            if((value = nullableUtil.extractString(attributes.get(VISIBILITY))) != null){
                header.add(value.toLowerCase());
            }
            if(nullableUtil.extractBoolean(attributes.get(STATIC))){
                IGTreeNode<ICodeNode> treeNode = codeNode.getParentTreeNode();
                if(treeNode != null && treeNode.getPayload().codeNodeEnum() != CODE_NODE.CLASS){
                    Glob.ERR.check(ERR_TYPE.LANGUAGE_ERR, "static");
                }
                header.add("static");
            }
            if(nullableUtil.extractBoolean(attributes.get(ABSTRACT))){
                header.add("abstract");
            }
            if(nullableUtil.extractBoolean(attributes.get(FINAL))){
                header.add("final");
            }
            header.add("class");
            if((value = nullableUtil.extractString(attributes.get(NAME))) != null){
                header.add(value);
            }
            if((value = nullableUtil.extractCsv(attributes.get(EXTENDS))) != null){
                header.add("extends " + value);
            }
            if((value = nullableUtil.extractCsv(attributes.get(IMPLEMENTS))) != null){
                header.add("implements " + value);
            }
            formatUtil.addLine(String.join(" ", header) + " {");
        }

        @Override
        public void buildFooter(IFormatUtil formatUtil, ICodeNode codeNode){
            formatUtil.addLine("}");
        }
    }

    public static class MethodJava extends IndentedBase {
        @Override
        public void buildHeader(IFormatUtil formatUtil, ICodeNode codeNode){
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            List<String> header = new ArrayList<>();
            String value;
            if((value = nullableUtil.extractString(attributes.get(VISIBILITY))) != null){
                header.add(value.toLowerCase());
            }
            if(nullableUtil.extractBoolean(attributes.get(STATIC))){
                IGTreeNode<ICodeNode> treeNode = codeNode.getParentTreeNode();
                if(treeNode != null && treeNode.getPayload().codeNodeEnum() != CODE_NODE.CLASS){
                    Glob.ERR.check(ERR_TYPE.LANGUAGE_ERR, "static");
                }
                header.add("static");
            }
            if(nullableUtil.extractBoolean(attributes.get(ABSTRACT))){
                header.add("abstract");
            }
            if(nullableUtil.extractBoolean(attributes.get(FINAL))){
                header.add("final");
            }
            if((value = nullableUtil.extractString(attributes.get(DATA_TYPE))) != null){
                header.add(value.toLowerCase());
            }
            if((value = nullableUtil.extractString(attributes.get(NAME))) != null){
                header.add(value);
            }
            formatUtil.lineUtil().prepare().setHeader(String.join(" ", header) + "(");
            for(ICodeNode childNode : codeNode.getParentTreeNode().getPayloadChildren()){
                if(childNode.codeNodeEnum() == CODE_NODE.METHOD_ARGS){
                    childNode.translator().translate(formatUtil, childNode);
                }
            }
            formatUtil.lineUtil().setFooter( ") {").toCsv();
        }
        @Override
        public void recurse(IFormatUtil formatUtil, ICodeNode codeNode) {
//            for(ICodeNode childNode : codeNode.getParentTreeNode().getPayloadChildren()){
//                if(childNode.codeNodeEnum() != CODE_NODE.METHOD_ARGS){
//                    childNode.translator().translate(formatUtil, childNode);
//                }
//            }
        }

        @Override
        public void buildFooter(IFormatUtil formatUtil, ICodeNode codeNode){
            formatUtil.addLine("}");
        }
    }

    public static class MethodArgsJava extends TranslatorBase {}

    public static class MethodArgJava extends TranslatorBase {
        @Override
        public void buildHeader(IFormatUtil formatUtil, ICodeNode codeNode) {
            HashMap<MODIFIER, String[]> attributes = codeNode.getAttribModifier().getAttributes();
            List<String> header = new ArrayList<>();
            String value;
            if((value = nullableUtil.extractString(attributes.get(DATA_TYPE))) != null){
                header.add(this.mapDataTypeEnum(value));
            }
            if((value = nullableUtil.extractString(attributes.get(NAME))) != null){
                header.add(value.toLowerCase());
            }
            formatUtil.lineUtil().appendLine(String.join(" ", header));
        }

        @Override
        public void recurse(IFormatUtil formatUtil, ICodeNode codeNode) {}
    }


}
