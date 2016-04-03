package project3;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private int boardSize = 10;
    private static final Map<Heading, Map<MoveDirection, int[]>> directionToSensorAndMoveCoordinates;
    private static final MoveDirection[] mdIterable = {MoveDirection.LEFT, MoveDirection.FORWARD, MoveDirection.RIGHT};
    private int[] playerPosition = new int[2];
    private int[] startingCell = new int[2];
    private Heading playerHeading;
    private CellType[][] grid = new CellType[boardSize][boardSize];

    static {
        // Fill the Map with the right heading/direction mappings

        directionToSensorAndMoveCoordinates = new HashMap<>();
        int[][] relativeCoordinates = new int[][]{{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
        Heading[] heads = {Heading.NORTH, Heading.WEST, Heading.SOUTH, Heading.EAST};

        for (int i = 0; i < 4; i++) {
            Heading h = heads[i];
            Map<MoveDirection, int[]> this_hs_map = new HashMap<>();
            for (int j = 0; j < 3; j++) {
                int i1 = (i + j + 4 - 1) % 4;
                this_hs_map.put(mdIterable[j], relativeCoordinates[i1]);
            }
            directionToSensorAndMoveCoordinates.put(h, this_hs_map);
        }
    }

    public Board(double f, double p, int[] startingCell) {

        // Fill the board with poison with the given food-poison-distribution
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (i == 0 && j == 0) {
                    continue; // This space will be used as the player space
                }
                if (Math.random() < f) {
                    grid[i][j] = CellType.FOOD;
                } else if (Math.random() < p) {
                    grid[i][j] = CellType.POISON;
                } else {
                    grid[i][j] = CellType.EMPTY;
                }
            }
        }

        System.arraycopy(startingCell, 0, this.startingCell, 0, 2); // Set the starting cell
        System.arraycopy(startingCell, 0, playerPosition, 0, 2); // Set the current position
        playerHeading = Heading.SOUTH;
    }

    public CellType getCell(int x, int y) {
        return this.grid[(y + boardSize) % boardSize][(x + boardSize) % boardSize];
    }

    public CellType move(MoveDirection d) {
        return null;
    }

    public boolean[] sensePoison() {
        boolean[] poisonSensings = new boolean[3];

        return poisonSensings;
    }

    public boolean[] senseFood() {
        return null;
    }

    public static void main(String[] args) {
        Board a = new Board(0.33, 0.33, new int[]{0, 0});
        System.out.println("hello");
    }
}
