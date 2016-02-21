package project2;

import org.junit.Test;
import project2.tools.ScalingTools;

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

    @Test
    public void testAverage() throws Exception {
        double[] doubles = {1,1,1,1,1,1,1,1,3,6};

        assertEquals(1.7, ScalingTools.average(doubles), 0.001);
    }

    @Test
    public void testStandardDeviation() throws Exception {
        double[] doubles = {1,1,1,1,1,1,1,1,3,6};

        assertEquals(1.6364, ScalingTools.standardDeviation(doubles, ScalingTools.average(doubles)), 0.0001);
    }
}