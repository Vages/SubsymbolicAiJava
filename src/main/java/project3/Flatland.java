package project3;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class Flatland {
    private FlatlandCellTypes[][] grid = new FlatlandCellTypes[10][10];

    public Flatland() {
        for (int i = 0; i<10; i++){
            for (int j = 0; j<10; j++){
                if (i == 0 && j == 0) {
                    continue;
                }
                if (Math.random() < 0.33) {
                    grid[i][j] = FlatlandCellTypes.FOOD;
                } else if (Math.random() < 0.33) {
                    grid[i][j] = FlatlandCellTypes.POISON;
                } else {
                    grid[i][j] = FlatlandCellTypes.EMPTY;
                }
            }
        }
    }

    public static void main(String[] args) {
        INDArray arr1 = Nd4j.create(new float[]{1,2,3,4},new int[]{2,2});
        System.out.println(arr1);
    }
}
