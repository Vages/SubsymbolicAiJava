package Project2;

import org.junit.Test;

import static org.junit.Assert.*;

public class OneMaxEvaluatorTest {

    @Test
    public void testEvaluate() throws Exception {
        int[] idealPhenotype = {1, 1, 1};
        OneMaxEvaluator evaluator = new OneMaxEvaluator(idealPhenotype);

        OneMaxIndividual noErrors = new OneMaxIndividual(new int[]{1, 1, 1}, null);
        OneMaxIndividual oneError = new OneMaxIndividual(new int[]{1, 1, 0}, null);
        OneMaxIndividual twoErrors = new OneMaxIndividual(new int[]{1, 0, 0}, null);
        OneMaxIndividual threeErrors = new OneMaxIndividual(new int[]{0, 0, 0}, null);

        assertEquals(evaluator.evaluate(noErrors.getPhenotype()), 1, 0.001);
        assertEquals(evaluator.evaluate(oneError.getPhenotype()), 1.0/(1+1), 0.001);
        assertEquals(evaluator.evaluate(twoErrors.getPhenotype()), 1.0/(1+2), 0.001);
        assertEquals(evaluator.evaluate(threeErrors.getPhenotype()), 1.0/(1+3), 0.001);

    }
}