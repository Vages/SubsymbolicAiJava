package project4;

public class BeerTrackerGame {
    private int width;
    private int height;
    private int trackerPosition = 0;
    private int maxTrackerPosition;
    private int trackerWidth;

    public BeerTrackerGame(int width, int height, int trackerWidth) {
        this.width = width;
        this.height = height;
        this.trackerWidth = trackerWidth;
        this.maxTrackerPosition = width-trackerWidth;
    }

    public void performAction(TrackerAction a, int magnitude){
        if (a == TrackerAction.MOVE_LEFT || a == TrackerAction.MOVE_RIGHT) {
            moveTracker(a, magnitude);
        }
    }

    private void moveTracker(TrackerAction a, int magnitude) {
        if (a == TrackerAction.MOVE_LEFT) {
            trackerPosition -= magnitude;
        } else {
            trackerPosition += magnitude;
        }

        // Modulo operation to ensure wraparond
        trackerPosition = (trackerPosition + width) % width;
    }
}
