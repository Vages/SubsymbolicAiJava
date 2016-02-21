package project2;

import project2.integerSpecializations.evaluators.SurprisingSequenceEvaluator;
import org.junit.Test;

import static org.junit.Assert.*;

public class SurprisingSequenceEvaluatorTest {

    @Test
    public void testEvaluate() throws Exception {
        SurprisingSequenceEvaluator locallySurprising = new SurprisingSequenceEvaluator(false);
        SurprisingSequenceEvaluator globallySurprising = new SurprisingSequenceEvaluator(true);

        int[] abccba = {0, 1, 2, 2, 1, 0};
        int[] aabcc = {0,0,1,2,2};
        int[] abbacca = {0,1,1,0,2,2,0};
        int[] abcbc = {0,1,2,1,2};

        // ABCCBA should be surprising globally and locally

        assertEquals(1.0, locallySurprising.evaluate(abccba), 0.001);
        assertEquals(1.0, globallySurprising.evaluate(abccba), 0.001);


        // AABCC should be locally surprising, but not globally

        assertEquals(1.0, locallySurprising.evaluate(aabcc), 0.001);
        assertNotEquals(1.0, globallySurprising.evaluate(aabcc), 0.001);

        // ABBACCA should be locally surprising, but not globally

        assertEquals(1.0, locallySurprising.evaluate(abbacca), 0.001);
        assertNotEquals(1.0, globallySurprising.evaluate(abbacca), 0.001);

        // ABCBC should be surprising neither globally nor locally

        assertNotEquals(1.0, locallySurprising.evaluate(abcbc), 0.001);
        assertNotEquals(1.0, globallySurprising.evaluate(abcbc), 0.001);

    }
}