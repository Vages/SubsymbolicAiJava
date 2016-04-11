package project3;

import org.junit.Test;

import static org.junit.Assert.*;
import static project3.MatrixAndVectorOperations.multiplyVectorByMatrix;

public class MatrixAndVectorOperationsTest {

    @Test
    public void testMultiplyVectorByMatrix() throws Exception {
        double[] simpleVector = {7,8,9};
        double[][] bigMatrix = {{1, 2, 3}, {8, 9, 4}, {7, 6, 5}};
        double[] expected = {2*67, 2*70, 2*49};

        assertArrayEquals(expected, multiplyVectorByMatrix(simpleVector, bigMatrix), 0.1);
    }
}