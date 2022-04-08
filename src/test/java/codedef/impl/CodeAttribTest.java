package codedef.impl;

import codedef.iface.ICodeNode;
import mock.MockSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import runstate.Glob;
import codedef.iface.IAttribModifier;
import codedef.iface.IAttribStruct;
import codedef.enums.MODIFIER;

import static codedef.enums.CODE_NODE.*;
import static codedef.enums.MODIFIER.*;

class CodeAttribTest {
    MockSource mockSource = new MockSource();

    @Test
    void givenDefaultAttribModifier_showDefaults() {
        IAttribModifier attrib = Glob.PROTOTYPE_FACTORY.get(FILE).getAttribModifier();
        String expected = "ICodeAttrib{codeNodeEnum=FILE,required=[TYPE_, NAME_],allowed=[IS_HEADER_],attributes={TYPE_=[FILE]}}";
        String actual = attrib.csvString();
        System.out.println(actual);
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenDefaultAttribStruct_showDefaults() {
        ICodeNode codeNode = Glob.PROTOTYPE_FACTORY.get(FILE);
        IAttribStruct header = codeNode.getStructHeader();
        IAttribStruct body = codeNode.getStructBody();

        String header1 = header.csvString();
        String body1 = body.csvString();
        System.out.println(header1);
        System.out.println(body1);
        Assertions.assertEquals("IAttribStruct{codeNodeEnum=FILE,required=[],allowed=[IMPORT, COMMENT]}}", header1);
        Assertions.assertEquals("IAttribStruct{codeNodeEnum=FILE,required=[CLASS],allowed=[]}}", body1);
    }

    @Test
    void givenPopulatedCodeNode_getJson() {
        ICodeNode codeNode = mockSource.mockCodeNode();
        String expected = "{\"children\":[],\"attributes\":{\"EXTENDS_\":[],\"ACCESS_\":[\"PUBLIC\"],\"FINAL_\":[\"FALSE\"],\"IMPLEMENTS_\":[],\"TYPE_\":[\"CLASS\"],\"ABSTRACT_\":[\"FALSE\"],\"STATIC_\":[\"FALSE\"]}}";
        String actual = codeNode.exportJson().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testUtilEnum() {
        MODIFIER mod = Glob.UTIL_ENUM.fromString(MODIFIER.class, "LANGUAGE_");
        Assertions.assertEquals(mod, LANGUAGE_);

        System.out.println("A: " + mod);
        mod = Glob.UTIL_ENUM.fromString(MODIFIER.class, "BOOBOO");
        Assertions.assertNull(mod);
    }
}