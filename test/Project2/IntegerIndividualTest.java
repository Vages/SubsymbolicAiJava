package project2;

import static org.junit.Assert.*;

public class IntegerIndividualTest {

    @org.junit.Test
    public void testGetPhenotype() throws Exception {
        int[] genotype = {1, 0, 1};
        IntegerIndividual omi = new IntegerIndividual(genotype, null);
        int[] phenotype = omi.getPhenotype();

        assertArrayEquals(genotype, phenotype); // Array is an exact copy
        assertNotEquals(genotype, phenotype); // Array references are not the same
    }

    @org.junit.Test
    public void testGetGenotype() throws Exception {
        int[] genotype = {1, 0, 1};
        IntegerIndividual omi = new IntegerIndividual(genotype, null);
        int[] returnedGenotype = omi.getGenotype();

        assertArrayEquals(genotype, returnedGenotype); // Array is an exact copy
        assertNotEquals(genotype, returnedGenotype); // Array references are not the same
    }
}