package project3;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;

public class Board {
    private int boardSize = 10;
    private static final MoveDirection[] mdIterable = {MoveDirection.LEFT, MoveDirection.FORWARD, MoveDirection.RIGHT};
    private INDArray currentCell;
    private INDArray startingCell;
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

        double[] startAsDouble = new double[2];
        startAsDouble[0] = startingCell[0];
        startAsDouble[1] = startingCell[1];

        this.startingCell = Nd4j.create(startAsDouble);
        this.currentCell = Nd4j.create(startAsDouble);
        startHeading = Heading.SOUTH;
        playerHeading = startHeading;
    }

    public void reset(){
        for (int i = 0; i < boardSize; i++) {
            grid[i] = Arrays.copyOf(originalGrid[i], boardSize);
        }

        this.currentCell = startingCell;
        this.playerHeading = startHeading;
    }

    public CellType getCell(INDArray pos) {
        return this.grid[((int) pos.getDouble(1) + boardSize) % boardSize][((int) pos.getDouble(0) + boardSize) % boardSize];
    }

    public CellType getCell(int x, int y) {
        return this.grid[(y+boardSize)%boardSize][(x+boardSize)%boardSize];
    }

    public int getBoardSize(){
        return this.boardSize;
    }

    public void setCell(INDArray pos, CellType contents){
        int x = (int) pos.getDouble(0);
        int y = (int) pos.getDouble(1);
        setCell(x, y, contents);
    }

    public void setCell(int x, int y, CellType contents) {
        this.grid[y][x] = contents;
    }

    public int getPlayerX(){
        return (int) currentCell.getDouble(0);
    }

    public int getPlayerY(){
        return (int) currentCell.getDouble(1);
    }

    public Heading getPlayerHeading() {
        return playerHeading;
    }

    public CellType move(MoveDirection d) {
        playerHeading = playerHeading.getHeadingAfterTurn(d); // Turn the robot, if it is to be turned.
        if (d != MoveDirection.STAND_STILL) {
            currentCell = currentCell.add(playerHeading.getVector()); // Move it forward in the current heading direction.
            for (int i = 0; i < 2; i++) {
                double value = currentCell.getDouble(i);
                if (value < 0) {
                    value += boardSize;
                    currentCell.putScalar(i, value);
                } else if (value >= boardSize ) {
                    value -= boardSize;
                    currentCell.putScalar(i, value);
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
            INDArray relativeCoordinate = playerHeading.getHeadingAfterTurn(md).getVector();

            INDArray directionCell = currentCell.add(relativeCoordinate);

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
