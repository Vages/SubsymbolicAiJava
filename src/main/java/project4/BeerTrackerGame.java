package project4;

import java.util.HashSet;
import java.util.Set;

public class BeerTrackerGame {
    private final boolean wrappingAllowed;
    private final int width;
    private final int height;
    private int trackerPosition;
    private int trackerSize;
    private int fallingObjectXPosition;
    private int fallingObjectYPosition;
    private int fallingObjectSize;
    private final int maxTrackerPositionWithoutWrap;
    private int timeStepsLeft = 600;

    public BeerTrackerGame(int width, int height, int trackerSize, boolean wrappingAllowed) {
        this.width = width;
        this.height = height;
        this.trackerSize = trackerSize;
        this.wrappingAllowed = wrappingAllowed;
        maxTrackerPositionWithoutWrap = width - trackerSize;
        int spawnPositionRoof = width;
        if (!wrappingAllowed)
            spawnPositionRoof = maxTrackerPositionWithoutWrap + 1;
        this.trackerPosition = (int) (Math.random()*spawnPositionRoof);
        spawnNewFallingObject();
    }

    public BeerTrackerGame() {
        this(30, 15, 5, true);
    }

    /**
     * Performs an action requested by the player
     *
     * @param a         action to be performed
     * @param magnitude the magnitude of the action (used in conjunction with moving left and right)
     */
    public GameEvent performAction(TrackerAction a, int magnitude) {
        if (timeStepsLeft == 0) {
            return GameEvent.GAME_OVER;
        }
        if (a == TrackerAction.MOVE_LEFT || a == TrackerAction.MOVE_RIGHT) {
            moveTracker(a, magnitude);
        }

        timeStepsLeft--;

        return moveFallingObjectOneStepAndCheckForCaptures();
    }

    private GameEvent moveFallingObjectOneStepAndCheckForCaptures() {
        fallingObjectYPosition--;

        if (fallingObjectYPosition == 0) {
            GameEvent captureResults = getCaptureResults();
            spawnNewFallingObject();
            return captureResults;
        }

        return GameEvent.NOTHING;
    }

    private GameEvent getCaptureResults() {
        Set<Integer> trackerCells = getCellsOccupiedByObject(trackerPosition, trackerSize);
        Set<Integer> objectCells = getCellsOccupiedByObject(fallingObjectXPosition, fallingObjectSize);
        boolean isBig = fallingObjectSize > 4;
        if (!isBig) {
            if (trackerCells.containsAll(objectCells)) {
                return GameEvent.CAPTURED_SMALL;
            } else {
                trackerCells.retainAll(objectCells);
                if (trackerCells.size() == 0) {
                    return GameEvent.AVOIDED_SMALL;
                } else {
                    return GameEvent.PARTIALLY_CAPTURED_SMALL;
                }
            }
        } else {
            if (objectCells.containsAll(trackerCells)) {
                return GameEvent.CAPTURED_BIG;
            } else {
                trackerCells.retainAll(objectCells);
                if (trackerCells.size() == 0) {
                    return GameEvent.AVOIDED_BIG;
                } else {
                    return GameEvent.PARTIALLY_CAPTURED_BIG;
                }
            }
        }
    }

    /**
     * Moves the tracker a given number of steps.
     *
     * @param a         direction
     * @param magnitude number of steps to move
     */
    private void moveTracker(TrackerAction a, int magnitude) {
        if (wrappingAllowed) {
            if (a == TrackerAction.MOVE_LEFT) {
                trackerPosition -= magnitude;
            } else {
                trackerPosition += magnitude;
            }

            // Modulo operation to ensure wraparound
            trackerPosition = (trackerPosition + width) % width;
        } else {
            if (a == TrackerAction.MOVE_LEFT) {
                trackerPosition -= magnitude;
                if (trackerPosition < 0) {
                    trackerPosition = 0;
                }
            } else {
                trackerPosition += magnitude;
                if (trackerPosition > maxTrackerPositionWithoutWrap) {
                    trackerPosition = maxTrackerPositionWithoutWrap;
                }
            }
        }
    }

    /**
     * Returns a set of the horizontal positions currently occupied by an object at the given starting position and of the given size.
     *
     * @param startingPosition x-position of the object
     * @param size             the size of the object
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
        fallingObjectSize = 1 + (int) (Math.random() * 6); // A random number from 1 to 6
        if (wrappingAllowed) {
            fallingObjectXPosition = (int) (Math.random() * width);
        } else {
            fallingObjectXPosition = (int) (Math.random() * (width - fallingObjectSize));
        }
    }

    /**
     * Returns an array of the readings of the shadow sensors of the tracker.
     *
     * @return an array of the shadow sensor readings
     */
    public boolean[] getShadowSensings() {
        boolean[] sensorReadings = new boolean[trackerSize];

        Set<Integer> fallingObjectCells = getCellsOccupiedByObject(fallingObjectXPosition, fallingObjectSize);

        for (int i = 0; i < trackerSize; i++) {
            int examinedCell = (trackerPosition + i) % width;
            sensorReadings[i] = fallingObjectCells.contains(examinedCell);
        }

        return sensorReadings;
    }

    /**
     * Returns an array indicating whether the tracker touches the edges of the board.
     *
     * @return array: first index indicates left edge, second index indicates the right edge.
     */
    public boolean[] getEdgeTouchSensings() {
        boolean[] readings = new boolean[2];

        if (trackerPosition == 0) readings[0] = true;
        if (trackerPosition == maxTrackerPositionWithoutWrap) readings[1] = true;

        return readings;
    }

    public int getFallingObjectSize() {
        return fallingObjectSize;
    }

    public int getFallingObjectYPosition() {
        return fallingObjectYPosition;
    }

    public int getFallingObjectXPosition() {
        return fallingObjectXPosition;
    }

    public int getTrackerPosition() {
        return trackerPosition;
    }

    public int getTrackerSize() {
        return trackerSize;
    }

    public Set<Integer> getTrackerCells() {
        return getCellsOccupiedByObject(trackerPosition, trackerSize);
    }

    public Set<Integer> getFallingObjectCells() {
        return getCellsOccupiedByObject(fallingObjectXPosition, fallingObjectSize);
    }
}
