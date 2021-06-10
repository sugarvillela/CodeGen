package codedef.init;

import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;
import codedef.impl.AttribModifier;
import codedef.impl.AttribModifier_Nameless;
import codedef.impl.AttribStruct;
import codedef.codenode.CodeNode;
import codedef.modifier.CODE_NODE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static codedef.modifier.CODE_NODE.*;
import static codedef.modifier.ENU_BOOLEAN.*;
import static codedef.modifier.ENU_COLLECTION.*;
import static codedef.modifier.ENU_DATA_TYPE.*;
import static codedef.modifier.ENU_OUT_LANG.JAVA;
import static codedef.modifier.ENU_QUANTIFIER.*;
import static codedef.modifier.ENU_VISIBILITY.*;
import static codedef.modifier.MODIFIER.*;

public class CodeDef {
    public static final class LangAgnosticAssumptions{
        public static final String RETURN = "return";
        public static final String ASSIGN = "=";
        public static final String BREAK = "break;";
    };
    private final HashMap<CODE_NODE, ICodeNode> prototypes;
    
    public CodeDef() {
        prototypes = new HashMap<>();
        initLargeScopes();
        initSmallScopes();
    }
    public final HashMap<CODE_NODE, ICodeNode> getPrototypes(){
        return this.prototypes;
    }

    private void initLargeScopes(){
        this.initGlob(GLOB);
        this.initPackage(PACKAGE);
        this.initFile(FILE);
        this.initImport(IMPORT);
        this.initImportItem(IMPORT_ITEM);
        this.initClass(CLASS);
        this.initClassField(CLASS_FIELD);
        this.initMethod(METHOD);
        this.initConstructor(CONSTRUCTOR);
        this.initMethodArgs(METHOD_ARGS);
        this.initMethodArg(METHOD_ARG);
        this.initIfElse(IF_ELSE);
        this.initConditional(CONDITIONAL);
        this.initElse(ELSE);
    }
    private void initSmallScopes(){
        this.initCodeBlock(CODE_BLOCK);
        this.initParBlock(PAR_BLOCK);
        this.initBoolBlock(BOOL_BLOCK);
        this.initStatement(STATEMENT);
        this.initExpr(EXPR);

        this.initLeafyText(LIT);
        this.initConjunction(CONJUNCTION);
        this.initComparison(COMPARISON);
        this.initVarDef(VAR_DEF);
        this.initLeafyText(RETURN, LangAgnosticAssumptions.RETURN);
        this.initLeafyText(ASSIGN, LangAgnosticAssumptions.ASSIGN);
        this.initLeafyText(BREAK, LangAgnosticAssumptions.BREAK);
        this.initComment(COMMENT);
        this.initComment(COMMENT_LONG);
        this.initSwitch(SWITCH);
        this.initSwitchCase(SWITCH_CASE);
        this.initSwitchDefault(SWITCH_DEFAULT);
    }

    private void initGlob(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);
        attribModifier.initAllowed(OUT_LANG, FORMAT_INDENT);

        attribModifier.put(OUT_LANG, JAVA.toString());
        attribModifier.put(FORMAT_INDENT, "4");

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(PACKAGE);

