package codedef.codenode;

import codedef.iface.ICodeNode;
import codedef.impl.PrototypeFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import runstate.Glob;

import java.util.ArrayList;
import java.util.List;

import static codedef.enums.CODE_NODE.*;
import static codedef.enums.MODIFIER.LIT_;
import static codedef.enums.MODIFIER.TYPE_DATA_;

class NodeWrapUtilTest {

    @Test
    void givenAllNode_returnNodeList() {
        PrototypeFactory f = Glob.PROTOTYPE_FACTORY;
        ICodeNode[] objects = {
                f.get(FUN_CALL),
                f.get(NEW_OBJECT).putAttrib(TYPE_DATA_, "myObject"),
                f.get(COMMENT).putAttrib(LIT_, "myComment")
        };

        for(ICodeNode object : objects){
            System.out.println(object.toString());
        }
        List<ICodeNode> expected = new ArrayList<>(3);
        for(ICodeNode object : objects){
            expected.add(object);
        }

        List<ICodeNode> actual = Glob.CONVERT_UTIL.convert(objects);

        Assertions.assertEquals(expected, actual);
    }
    @Test
    void givenAllLong_returnNodeList() {
        PrototypeFactory f = Glob.PROTOTYPE_FACTORY;
        Object[] objects = {0x68L, 0x69L, 0x754L};

        for(Object object : objects){
            System.out.println(object.toString());
        }
        List<ICodeNode> expected = new ArrayList<>(1);
        expected.add(f.get(LIT).putAttrib(LIT_, "0x68L", "0x69L", "0x754L"));

        List<ICodeNode> actual = Glob.CONVERT_UTIL.convert(objects);
        System.out.println(expected);
        System.out.println(actual);
        Assertions.assertEquals(expected.toString(), actual.toString());
    }
    @Test
    void givenMixedPrim_returnNodeList() {
        PrototypeFactory f = Glob.PROTOTYPE_FACTORY;
        Object[] objects = {23.697, 0x69L, "myString"};

        for(Object object : objects){
            System.out.println(object.toString());
        }
        List<ICodeNode> expected = new ArrayList<>(1);
        expected.add(f.get(LIT).putAttrib(LIT_, "0x1.7b26e978d4fdfp4", "0x69L", "myString"));

        List<ICodeNode> actual = Glob.CONVERT_UTIL.convert(objects);
        System.out.println(expected);
        System.out.println(actual);
        Assertions.assertEquals(expected.toString(), actual.toString());
    }
    @Test
    void givenNodeAndPrim_returnNodeList() {
        PrototypeFactory f = Glob.PROTOTYPE_FACTORY;
        Object[] objects = {
                f.get(FUN_CALL),
                0x69L,
                "myString"
        };

        List<ICodeNode> expected = new ArrayList<>(3);
        expected.add(f.get(FUN_CALL));
        expected.add(f.get(LIT).putAttrib(LIT_, "0x69L"));
        expected.add(f.get(LIT).putAttrib(LIT_, "myString"));

        List<ICodeNode> actual = Glob.CONVERT_UTIL.convert(objects);
        System.out.println(expected);
        System.out.println(actual);
//        for(ICodeNode codeNode : actual){
//            System.out.println(codeNode.toString());
//        }
        Assertions.assertEquals(expected.toString(), actual.toString());
    }
}