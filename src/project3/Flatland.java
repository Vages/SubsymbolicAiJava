package project3;

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
}
