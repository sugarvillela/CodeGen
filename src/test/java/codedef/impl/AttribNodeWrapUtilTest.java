package codedef.impl;

import static codedef.enums.ENU_BOOLEAN.TRUE;
import static org.junit.jupiter.api.Assertions.*;

import codedef.enums.MODIFIER;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import runstate.Glob;

import java.util.Arrays;
import java.util.List;

class AttribNodeWrapUtilTest {
    AttribConvertUtil subject;

    @BeforeEach
    void beforeEach(){
        subject = Glob.ATTRIB_CONVERT_UTIL;
    }
    @Test
    void givenGoodInput_returnGoodOutput(){
        String[] actual, expected;

        actual = subject.convert(MODIFIER.IS_HEADER_, TRUE);
        expected = new String[]{"TRUE"};
        expected.equals(new String[1]);
        assertTrue(Arrays.equals(actual, expected));

        actual = subject.convert(MODIFIER.IS_HEADER_, "TRUE");
        expected = new String[]{"TRUE"};
        assertTrue(Arrays.equals(actual, expected));

        actual = subject.convert(MODIFIER.NAME_, "myName");
        expected = new String[]{"myName"};
        assertTrue(Arrays.equals(actual, expected));

        actual = subject.convert(MODIFIER.INDENT_, "4");
        expected = new String[]{"4"};
        assertTrue(Arrays.equals(actual, expected));

        actual = subject.convert(MODIFIER.INDENT_, 4);
        expected = new String[]{"4"};
        assertTrue(Arrays.equals(actual, expected));
    }
    @Test
    void givenBadInput_errAndQuit(){
        String[] actual = subject.convert(MODIFIER.IS_HEADER_, "munchie");
    }

    @Test
    void givenWrongQuantityArgs_errAndQuit(){
        String[] actual = subject.convert(MODIFIER.INDENT_, 4, 5, 6);
    }
    @Test
    void givenNoArgs_errAndQuit(){
        String[] actual = subject.convert(MODIFIER.LIT_);
    }
}