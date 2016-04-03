package project3;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public enum Heading {
    NORTH, EAST, SOUTH, WEST;

    private INDArray vector;
    private Heading left;
    private Heading right;

    static {
        NORTH.vector = Nd4j.create(new double[]{0, -1});
        EAST.vector = Nd4j.create(new double[]{1, 0});
        SOUTH.vector = Nd4j.create(new double[]{0, 1});
        WEST.vector =  Nd4j.create(new double[]{-1, 0});

        NORTH.left = WEST;
        EAST.left = NORTH;
        SOUTH.left = EAST;
        WEST.left = SOUTH;

        NORTH.right = EAST;
        EAST.right = SOUTH;
        SOUTH.right = WEST;
        WEST.right = NORTH;
    }

    public INDArray getVector() {
        return vector;
    }

    public Heading getHeadingAfterTurn(MoveDirection md){
        if (md == MoveDirection.LEFT) {
            return left;
        } else if (md == MoveDirection.RIGHT) {
            return right;
        } else {
            return this;
        }
    }
}
