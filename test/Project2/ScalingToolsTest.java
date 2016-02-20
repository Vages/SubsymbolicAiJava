package Project2;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScalingToolsTest {

    @Test
    public void testSigmaScale() throws Exception {
        // Test for ten element data set.
        double mean = 1.7;
        double standardDeviation = 1.6364;

        assertEquals(0.7861, ScalingTools.sigmaScale(1, mean, standardDeviation), 0.0001);
        assertEquals(1.3972, ScalingTools.sigmaScale(3, mean, standardDeviation), 0.0001);
        assertEquals(2.3139, ScalingTools.sigmaScale(6, mean, standardDeviation), 0.0001);
    }
}