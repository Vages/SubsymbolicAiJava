package Project1;

import java.util.ArrayList;

/**
 * Created by eirikvageskar on 28.01.2016.
 */
public class Boid {
    private static int idCounter;
    public static double flockingRadius = 50;

    private static float maxSpeed = 10;
    private final Vector worldDimensions;
    private Vector position;
    private Vector velocity;
    private BoidWorld world;
    private int id;

    public static float getMaxSpeed() {
        return maxSpeed;
    }

    public Boid(double[] position, double speed, BoidWorld world){
        this.position = new Vector(position);

        double angle = Math.random() * 2 * Math.PI;
        double[] velocity = {Math.cos(angle) * speed,
                            Math.sin(angle) * speed};
        this.velocity = new Vector(velocity);
        this.world = world;
        this.worldDimensions = new Vector(world.getSize());
        this.id = idCounter++;
    }

    public void update() {
        ArrayList<Boid> neighbours = world.getNeighbours(this);

        Vector align = calculateAlignmentForce(neighbours);
        Vector cohere = calculateCohesionForce(neighbours);

        this.velocity = this.velocity.plus(align).plus(cohere);
        this.position = this.position.plus(this.velocity);
        this.position = this.position.elementWiseModulo(this.worldDimensions);
    }

    private Vector calculateCohesionForce(ArrayList<Boid> neighbours) {
        Vector sumOfPositions = new Vector(new double[]{0, 0});

        if (neighbours.size() == 0){
            return sumOfPositions;
        }

        for (Boid n: neighbours) {
            sumOfPositions = sumOfPositions.plus(n.getPosition());
        }

        sumOfPositions = sumOfPositions.times(1/neighbours.size());

        return sumOfPositions.minus(this.getPosition()).times(0.01); // Must be prettified
    }

    private Vector calculateAlignmentForce(ArrayList<Boid> neighbours) {
        Vector sumOfDirections = new Vector(new double[]{0, 0});

        if (neighbours.size() == 0){
            return sumOfDirections;
        }

        for (Boid n: neighbours) {
            sumOfDirections = sumOfDirections.plus(n.getVelocity().direction());
        }

        Vector force = sumOfDirections.direction().times(maxSpeed);
        Vector steer = force.minus(this.velocity);
        return steer;
    }

    public double[] getPositionAsArray() {
        return this.position.getData();
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }
}

