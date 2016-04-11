package project3;

public class MatrixAndVectorOperations {
    public static double[][] createTwoDimensionalMatrix(double[] data, int rows, int columns) {
        double[][] results = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(data, i * columns, results[i], 0, columns);
        }

        return results;
    }

    public static double[] multiplyVectorByMatrix(double[] vector, double[][] matrix) {
        double[] result = new double[vector.length];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i] += vector[j]*matrix[j][i];
            }
        }

        return result;
    }

    public static int[] addIntArrays(int[] left, int[] right) {
        int length = left.length;
        int[] result = new int[length];

        for (int i = 0; i < length; i++) {
            result[i] = left[i] + right[i];
        }

        return result;
    }

    public static double[] addArrays(double[] left, double[] right) {
        int length = left.length;
        double[] result = new double[length];

        for (int i = 0; i < length; i++) {
            result[i] = left[i] + right[i];
        }

        return result;
    }

    public static double[] subtractArrays(double[] left, double[] right) {
        int length = left.length;
        double[] result = new double[length];

        for (int i = 0; i < length; i++) {
            result[i] = left[i] - right[i];
        }

        return result;
    }

    public static double[] divideArrays(double[] dividend, double[] divisor) {
        int length = dividend.length;
        double[] result = new double[length];

        for (int i = 0; i < length; i++) {
            result[i] = dividend[i] / divisor[i];
        }

        return result;
    }

    public static double[] multiplyArrays(double[] left, double[] right) {
        int length = left.length;
        double[] result = new double[length];

        for (int i = 0; i < length; i++) {
            result[i] = left[i] * right[i];
        }

        return result;
    }

    public static double[] applySigmoid(double[] array) {
        int length = array.length;
        double[] result = new double[length];

        for (int i = 0; i < length; i++) {
            result[i] = (1 / (1 + Math.pow(Math.E, (-1 * array[i]))));
        }

        return result;
    }
}
