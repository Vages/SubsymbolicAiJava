package project2;

import project2.integerSpecializations.evaluators.LolzEvaluator;
import org.junit.Test;

import static org.junit.Assert.*;

public class LolzEvaluatorTest {

    @Test
    public void testEvaluate() throws Exception {
        LolzEvaluator lolzEvaluator = new LolzEvaluator(4);

        int[] allOnes = {1,1,1,1,1,1};
        int[] allZeroes = {0,0,0,0,0,0};
        int[] twoLeadingOnes = {1,1,0,1,0,1};
        int[] twoLeadingZeroes = {0,0,1,0,1,1};

        assertEquals(lolzEvaluator.evaluate(allOnes), 6, 0.001);
        assertEquals(lolzEvaluator.evaluate(allZeroes), 4, 0.001);
        assertEquals(lolzEvaluator.evaluate(twoLeadingOnes), 2, 0.001);
        assertEquals(lolzEvaluator.evaluate(twoLeadingZeroes), 2, 0.001);
    }
}