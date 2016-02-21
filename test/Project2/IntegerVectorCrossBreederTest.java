package project2;

import org.junit.Test;
import project2.integerSpecializations.IntegerVectorCrossBreeder;

import java.util.Random;

import static org.junit.Assert.*;

public class IntegerVectorCrossBreederTest {

    @Test
    public void testCrossBreed() throws Exception {
        int[] a = new int[]{0, 0, 0, 0, 0};
        int[] b = new int[]{1, 1, 1, 1, 1};

        IntegerVectorCrossBreeder cb = new IntegerVectorCrossBreeder(new Random(0)); // Seed so that crossing point is index 3

        int[][] children = cb.crossBreed(a, b);
        int[] childAGenotype = children[0];
        int[] childBGenotype = children[1];

        assertArrayEquals(new int[]{0, 0, 0, 1, 1}, childAGenotype);
        assertArrayEquals(new int[]{1, 1, 1, 0, 0}, childBGenotype);
    }
}