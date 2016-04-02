package project2.integerSpecializations;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class IntegerCrossBreederTest {

    @Test
    public void testCrossBreed() throws Exception {
        Integer[] a = new Integer[]{0, 0, 0, 0, 0};
        Integer[] b = new Integer[]{1, 1, 1, 1, 1};

        IntegerCrossBreeder cb = new IntegerCrossBreeder(1.0, new Random(0)); // Seed so that crossing point is index 3

        Integer[][] children = cb.crossBreed(a, b);
        Integer[] childAGenotype = children[0];
        Integer[] childBGenotype = children[1];

        assertArrayEquals(new Integer[]{0, 0, 0, 1, 1}, childAGenotype);
        assertArrayEquals(new Integer[]{1, 1, 1, 0, 0}, childBGenotype);
    }
}