        ICodeNode codeStruct = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeStruct);
    }
    private void initPackage(CODE_NODE codeNodeEnum){
        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(PACKAGE, FILE);

        ICodeNode codeStruct = new CodeNode(codeNodeEnum, null, null, null, structBody);
        prototypes.put(codeNodeEnum, codeStruct);
    }
    private void initFile(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(PATH);

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initAllowed(IMPORT, COMMENT);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(CLASS);

        ICodeNode codeStruct = new CodeNode(codeNodeEnum, null, attribModifier, structHeader, structBody);
        prototypes.put(codeNodeEnum, codeStruct);
    }
    private void initImport(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(IMPORT_ITEM);
        structBody.initAllowed(COMMENT);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initImportItem(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);
        attribModifier.initRequired(LIT_VAL);
        attribModifier.initAllowed(STATIC);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initClass(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired();
        attribModifier.initAllowed(VISIBILITY, ABSTRACT, STATIC, FINAL, IMPLEMENTS, EXTENDS);

        attribModifier.put(VISIBILITY, PUBLIC.toString());
        attribModifier.put(ABSTRACT, FALSE.toString());
        attribModifier.put(STATIC, FALSE.toString());
        attribModifier.put(FINAL, FALSE.toString());
        attribModifier.put(IMPLEMENTS);
        attribModifier.put(EXTENDS);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(CLASS_FIELD, METHOD, CONSTRUCTOR, COMMENT);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initClassField(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(DATA_TYPE, COLLECTION);
        attribModifier.initAllowed(VISIBILITY, STATIC, FINAL, VAR_VALUE);

        attribModifier.put(COLLECTION, SCALAR.toString());     // Denotes scalar vs array
        attribModifier.put(VISIBILITY, PUBLIC.toString());
        attribModifier.put(STATIC, FALSE.toString());
        attribModifier.put(FINAL, FALSE.toString());

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initMethod(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(DATA_TYPE);
        attribModifier.initAllowed(VISIBILITY, ABSTRACT, STATIC, FINAL, OVERRIDE);

        attribModifier.put(VISIBILITY, PUBLIC.toString());
        attribModifier.put(DATA_TYPE, VOID.toString());
        attribModifier.put(ABSTRACT, FALSE.toString());
        attribModifier.put(STATIC, FALSE.toString());
        attribModifier.put(FINAL, FALSE.toString());
        attribModifier.put(OVERRIDE, FALSE.toString());

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initRequired(METHOD_ARGS);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(CODE_BLOCK);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, structHeader, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initConstructor(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initAllowed(VISIBILITY);

        attribModifier.put(NAME, codeNodeEnum.toString());
        attribModifier.put(VISIBILITY, PUBLIC.toString());

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initRequired(METHOD_ARGS);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(CODE_BLOCK);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, structHeader, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initMethodArgs(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);
        attribModifier.initRequired(QUANTIFIER);
        attribModifier.initAllowed();

        attribModifier.put(QUANTIFIER, ZERO.toString());

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired();
        structBody.initAllowed(METHOD_ARG);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initMethodArg(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(DATA_TYPE, COLLECTION);
        attribModifier.initAllowed(VAR_VALUE);

        attribModifier.put(COLLECTION, SCALAR.toString());     // Denotes scalar vs array

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired();
        structBody.initAllowed();

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initIfElse(CODE_NODE codeNodeEnum){// conditional, code block, optional else
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);
        attribModifier.initRequired();
        attribModifier.initAllowed();

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initRequired(CONDITIONAL);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(CODE_BLOCK);
        structBody.initAllowed(ELSE);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, structHeader, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initConditional(CODE_NODE codeNodeEnum){// parentheses-surrounded expression
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(EXPR);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initElse(CODE_NODE codeNodeEnum){// simply wraps a code block
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(CODE_BLOCK);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }

    private void initCodeBlock(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(EXPR, STATEMENT, COMMENT);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initParBlock(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(EXPR);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initBoolBlock(CODE_NODE codeNodeEnum){// parentheses-surrounded expression with negate
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);

        attribModifier.initAllowed(NEGATE);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(EXPR);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initStatement(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(EXPR);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }

    private void initExpr(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired();

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initConjunction(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);
        attribModifier.initRequired(CONJUNCTION_TYPE);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initComparison(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);
        attribModifier.initRequired(COMPARISON_TYPE);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initVarDef(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(DATA_TYPE, COLLECTION);
        attribModifier.initAllowed(STATIC, FINAL, VAR_VALUE);
        attribModifier.put(COLLECTION, SCALAR.toString());
        attribModifier.put(STATIC, FALSE.toString());
        attribModifier.put(FINAL, FALSE.toString());

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initComment(CODE_NODE codeNodeEnum){// simple text, no attrib or children
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);
        attribModifier.initRequired(LIT_VAL);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, COMMENT, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }

    private void initSwitch(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);
        attribModifier.initRequired(LIT_VAL);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(SWITCH_CASE, SWITCH_DEFAULT, BREAK);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initSwitchCase(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);
        attribModifier.initRequired(LIT_VAL);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(CODE_BLOCK, EXPR);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initSwitchDefault(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(CODE_BLOCK, EXPR);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }

    private void initLeafyText(CODE_NODE codeNodeEnum){// simple text, no attrib or children
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);
        attribModifier.initRequired(LIT_VAL);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initLeafyText(CODE_NODE codeNodeEnum, String langAgnosticText){// simple text, no attrib or children
        IAttribModifier attribModifier = new AttribModifier_Nameless(codeNodeEnum);
        attribModifier.initRequired(LIT_VAL);
        attribModifier.put(LIT_VAL, langAgnosticText);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void display(){
        Iterator<Map.Entry<CODE_NODE, ICodeNode>> itr = prototypes.entrySet().iterator();

        while(itr.hasNext()) {
            Map.Entry<CODE_NODE, ICodeNode> entry = itr.next();
            CODE_NODE key = entry.getKey();
            System.out.println(key);
            ICodeNode value = entry.getValue();
            System.out.println(value);
            if(value != null && value.getAttribModifier() != null){
                System.out.println("\n===" + key + "===\n" + value.getAttribModifier().toString());
            }
        }
    }
}
