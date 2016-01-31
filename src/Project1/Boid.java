package Project1;

import java.util.ArrayList;

/**
 * Created by eirikvageskar on 28.01.2016.
 */
public class Boid {
    private static int idCounter;
    public static double flockingRadius = 100;
    public static double separationRadius = 30;
    public static double separationForceFactor = 2;
    public static double cohesionFactor = 0.1;

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
        Vector separation = calculateSeparationForce(neighbours);

        this.velocity = this.velocity.plus(align);
        this.velocity = this.velocity.plus(cohere);
        this.velocity = this.velocity.plus(separation);
        this.velocity = this.velocity.limit(maxSpeed);

        this.position = this.position.plus(this.velocity);
        this.position = this.position.elementWiseModulo(this.worldDimensions);
    }

    private Vector calculateSeparationForce(ArrayList<Boid> neighbours) {
        Vector sumOfPositions = new Vector(new double[]{0, 0});

        if (neighbours.size() == 0){
            return sumOfPositions;
        }

        for (Boid n: neighbours) {

            Vector diff = n.getPosition().minus(this.getPosition());

            int distance = diff.length();
            if (distance < Boid.separationRadius) {
                diff = diff.direction();
                diff = diff.times(separationForceFactor /Math.pow(distance, 2));
                sumOfPositions = sumOfPositions.minus(diff);
            }

        }

        return sumOfPositions;
    }

    private Vector calculateCohesionForce(ArrayList<Boid> neighbours) {
        Vector sumOfPositions = new Vector(new double[]{0, 0});

        if (neighbours.size() == 0){
            return sumOfPositions;
        }

        for (Boid n: neighbours) {
            sumOfPositions = sumOfPositions.plus(n.getPosition());
        }

        sumOfPositions = sumOfPositions.times(1.0/neighbours.size());

        Vector d_v = sumOfPositions.minus(this.getPosition());

        return d_v.times(cohesionFactor); // Must be prettified
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

