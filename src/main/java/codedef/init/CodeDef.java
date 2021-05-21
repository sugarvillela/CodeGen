package codedef.init;

import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.iface.ICodeNode;
import codedef.impl.AttribModifier;
import codedef.impl.AttribStruct;
import codedef.codenode.CodeNode;
import codedef.modifier.CODE_NODE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static codedef.modifier.CODE_NODE.*;
import static codedef.modifier.ENU_OUT_LANG.JAVA;
import static codedef.modifier.ENU_QUANTIFIER.*;
import static codedef.modifier.ENU_VISIBILITY.*;
import static codedef.modifier.ENU_COLLECTION.*;
import static codedef.modifier.MODIFIER.*;

public class CodeDef {
    private static final int EXPECTED_SIZE = 13;
    private final HashMap<CODE_NODE, ICodeNode> prototypes;

    public CodeDef() {
        prototypes = new HashMap<>((int)Math.ceil(EXPECTED_SIZE * 1.34));
        this.initGlob();
        this.initPackage();
        this.initFile();
        this.initClass();
        this.initImport();
        this.initImportItem();
        this.initMethod();
        this.initClassField();
        this.initVar();
        this.initMethodArg();
        this.initMethodArgs();
        this.initMethodBody();
        //this.display();
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

    public final HashMap<CODE_NODE, ICodeNode> getPrototypes(){
        return this.prototypes;
    }

    private void initGlob(){
        IAttribModifier attribModifier = new AttribModifier(GLOB);
        attribModifier.initRequired(CODE_NODE_TYPE, NAME);
        attribModifier.initAllowed(OUT_LANG, FORMAT_INDENT);

        attribModifier.put(CODE_NODE_TYPE, GLOB.toString());
        attribModifier.put(NAME, GLOB.toString());
        attribModifier.put(OUT_LANG, JAVA.toString());
        attribModifier.put(FORMAT_INDENT, "4");

        IAttribStruct attribStruct = new AttribStruct(GLOB);
        attribStruct.initAllowed(PACKAGE);

        ICodeNode codeStruct = new CodeNode(GLOB, attribModifier, attribStruct);
        prototypes.put(GLOB, codeStruct);
    }
    private void initPackage(){
        IAttribModifier attribModifier = new AttribModifier(PACKAGE);
        attribModifier.initRequired(CODE_NODE_TYPE, NAME);
        attribModifier.initAllowed();
        attribModifier.put(CODE_NODE_TYPE, PACKAGE.toString());

        IAttribStruct attribStruct = new AttribStruct(PACKAGE);
        attribStruct.initAllowed(PACKAGE, FILE);

        ICodeNode codeStruct = new CodeNode(PACKAGE, attribModifier, attribStruct);
        prototypes.put(PACKAGE, codeStruct);
    }
    private void initFile(){
        IAttribModifier attribModifier = new AttribModifier(FILE);
        attribModifier.initRequired(CODE_NODE_TYPE, NAME, PATH);
        attribModifier.initAllowed();

        attribModifier.put(CODE_NODE_TYPE, FILE.toString());

        IAttribStruct attribStruct = new AttribStruct(FILE);
        attribStruct.initRequired(CLASS);
        attribStruct.initAllowed();

        ICodeNode codeStruct = new CodeNode(FILE, attribModifier, attribStruct);
        prototypes.put(FILE, codeStruct);
    }
    private void initClass(){
        IAttribModifier attribModifier = new AttribModifier(CLASS);
        attribModifier.initRequired(CODE_NODE_TYPE, NAME);
        attribModifier.initAllowed(VISIBILITY, ABSTRACT, STATIC, FINAL, IMPLEMENTS, EXTENDS);

        attribModifier.put(CODE_NODE_TYPE, CLASS.toString());
        attribModifier.put(NAME, GLOB.toString());
        attribModifier.put(VISIBILITY, PUBLIC.toString());
        attribModifier.put(ABSTRACT, "FALSE");
        attribModifier.put(STATIC, "FALSE");
        attribModifier.put(FINAL, "FALSE");
        attribModifier.put(IMPLEMENTS);
        attribModifier.put(EXTENDS);

        IAttribStruct attribStruct = new AttribStruct(CLASS);
        attribStruct.initRequired(IMPORT);
        attribStruct.initAllowed(CLASS_FIELD, METHOD);

        ICodeNode codeNode = new CodeNode(CLASS, attribModifier, attribStruct);
        prototypes.put(CLASS, codeNode);
    }
    private void initImport(){
        IAttribModifier attribModifier = new AttribModifier(IMPORT);
        attribModifier.initRequired(CODE_NODE_TYPE, NAME);
        attribModifier.initAllowed();

        attribModifier.put(CODE_NODE_TYPE, IMPORT.toString());
        attribModifier.put(NAME, IMPORT.toString());

        IAttribStruct attribStruct = new AttribStruct(IMPORT);
        attribStruct.initRequired();
        attribStruct.initAllowed(IMPORT_ITEM);

        ICodeNode codeNode = new CodeNode(IMPORT, attribModifier, attribStruct);
        prototypes.put(IMPORT, codeNode);
    }
    private void initImportItem(){
        IAttribModifier attribModifier = new AttribModifier(IMPORT_ITEM);
        attribModifier.initRequired(CODE_NODE_TYPE, NAME, PATH);
        attribModifier.initAllowed(STATIC);
        attribModifier.put(CODE_NODE_TYPE, IMPORT_ITEM.toString());

        IAttribStruct attribStruct = new AttribStruct(IMPORT_ITEM);
        attribStruct.initRequired();
        attribStruct.initAllowed();

        ICodeNode codeNode = new CodeNode(IMPORT_ITEM, attribModifier, attribStruct);
        prototypes.put(IMPORT_ITEM, codeNode);
    }
    private void initMethod(){
        IAttribModifier attribModifier = new AttribModifier(METHOD);
        attribModifier.initRequired(CODE_NODE_TYPE, NAME, DATA_TYPE);
        attribModifier.initAllowed(VISIBILITY, ABSTRACT, STATIC, FINAL, OVERRIDE);

        attribModifier.put(CODE_NODE_TYPE, METHOD.toString());
        attribModifier.put(VISIBILITY, PUBLIC.toString());
        attribModifier.put(DATA_TYPE, "VOID");
        attribModifier.put(ABSTRACT, "FALSE");
        attribModifier.put(STATIC, "FALSE");
        attribModifier.put(FINAL, "FALSE");
        attribModifier.put(OVERRIDE);

        IAttribStruct attribStruct = new AttribStruct(METHOD);
        attribStruct.initRequired();
        attribStruct.initAllowed(METHOD_ARGS, METHOD_BODY);

        ICodeNode codeNode = new CodeNode(METHOD, attribModifier, attribStruct);
        prototypes.put(METHOD, codeNode);
    }
    private void initClassField(){
        IAttribModifier attribModifier = new AttribModifier(CLASS_FIELD);
        attribModifier.initRequired(CODE_NODE_TYPE, NAME, DATA_TYPE, COLLECTION);
        attribModifier.initAllowed(VISIBILITY, STATIC, FINAL, VAR_VALUE);

        attribModifier.put(CODE_NODE_TYPE, CLASS_FIELD.toString());
        attribModifier.put(COLLECTION, SCALAR.toString());     // Denotes scalar vs array
        attribModifier.put(VISIBILITY, PUBLIC.toString());
        attribModifier.put(STATIC, "FALSE");
        attribModifier.put(FINAL, "FALSE");

        IAttribStruct attribStruct = new AttribStruct(CLASS_FIELD);
        attribStruct.initRequired();
        attribStruct.initAllowed();

        ICodeNode codeNode = new CodeNode(CLASS_FIELD, attribModifier, attribStruct);
        prototypes.put(CLASS_FIELD, codeNode);
    }
    private void initVar(){
        IAttribModifier attribModifier = new AttribModifier(VAR);
        attribModifier.initRequired(CODE_NODE_TYPE, NAME, DATA_TYPE, COLLECTION);
        attribModifier.initAllowed(STATIC, FINAL, VAR_VALUE);

        attribModifier.put(CODE_NODE_TYPE, VAR.toString());
        attribModifier.put(COLLECTION, SCALAR.toString());
        attribModifier.put(STATIC, "FALSE");
        attribModifier.put(FINAL, "FALSE");

        IAttribStruct attribStruct = new AttribStruct(VAR);
        attribStruct.initRequired();
        attribStruct.initAllowed();

        ICodeNode codeNode = new CodeNode(VAR, attribModifier, attribStruct);
        prototypes.put(VAR, codeNode);
    }
    private void initMethodArgs(){
        IAttribModifier attribModifier = new AttribModifier(METHOD_ARGS);
        attribModifier.initRequired(CODE_NODE_TYPE, NAME, QUANTIFIER);
        attribModifier.initAllowed();

        attribModifier.put(CODE_NODE_TYPE, METHOD_ARGS.toString());
        attribModifier.put(NAME, METHOD_ARGS.toString());
        attribModifier.put(QUANTIFIER, ZERO.toString());

        IAttribStruct attribStruct = new AttribStruct(METHOD_ARGS);
        attribStruct.initRequired();
        attribStruct.initAllowed(METHOD_ARG);

        ICodeNode codeNode = new CodeNode(METHOD_ARGS, attribModifier, attribStruct);
        prototypes.put(METHOD_ARGS, codeNode);
    }
    private void initMethodArg(){
        IAttribModifier attribModifier = new AttribModifier(METHOD_ARG);
        attribModifier.initRequired(CODE_NODE_TYPE, NAME, DATA_TYPE, COLLECTION);
        attribModifier.initAllowed(VAR_VALUE);

        attribModifier.put(CODE_NODE_TYPE, METHOD_ARG.toString());
        attribModifier.put(COLLECTION, SCALAR.toString());     // Denotes scalar vs array

        IAttribStruct attribStruct = new AttribStruct(METHOD_ARG);
        attribStruct.initRequired();
        attribStruct.initAllowed();

        ICodeNode codeNode = new CodeNode(METHOD_ARG, attribModifier, attribStruct);
        prototypes.put(METHOD_ARG, codeNode);
    }
    private void initMethodBody(){
        IAttribModifier attribModifier = new AttribModifier(METHOD_BODY);
        attribModifier.initRequired(CODE_NODE_TYPE, NAME);
        attribModifier.initAllowed();

        attribModifier.put(CODE_NODE_TYPE, METHOD_BODY.toString());
        attribModifier.put(NAME, METHOD_BODY.toString());

        IAttribStruct attribStruct = new AttribStruct(METHOD_BODY);
        attribStruct.initRequired();
        attribStruct.initAllowed(VAR, IF, RETURN);

        ICodeNode codeNode = new CodeNode(METHOD_BODY, attribModifier, attribStruct);
        prototypes.put(METHOD_BODY, codeNode);
    }
}
