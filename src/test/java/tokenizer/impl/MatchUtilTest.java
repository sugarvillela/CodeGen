package tokenizer.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import tokenizer.iface.IMatchUtil;

import java.util.List;

public class MatchUtilTest {
    @Test
    void givenStringWithTarget_findTargetByChar(){
        String text = "zero D_E D_END three four five six seven eight nine ten";
        IMatchUtil matchUtil = new MatchUtil().setNeedle("D_END").setHaystack(text).setMatchOnce(true);

        int actual1 = -1, actual2 = -1;
        int expected1 = 9, expected2 = 14;
        for(int i = 0; i < text.length(); i++){
            char curr = text.charAt(i);
            if(matchUtil.parseByChar(i)){
                actual1 = matchUtil.getIndexStart();
                actual2 = matchUtil.getIndexFinish();;
            }
        }
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }
    @Test
    void givenStringWithTarget_findTargetInString(){
        String needle = "three";
        String haystack = "zero one two three four five six seven eight nine ten";
        IMatchUtil matchUtil = new MatchUtil().setNeedle(needle).setHaystack(haystack).setMatchOnce(true);

        int actual1 = -1, actual2 = -1;
        int expected1 = 13, expected2 = 18;
        if(matchUtil.parse().haveMatch()){
            actual1 = matchUtil.getIndexStart();
            actual2 = matchUtil.getIndexFinish();;
        }
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }
    @Test
    void givenStringWithManyTargets_findNumOccurs(){
        String needle = "three";
        String haystack = "zero one two three four threethree six seven eight nine ten";
        IMatchUtil matchUtil = new MatchUtil().setNeedle(needle).setHaystack(haystack);
        int actual1 = matchUtil.parse().numOccurs();
        int expected1 = 3;
        Assertions.assertEquals(expected1, actual1);
    }
    @Test
    void givenStringWithQuotedTarget_findNumOccurs(){
        String needle = "three";
        String haystack = "zero one two three four \"three\"three six seven eight nine ten";
        IMatchUtil matchUtil = new MatchUtil().setNeedle(needle).setHaystack(haystack);
        int actual1 = matchUtil.parse().numOccurs();
        int expected1 = 2;
        Assertions.assertEquals(expected1, actual1);
    }
    @Test
    void givenStringWithEscapedChar_findNumOccurs(){
        String needle = "three";
        String haystack = "zero one two \\three four \"three\"three six seven eight nine ten";
        IMatchUtil matchUtil = new MatchUtil().setNeedle(needle).setHaystack(haystack);
        int actual1 = matchUtil.parse().numOccurs();
        int expected1 = 1;
        Assertions.assertEquals(expected1, actual1);
    }
    @Test
    void givenStringWithShortTarget_findTargetInString() {
        String needle = "{";
        String haystack = "zero one {two three four five six seven eight nine ten";
        IMatchUtil matchUtil = new MatchUtil().setNeedle(needle).setHaystack(haystack).setMatchOnce(true);
        int actual1 = -1, actual2 = -1;
        int expected1 = 9, expected2 = 10;
        if (matchUtil.parse().haveMatch()) {
            actual1 = matchUtil.getIndexStart();
            actual2 = matchUtil.getIndexFinish();
            ;
        }
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }
    @Test
    void givenStringWithManyTargets_getHitMap(){
        String needle = "three";
        String haystack = "zero one two three four threethree six seven eight nine ten";
        IMatchUtil matchUtil = new MatchUtil().setNeedle(needle).setHaystack(haystack);
        List<Integer> hitMap = matchUtil.parse().getHitMap();
        int[] expected = {13,18,24,29,29,34};
        Assertions.assertEquals(hitMap.size(), expected.length);
        for(int i = 0; i < expected.length; i++){
            Assertions.assertEquals(expected[i], hitMap.get(i));
        }
    }
    @Test
    void givenStringWithTarget_deleteTarget(){
        String needle = "three";
        String haystack = "zero one two three four five six seven eight nine ten";
        IMatchUtil matchUtil = new MatchUtil().setNeedle(needle).setHaystack(haystack).setMatchOnce(true).setDeleteNeedle(true);

        String actual1 = matchUtil.parse().getHaystack();
        String expected1 = "zero one two  four five six seven eight nine ten";
        Assertions.assertEquals(expected1, actual1);
    }
    @Test
    void givenStringWithManyTargets_deleteAll(){
        String needle = "three";
        String haystack = "zero one two three four threethree six seven eight nine ten";
        IMatchUtil matchUtil = new MatchUtil().setNeedle(needle).setHaystack(haystack).setDeleteNeedle(true);

        String actual1 = matchUtil.parse().getHaystack();
        String expected1 = "zero one two  four  six seven eight nine ten";
        Assertions.assertEquals(expected1, actual1);
    }
}
