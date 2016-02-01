package Project1;

public class Obstacle {
    private Vector position;
    private double radius;

    public Obstacle(double x, double y, double radius) {
        this.position = new Vector(new double[]{x, y});
        this.radius = radius;
    }

    public Vector getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }
}
