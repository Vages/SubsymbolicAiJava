package project3;

public enum Heading {
    NORTH, EAST, SOUTH, WEST;

    private int[] vector;
    private Heading left;
    private Heading right;

    static {
        NORTH.vector = new int[]{0, -1};
        EAST.vector = new int[]{1, 0};
        SOUTH.vector = new int[]{0, 1};
        WEST.vector =  new int[]{-1, 0};

        NORTH.left = WEST;
        EAST.left = NORTH;
        SOUTH.left = EAST;
        WEST.left = SOUTH;

        NORTH.right = EAST;
        EAST.right = SOUTH;
        SOUTH.right = WEST;
        WEST.right = NORTH;
    }

    public int[] getVector() {
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

    public double getVectorY(){
        return vector[1];
    }

    public double getVectorX(){
        return vector[0];
    }
}
