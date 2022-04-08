package codedef.init;

import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;
import codedef.impl.AttribModifier;
import codedef.impl.AttribStruct;
import codedef.codenode.CodeNode;
import codedef.enums.CODE_NODE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static codedef.enums.CODE_NODE.*;
import static codedef.enums.ENU_BOOLEAN.*;
import static codedef.enums.ENU_DATA_TYPE.*;
import static codedef.enums.ENU_LANGUAGE.JAVA_;
import static codedef.enums.ENU_VISIBILITY.*;
import static codedef.enums.MODIFIER.*;

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
        this.initMethod(METHOD);
        this.initMethodArgs(METHOD_ARGS);
    }
    private void initSmallScopes(){
        this.initCodeBlock(CODE_BLOCK);
        this.initParBlock(PAR_BLOCK);
        this.initBoolBlock(BOOL_BLOCK);
        this.initStatement(STATEMENT);
        //this.initExpr(EXPR_GRP);

        this.initIfElse(IF_ELSE);
        this.initConditional(CONDITIONAL);
        this.initConjunction(CONJUNCTION);
        this.initComparison(COMPARISON);
        this.initElse(ELSE);

        this.initWhile(WHILE);
        this.initWhile(DO_WHILE);

        this.initFor(FOR);
        this.initForInitOrInc(FOR_INIT);
        this.initForInitOrInc(FOR_INC);

        this.initForIn(FOR_IN);

        this.initVarDef(VAR_DEF_SCALAR);
        this.initVarDef(VAR_DEF_LIST);
        this.initVarDef(VAR_DEF_MAP);
        this.initVarDef(VAR_DEF_SET);
        this.initVarDefObject(VAR_DEF_OBJECT);
        this.initVarDefArray(VAR_DEF_ARRAY);

        this.initFunCall(FUN_CALL);
        this.initNewGenericObject(NEW_LIST);
        this.initNewGenericObject(NEW_MAP);
        this.initNewGenericObject(NEW_SET);
        this.initNewObject(NEW_OBJECT);
        this.initNewArray(NEW_ARRAY);

        this.initSwitch(SWITCH);
        this.initSwitchCase(SWITCH_CASE);
        this.initSwitchDefault(SWITCH_DEFAULT);

        this.initComment(COMMENT);
        this.initComment(COMMENT_LONG);

        this.initLeafyText(LIT);
        this.initLeafyText(ASSIGN, LangAgnosticAssumptions.ASSIGN);
        this.initLeafyText(RETURN, LangAgnosticAssumptions.RETURN);
        this.initLeafyText(BREAK, LangAgnosticAssumptions.BREAK);
    }

    private void initGlob(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initAllowed(LANGUAGE_, INDENT_);

        attribModifier.put(NAME_, codeNodeEnum.toString());
        attribModifier.put(LANGUAGE_, JAVA_.toString());
        attribModifier.put(INDENT_, "4");

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

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initAllowed(IMPORT, COMMENT);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(CLASS);

        ICodeNode codeStruct = new CodeNode(codeNodeEnum, null, null, structHeader, structBody);
        prototypes.put(codeNodeEnum, codeStruct);
    }
    private void initImport(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(IMPORT_ITEM);
        structBody.initAllowed(COMMENT);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initImportItem(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(LIT_);
        attribModifier.initAllowed(STATIC_);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initClass(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired();
        attribModifier.initAllowed(ACCESS_, ABSTRACT_, STATIC_, FINAL_, IMPLEMENTS_, EXTENDS_);

        attribModifier.put(ACCESS_, PUBLIC.toString());
        attribModifier.put(IMPLEMENTS_);
        attribModifier.put(EXTENDS_);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(METHOD, COMMENT, STATEMENT);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initMethod(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired();
        attribModifier.initAllowed(ACCESS_, ABSTRACT_, STATIC_, FINAL_, OVERRIDE_, TYPE_RETURN_);

        attribModifier.put(ACCESS_, PUBLIC.toString());
        attribModifier.put(TYPE_RETURN_, VOID.toString());
        attribModifier.put(ABSTRACT_, FALSE.toString());
        attribModifier.put(STATIC_, FALSE.toString());
        attribModifier.put(FINAL_, FALSE.toString());
        attribModifier.put(OVERRIDE_, FALSE.toString());

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initRequired(METHOD_ARGS);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(CODE_BLOCK);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, structHeader, structBody);
        codeNode.getAttribModifier().getRequired().add(TYPE_RETURN_);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initMethodArgs(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired();
        structBody.initAllowed(VAR_DEF_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }

    private void initCodeBlock(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(EXPR_GRP, STATEMENT, COMMENT);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initParBlock(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(EXPR_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initBoolBlock(CODE_NODE codeNodeEnum){// parentheses-surrounded expression with negate
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        attribModifier.initAllowed(IS_NEGATE_);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(EXPR_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initStatement(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(EXPR_GRP, VAR_DEF_GRP, NEW_DEF_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
//    private void initExpr(CODE_NODE codeNodeEnum){
//        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
//        attribModifier.put(NAME_, codeNodeEnum.toString());
//
//        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
//        structBody.initRequired();
//
//        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, null, structBody);
//        prototypes.put(codeNodeEnum, codeNode);
//    }

    private void initIfElse(CODE_NODE codeNodeEnum){// conditional, code block, optional else
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initRequired(CONDITIONAL);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(CODE_BLOCK);
        structBody.initAllowed(ELSE);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, structHeader, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initConditional(CODE_NODE codeNodeEnum){// parentheses-surrounded expression
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(EXPR_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initConjunction(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(TYPE_CONJ_);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initComparison(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(TYPE_COMP_);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initElse(CODE_NODE codeNodeEnum){// simply wraps a code block
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(CODE_BLOCK);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }

    private void initWhile(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initRequired(CONDITIONAL);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(CODE_BLOCK);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, structHeader, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initFor(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initRequired(FOR_INIT, CONDITIONAL, FOR_INC);

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initRequired(CODE_BLOCK);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, structHeader, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initForInitOrInc(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, "i");// NAME_ is variable name; overwrite as needed

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initRequired(EXPR_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, structHeader, null);
        prototypes.put(codeNodeEnum, codeNode);
    }

    private void initForIn(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initAllowed(ARGS_);

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initAllowed(EXPR_GRP, NEW_DEF_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, structHeader, null);
        prototypes.put(codeNodeEnum, codeNode);
    }

    private void initVarDef(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initAllowed(TYPE_DATA_, ACCESS_, STATIC_, FINAL_);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, VAR_DEF_GRP, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initVarDefObject(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(TYPE_DATA_);
        attribModifier.initAllowed(ACCESS_, STATIC_, FINAL_);

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initAllowed(EXPR_GRP, NEW_DEF_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, VAR_DEF_GRP, attribModifier, structHeader, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initVarDefArray(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initAllowed(TYPE_DATA_, ACCESS_, STATIC_, FINAL_);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, VAR_DEF_GRP, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initNewGenericObject(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initAllowed(ARGS_, LIT_);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initAllowed(EXPR_GRP, NEW_DEF_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, NEW_DEF_GRP, attribModifier, structHeader, null);
        prototypes.put(codeNodeEnum, codeNode);
    }

    private void initFunCall(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initAllowed(ARGS_, LIT_);

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initAllowed(EXPR_GRP, NEW_DEF_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, structHeader, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initNewObject(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(TYPE_DATA_);
        attribModifier.initAllowed(ARGS_, IS_GENERIC_, LIT_);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initAllowed(EXPR_GRP, NEW_DEF_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, NEW_DEF_GRP, attribModifier, structHeader, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initNewArray(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initAllowed(TYPE_DATA_, SIZE_, ARGS_, LIT_);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structHeader = new AttribStruct(codeNodeEnum);
        structHeader.initAllowed(EXPR_GRP, NEW_DEF_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, NEW_DEF_GRP, attribModifier, structHeader, null);
        prototypes.put(codeNodeEnum, codeNode);
    }

    private void initSwitch(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(LIT_);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(SWITCH_CASE, SWITCH_DEFAULT, BREAK);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initSwitchCase(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(LIT_);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(CODE_BLOCK, EXPR_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initSwitchDefault(CODE_NODE codeNodeEnum){
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        IAttribStruct structBody = new AttribStruct(codeNodeEnum);
        structBody.initAllowed(CODE_BLOCK, EXPR_GRP);

        ICodeNode codeNode = new CodeNode(codeNodeEnum, null, attribModifier, null, structBody);
        prototypes.put(codeNodeEnum, codeNode);
    }

    private void initComment(CODE_NODE codeNodeEnum){// simple text, no attrib or children
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(LIT_);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        ICodeNode codeNode = new CodeNode(codeNodeEnum, COMMENT, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }

    private void initLeafyText(CODE_NODE codeNodeEnum){// simple text, no attrib or children
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(LIT_);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, null, null);
        prototypes.put(codeNodeEnum, codeNode);
    }
    private void initLeafyText(CODE_NODE codeNodeEnum, String langAgnosticText){// simple text, no attrib or children
        IAttribModifier attribModifier = new AttribModifier(codeNodeEnum);
        attribModifier.initRequired(LIT_);
        attribModifier.put(LIT_, langAgnosticText);
        attribModifier.put(NAME_, codeNodeEnum.toString());

        ICodeNode codeNode = new CodeNode(codeNodeEnum, EXPR_GRP, attribModifier, null, null);
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
