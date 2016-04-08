package project4;

import org.junit.Test;

import static org.junit.Assert.*;

public class BeerTrackerIndividualTest {

    @Test
    public void testGenerateRandomGenotype() throws Exception {
        int expectedLength = 34;
        int[] topology = new int[]{5, 2, 2};

        NeuralNetworkGene[] genotype = BeerTrackerIndividual.generateRandomGenotype(topology);

        assertEquals(expectedLength, genotype.length);
    }
}