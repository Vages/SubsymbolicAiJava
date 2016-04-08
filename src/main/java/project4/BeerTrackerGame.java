package project4;

import java.util.HashSet;
import java.util.Set;

public class BeerTrackerGame {
    private int width;
    private int height;
    private int trackerPosition = 0;
    private int trackerWidth;
    private int fallingObjectXPosition;
    private int fallingObjectYPosition;

    public BeerTrackerGame(int width, int height, int trackerWidth) {
        this.width = width;
        this.height = height;
        this.trackerWidth = trackerWidth;
        spawnNewFallingObject();
    }

    /**
     * Performs an action requested by the player
     *
     * @param a action to be performed
     * @param magnitude the magnitude of the action (used in conjunction with moving left and right)
     */
    public void performAction(TrackerAction a, int magnitude) {
        if (a == TrackerAction.MOVE_LEFT || a == TrackerAction.MOVE_RIGHT) {
            moveTracker(a, magnitude);
        }
    }

    /**
     * Moves the tracker a given number of steps.
     *
     * @param a direction
     * @param magnitude number of steps to move
     */
    private void moveTracker(TrackerAction a, int magnitude) {
        if (a == TrackerAction.MOVE_LEFT) {
            trackerPosition -= magnitude;
        } else {
            trackerPosition += magnitude;
        }

        // Modulo operation to ensure wraparound
        trackerPosition = (trackerPosition + width) % width;
    }

    /**
     * Returns a set of the horizontal positions currently occupied by an object at the given starting position and of the given size.
     *
     * @param startingPosition x-position of the object
     * @param size the size of the object
     * @return set of x-positions occupied by the object
     */
    public Set<Integer> getCellsOccupiedByObject(int startingPosition, int size) {
        Set<Integer> occupiedCells = new HashSet<>();

        for (int i = 0; i < size; i++) {
            occupiedCells.add((startingPosition + i) % width);
        }

        return occupiedCells;
    }

    /**
     * Spawn a new falling object at the top of the screen.
     */
    private void spawnNewFallingObject() {
        fallingObjectYPosition = height;
        fallingObjectXPosition = 1 + (int) (Math.random()*6); // A random number from 1 to 6
    }

    public boolean[] getShadowSensings(){
        boolean[] sensorReadings = new boolean[trackerWidth];
        return null;
    }
}
