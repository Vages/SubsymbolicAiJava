package project2;

import project2.evaluators.OneMaxEvaluator;
import org.junit.Test;

import static org.junit.Assert.*;

public class OneMaxEvaluatorTest {

    @Test
    public void testEvaluate() throws Exception {
        int[] idealPhenotype = {1, 1, 1};
        OneMaxEvaluator evaluator = new OneMaxEvaluator(idealPhenotype);

        IntegerIndividual noErrors = new IntegerIndividual(new int[]{1, 1, 1}, evaluator);
        IntegerIndividual oneError = new IntegerIndividual(new int[]{1, 1, 0}, evaluator);
        IntegerIndividual twoErrors = new IntegerIndividual(new int[]{1, 0, 0}, evaluator);
        IntegerIndividual threeErrors = new IntegerIndividual(new int[]{0, 0, 0}, evaluator);

        assertEquals(evaluator.evaluate(noErrors.getPhenotype()), 1, 0.001);
        assertEquals(evaluator.evaluate(oneError.getPhenotype()), 1.0/(1+1), 0.001);
        assertEquals(evaluator.evaluate(twoErrors.getPhenotype()), 1.0/(1+2), 0.001);
        assertEquals(evaluator.evaluate(threeErrors.getPhenotype()), 1.0/(1+3), 0.001);

    }
}