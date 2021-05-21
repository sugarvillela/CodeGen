package codedef.impl;

import codedef.codenode.CodeNode;
import codedef.iface.ICodeNode;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import runstate.Glob;
import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.init.CodeDef;
import codedef.modifier.ENU_DATA_TYPE;
import codedef.modifier.MODIFIER;

import static codedef.modifier.CODE_NODE.*;
import static codedef.modifier.ENU_VISIBILITY.PUBLIC;
import static codedef.modifier.MODIFIER.*;

class CodeAttribTest {
    private IAttribModifier mockAttribModifier(){
        IAttribModifier attribModifier = new AttribModifier(CLASS);
        attribModifier.initRequired(NAME);
        attribModifier.initAllowed(VISIBILITY, ABSTRACT, STATIC, FINAL, IMPLEMENTS, EXTENDS);
        attribModifier.put(VISIBILITY, PUBLIC.toString());
        attribModifier.put(ABSTRACT, "FALSE");
        attribModifier.put(STATIC, "FALSE");
        attribModifier.put(FINAL, "FALSE");
        attribModifier.put(IMPLEMENTS);
        attribModifier.put(EXTENDS);
        return attribModifier;
    }
    private IAttribStruct mockAttribStruct(){
        IAttribStruct attribStruct = new AttribStruct(CLASS);
        attribStruct.initRequired(IMPORT);
        attribStruct.initAllowed(CLASS_FIELD, METHOD);
        return attribStruct;
    }
    private ICodeNode mockCodeNode(){
        return new CodeNode(CLASS, mockAttribModifier(), mockAttribStruct());
    }
    @Test
    void setSimpleAttribModifier() {
        IAttribModifier attrib = new AttribModifier(FILE);
        attrib.initRequired(NAME, PATH);
        attrib.initAllowed();
        attrib.put(NAME, "attribName");
        attrib.put(PATH, "attribPath");
        System.out.println(attrib.csvString());
        String expected = "ICodeAttrib{codeNodeEnum=FILE,required=[NAME, PATH],allowed=[NAME, PATH],attributes={NAME:[attribName],PATH:[attribPath]}}";
        String actual = attrib.csvString();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void setSimpleAttribStruct() {
        IAttribStruct attrib = new AttribStruct(METHOD);
        attrib.initRequired(METHOD_ARGS);
        attrib.initAllowed();
        System.out.println(attrib.csvString());
        String expected = "IAttribStruct{codeNodeEnum=METHOD,required=[CODE, METHOD_ARGS],allowed=[CODE, METHOD_ARGS]}}";
        String actual = attrib.csvString();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void setVarious() {
        String actual, expected;
        IAttribModifier attrib = new AttribModifier(VAR);
        attrib.initRequired(NAME, DATA_TYPE, QUANTIFIER);
        attrib.initAllowed(STATIC, FINAL, VAR_VALUE);
        attrib.put(STATIC, "FALSE");
        attrib.put(FINAL, "FALSE");

        attrib.put(DATA_TYPE, "INT");
        actual = attrib.get(VAR_VALUE)[0];
        expected = "0";
        Assertions.assertEquals(expected, actual);

        attrib.put(DATA_TYPE, "BOOLEAN");
        actual = attrib.get(VAR_VALUE)[0];
        expected = "FALSE";
        Assertions.assertEquals(expected, actual);

        attrib.put(DATA_TYPE, "STRING");
        actual = attrib.get(VAR_VALUE)[0];
        expected = "";
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void viewAll() {
        CodeDef codeDef = new CodeDef();
    }

    @Test
    void givenPopulatedAttribModifier_getJson() {
        String expected = "{\"allowed\":[\"NAME\",\"VISIBILITY\",\"ABSTRACT\",\"STATIC\",\"FINAL\",\"IMPLEMENTS\",\"EXTENDS\"],\"attributes\":{\"EXTENDS\":[],\"VISIBILITY\":[\"PUBLIC\"],\"IMPLEMENTS\":[],\"FINAL\":[\"FALSE\"],\"ABSTRACT\":[\"FALSE\"],\"STATIC\":[\"FALSE\"]},\"required\":[\"NAME\"]}";
        IAttribModifier attrib = mockAttribModifier();
        System.out.println(attrib.csvString());
        JSONObject jsonObj = attrib.toJson();
        String actual = jsonObj.toString();
        System.out.println(jsonObj);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenPopulatedAttribStruct_getJson() {
        String expected = "{\"allowed\":[\"IMPORT\",\"CLASS_FIELD\",\"METHOD\"],\"required\":[\"IMPORT\"]}";
        IAttribStruct attrib = mockAttribStruct();
        System.out.println(attrib.csvString());
        JSONObject jsonObj = attrib.toJson();
        String actual = jsonObj.toString();
        System.out.println(jsonObj);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenPopulatedCodeNode_getJson() {
        String expected = "{\"attribStruct\":{\"allowed\":[\"IMPORT\",\"CLASS_FIELD\",\"METHOD\"],\"required\":"+
                "[\"IMPORT\"]},\"attribModifier\":{\"allowed\":[\"NAME\",\"VISIBILITY\",\"ABSTRACT\",\"STATIC\","+
                "\"FINAL\",\"IMPLEMENTS\",\"EXTENDS\"],\"attributes\":{\"EXTENDS\":[],\"FINAL\":[\"FALSE\"],"+
                "\"VISIBILITY\":[\"PUBLIC\"],\"IMPLEMENTS\":[],\"ABSTRACT\":[\"FALSE\"],\"STATIC\":[\"FALSE\"]},"+
                "\"required\":[\"NAME\"]},\"codeNodeType\":\"CLASS\"}";
        ICodeNode codeNode = mockCodeNode();
        System.out.println(codeNode.csvString());
        JSONObject jsonObj = codeNode.toJson();
        String actual = jsonObj.toString();
        System.out.println(jsonObj);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void reflect() {
        MODIFIER mod = Glob.UTIL_ENUM.fromString(MODIFIER.class, "ATTRIB_SCOPE");
        System.out.println("A: " + mod);
        mod = Glob.UTIL_ENUM.fromString(MODIFIER.class, "BOOBOO");
        System.out.println("B: " + mod);
        ENU_DATA_TYPE enu = Glob.UTIL_ENUM.fromString(ENU_DATA_TYPE.class, "VOID");
        System.out.println("C: " + enu);
    }
}