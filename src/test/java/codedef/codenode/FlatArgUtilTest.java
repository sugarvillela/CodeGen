package codedef.codenode;

import codedef.enums.MODIFIER;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static codedef.enums.MODIFIER.NAME_;
import static codedef.enums.MODIFIER.TYPE_;
import static org.junit.jupiter.api.Assertions.*;

class FlatArgUtilTest {
    FlatArgUtil subject;

    @BeforeEach
    void before(){
        subject =  new FlatArgUtil();
    }

    @Test
    void givenNonFlatArray_detect() {
        boolean actual = subject.isFlat(
                42, new long[]{43, 44}, 45
        );
        assertFalse(actual);
    }

    @Test
    void givenNonFlatLongArray_flatten() {
        Object[] actual = subject.flatten(
                42L, new long[]{43L, 44L}, 45L
        );
        Object[] expected = new Object[]{42L, 43L, 44L, 45L};
        assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    void givenNonFlatMixedArray_flatten() {
        Object[] actual = subject.flatten(
                42L, new String[]{"happy", "birthday"}, 45L
        );
        Object[] expected = new Object[]{42L, "happy", "birthday", 45L};
        assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    void givenNonFlatDoubleArray_flatten() {
        Object[] actual = subject.flatten(
                3.22d, new double[]{43d, 6.9d}, 14d
        );
        Object[] expected = new Object[]{3.22d, 43d, 6.9d, 14d};
        assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    void givenListArg_detect() {
        boolean actual = subject.isFlat(
                42, Arrays.asList(43, 44), 45
        );
        assertFalse(actual);
    }

    @Test
    void givenListArg_flatten() {
        Object[] actual = subject.flatten(
                42, Arrays.asList(43, 44), 45
        );
        Object[] expected = new Object[]{42, 43, 44, 45};
        for(Object o : expected){
            System.out.println(o.getClass().getSimpleName());
        }
        assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    void givenMixedListArg_flatten() {
        Object[] actual = subject.flatten(
                42, Arrays.asList("happy", "birthday"), Arrays.asList(true, false), 45
        );
        Object[] expected = new Object[]{42, "happy", "birthday", true, false, 45};
        for(Object o : expected){
            System.out.println(o.getClass().getSimpleName());
        }
        assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    void givenInvalidArrayArg_errAndQuit() {
        Object[] actual = subject.flatten(
                42L,
                new MODIFIER[]{NAME_, TYPE_},
                45L
        );
    }

    @Test
    void givenInvalidListArg_errAndQuit() {
        Object[] actual = subject.flatten(
                42L,
                Arrays.asList(NAME_, TYPE_),
                45L
        );
    }
}