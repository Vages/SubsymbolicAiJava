package project3;

import static project3.MatrixAndVectorOperations.*;

import java.util.Arrays;

public class Board {
    private int boardSize = 10;
    private static final MoveDirection[] mdIterable = {MoveDirection.LEFT, MoveDirection.FORWARD, MoveDirection.RIGHT};
    private int[] currentCell;
    private int[] startingCell;
    private Heading playerHeading;
    private Heading startHeading;
    private CellType[][] grid = new CellType[boardSize][boardSize];
    private CellType[][] originalGrid = new CellType[boardSize][boardSize];

    public Board(double f, double p, int[] startingCell) {

        // Fill the board with poison with the given food-poison-distribution
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (j == startingCell[0] && i == startingCell[1]) {
                    setCell(j, i, CellType.EMPTY);
                    continue; // This space will be used as the player space
                }
                if (Math.random() < f) {
                    setCell(j, i, CellType.FOOD);
                } else if (Math.random() < p) {
                    setCell(j, i, CellType.POISON);
                } else {
                    setCell(j, i, CellType.EMPTY);
                }
            }
            originalGrid[i] = Arrays.copyOf(grid[i], boardSize);
        }

        this.startingCell = Arrays.copyOf(startingCell, startingCell.length);
        this.currentCell = Arrays.copyOf(startingCell, startingCell.length);
        startHeading = Heading.SOUTH;
        playerHeading = startHeading;
    }

    public void reset() {
        for (int i = 0; i < boardSize; i++) {
            grid[i] = Arrays.copyOf(originalGrid[i], boardSize);
        }

        this.currentCell = startingCell;
        this.playerHeading = startHeading;
    }

    public CellType getCell(int[] pos) {
        return this.grid[(pos[1] + boardSize) % boardSize][(pos[0] + boardSize) % boardSize];
    }

    public CellType getCell(int x, int y) {
        return this.grid[(y + boardSize) % boardSize][(x + boardSize) % boardSize];
    }

    public int getBoardSize() {
        return this.boardSize;
    }

    public void setCell(int[] pos, CellType contents) {
        int x = pos[0];
        int y = pos[1];
        setCell(x, y, contents);
    }

    public void setCell(int x, int y, CellType contents) {
        this.grid[y][x] = contents;
    }

    public int getPlayerX() {
        return currentCell[0];
    }

    public int getPlayerY() {
        return currentCell[1];
    }

    public Heading getPlayerHeading() {
        return playerHeading;
    }

    public CellType move(MoveDirection d) {
        playerHeading = playerHeading.getHeadingAfterTurn(d); // Turn the robot, if it is to be turned.
        if (d != MoveDirection.STAND_STILL) {
            currentCell = addIntArrays(currentCell, playerHeading.getVector()); // Move it forward in the current heading direction.
            for (int i = 0; i < 2; i++) {
                int value = currentCell[i];
                if (value < 0) {
                    value += boardSize;
                    currentCell[i] = value;
                } else if (value >= boardSize) {
                    value -= boardSize;
                    currentCell[i] = value;
                }
            }
        }

        CellType contents = getCell(currentCell);
        setCell(currentCell, CellType.EMPTY);

        return contents;
    }

    public boolean[] sense(CellType t) {
        boolean[] sensings = new boolean[3];

        for (int i = 0; i < mdIterable.length; i++) {
            MoveDirection md = mdIterable[i];
            int[] relativeCoordinate = playerHeading.getHeadingAfterTurn(md).getVector();

            int[] directionCell = addIntArrays(currentCell, relativeCoordinate);

            CellType contents = this.getCell(directionCell);

            sensings[i] = contents == t;
        }

        return sensings;
    }

    public static void main(String[] args) {
        Board a = new Board(0.33, 0.33, new int[]{0, 0});
        a.sense(CellType.POISON);
        System.out.println("hello");
    }
}
