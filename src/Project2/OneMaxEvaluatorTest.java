package Project2;

import org.junit.Test;

import static org.junit.Assert.*;

public class OneMaxEvaluatorTest {

    @Test
    public void testEvaluate() throws Exception {
        OneMaxIndividual noErrors = new OneMaxIndividual(new int[]{1, 1, 1});
        OneMaxIndividual oneError = new OneMaxIndividual(new int[]{1, 1, 0});
        OneMaxIndividual twoErrors = new OneMaxIndividual(new int[]{1, 0, 0});
        OneMaxIndividual threeErrors = new OneMaxIndividual(new int[]{0, 0, 0});

        assertEquals(OneMaxEvaluator.evaluate(noErrors), 1, 0.001);
        assertEquals(OneMaxEvaluator.evaluate(oneError), 1.0/(1+1), 0.001);
        assertEquals(OneMaxEvaluator.evaluate(twoErrors), 1.0/(1+2), 0.001);
        assertEquals(OneMaxEvaluator.evaluate(threeErrors), 1.0/(1+3), 0.001);

    }
}