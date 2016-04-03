package project3;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private int boardSize = 10;
    private static Map<Heading, Integer[][]> directionToSensorAndMoveCoordinates = new HashMap<>();
    private int[] playerPosition;
    private Heading playerHeading;
    private CellType[][] grid = new CellType[boardSize][boardSize];

    public Board() {
        for (int i = 0; i<boardSize; i++){
            for (int j = 0; j<boardSize; j++){
                if (i == 0 && j == 0) {
                    continue; // This space will be used as the player space
                }
                if (Math.random() < 0.33) {
                    grid[i][j] = CellType.FOOD;
                } else if (Math.random() < 0.33) {
                    grid[i][j] = CellType.POISON;
                } else {
                    grid[i][j] = CellType.EMPTY;
                }
            }
        }

        playerPosition = new int[]{0,0};
        playerHeading = Heading.SOUTH;
    }


}
