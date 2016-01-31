package Project1;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by eirikvageskar on 28.01.2016.
 */
public class Boid {
    private static int idCounter;
    public static double flockingRadius = 100;
    public static double separationRadius = 30;
    public static double separationForceFactor = 2;
    public static double cohesionFactor = 0.1;
    public static final double MAX_SEE_AHEAD = 60;
    public static double obstacleAvoidanceForce = 5;

    private static float maxSpeed = 5;
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
        Vector obstacleAvoidance = calculateObstacleAvoidance();

        this.velocity = this.velocity.plus(align);
        this.velocity = this.velocity.plus(cohere);
        this.velocity = this.velocity.plus(separation);
        this.velocity = this.velocity.plus(obstacleAvoidance);
        this.velocity = this.velocity.limit(maxSpeed);

        this.position = this.position.plus(this.velocity);
        this.position = this.position.elementWiseModulo(this.worldDimensions);
    }

    private Vector calculateObstacleAvoidance() {
        double speedRatio = velocity.magnitude()/maxSpeed;
        double aheadLength = speedRatio*MAX_SEE_AHEAD;
        Vector ahead = velocity.direction().times(aheadLength);
        Vector halfAhead = ahead.times(0.5);
        Vector aheadPoint = position.plus(ahead);
        Vector halfAheadPoint = position.plus(halfAhead);

        TreeMap<Double, Obstacle> potentialObstacles = new TreeMap<>();

        for (Obstacle o: world.getObstacles()) {

            double distance = position.distanceTo(o.getPosition());
            double sweepZone = o.getRadius() + aheadLength;
            if (distance <= sweepZone) {
                potentialObstacles.put(distance, o);
            }
        }

        if (potentialObstacles.size() == 0) {
            return new Vector(new double[]{0, 0});
        }

        for (Obstacle o: potentialObstacles.values()) {
            Vector center = o.getPosition();
            double radius = o.getRadius();
            Vector aheadPointToCenter = aheadPoint.minus(center);
            double dist = aheadPointToCenter.magnitude();

            if (dist < radius) {
                return aheadPointToCenter.direction().times(obstacleAvoidanceForce);
            }

            aheadPointToCenter = halfAheadPoint.minus(center);
            dist = aheadPointToCenter.magnitude();
            if (dist < radius) {
                return aheadPointToCenter.direction().times(obstacleAvoidanceForce);
            }

            aheadPointToCenter = position.minus(center);
            dist = aheadPointToCenter.magnitude();

            if (dist < radius) {
                return aheadPointToCenter.direction().times(obstacleAvoidanceForce);
            }
        }

        return new Vector(new double[]{0, 0});
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

