package project3;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private int boardSize = 10;
    private static final Map<Heading, Map<MoveDirection, INDArray>> directionToSensorAndMoveCoordinates;
    private static final MoveDirection[] mdIterable = {MoveDirection.LEFT, MoveDirection.FORWARD, MoveDirection.RIGHT};
    private INDArray currentCell;
    private INDArray startingCell;
    private Heading playerHeading;
    private CellType[][] grid = new CellType[boardSize][boardSize];

    static {
        // Fill the Map with the right heading/direction mappings

        directionToSensorAndMoveCoordinates = new HashMap<>();

        INDArray[] relativeCoordinates = new INDArray[]{Nd4j.create(new double[]{0, -1}), // North
                Nd4j.create(new double[]{1, 0}), // East
                Nd4j.create(new double[]{0, 1}), // South
                Nd4j.create(new double[]{-1, 0})}; // West
        Heading[] heads = {Heading.NORTH, Heading.EAST, Heading.SOUTH, Heading.WEST};

        for (int i = 0; i < 4; i++) {
            Heading h = heads[i];
            Map<MoveDirection, INDArray> this_hs_map = new HashMap<>();
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
                if (i == startingCell[1] && j == startingCell[0]) {
                    grid[i][j] = CellType.EMPTY;
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

        double[] startAsDouble = new double[2];
        startAsDouble[0] = startingCell[0];
        startAsDouble[1] = startingCell[1];

        this.startingCell = Nd4j.create(startAsDouble);
        this.currentCell = Nd4j.create(startAsDouble);
        playerHeading = Heading.SOUTH;
    }

    public CellType getCell(int x, int y) {
        return this.grid[(y + boardSize) % boardSize][(x + boardSize) % boardSize];
    }

    public CellType move(MoveDirection d) {
        return null;
    }

    public boolean[] sense(CellType t) {
        boolean[] sensings = new boolean[3];

        Map<MoveDirection, INDArray> coordinates = directionToSensorAndMoveCoordinates.get(playerHeading);

        for (int i = 0; i < mdIterable.length; i++) {
            MoveDirection md = mdIterable[i];
            INDArray relativeCoordinate = coordinates.get(md);

            INDArray directionCell = currentCell.add(relativeCoordinate);

            CellType contents = this.getCell((int) directionCell.getDouble(0), (int) directionCell.getDouble(1));

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
