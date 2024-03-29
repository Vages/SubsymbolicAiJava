package project2.integerSpecializations;

import project2.integerSpecializations.IntegerIndividual;

import static org.junit.Assert.*;

public class IntegerIndividualTest {

    @org.junit.Test
    public void testGetPhenotype() throws Exception {
        Integer[] genotype = {1, 0, 1};
        IntegerIndividual omi = new IntegerIndividual(genotype, null);
        Integer[] phenotype = omi.getPhenotype();

        assertArrayEquals(genotype, phenotype); // Array is an exact copy
        assertNotEquals(genotype, phenotype); // Array references are not the same
    }

    @org.junit.Test
    public void testGetGenotype() throws Exception {
        Integer[] genotype = {1, 0, 1};
        IntegerIndividual omi = new IntegerIndividual(genotype, null);
        Integer[] returnedGenotype = omi.getGenotype();

        assertArrayEquals(genotype, returnedGenotype); // Array is an exact copy
        assertNotEquals(genotype, returnedGenotype); // Array references are not the same
    }
}