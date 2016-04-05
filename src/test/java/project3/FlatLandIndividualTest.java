package project3;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FlatlandIndividualTest {

    @Test
    public void testGenerateRandomGenotype() throws Exception {
        int[] two_layer_topology = {3, 3};
        int[] three_layer_topology = {1, 1, 1};

        Double[] twoLayerWeights = FlatlandIndividual.generateRandomGenotype(two_layer_topology);
        Double[] threeLayerWeights = FlatlandIndividual.generateRandomGenotype(three_layer_topology);

        assertEquals(12, twoLayerWeights.length);
        assertEquals(4, threeLayerWeights.length);

        List<Double> twoList = Arrays.asList(twoLayerWeights);
        List<Double> threeList = Arrays.asList(threeLayerWeights);

        assertTrue(Collections.max(twoList)<=1);
        assertTrue(Collections.max(threeList)<=1);

        assertTrue(Collections.min(twoList)>=-1);
        assertTrue(Collections.min(threeList)>=-1);
    }
}