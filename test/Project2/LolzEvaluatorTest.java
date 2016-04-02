package project2;

import project2.integerSpecializations.evaluators.LolzEvaluator;
import org.junit.Test;

import static org.junit.Assert.*;

public class LolzEvaluatorTest {

    @Test
    public void testEvaluate() throws Exception {
        LolzEvaluator lolzEvaluator = new LolzEvaluator(4);

        Integer[] allOnes = {1,1,1,1,1,1};
        Integer[] allZeroes = {0,0,0,0,0,0};
        Integer[] twoLeadingOnes = {1,1,0,1,0,1};
        Integer[] twoLeadingZeroes = {0,0,1,0,1,1};

        assertEquals(lolzEvaluator.evaluate(allOnes), 6, 0.001);
        assertEquals(lolzEvaluator.evaluate(allZeroes), 4, 0.001);
        assertEquals(lolzEvaluator.evaluate(twoLeadingOnes), 2, 0.001);
        assertEquals(lolzEvaluator.evaluate(twoLeadingZeroes), 2, 0.001);
    }
}