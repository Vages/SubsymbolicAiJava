package project1;

import java.util.ArrayList;
import java.util.TreeMap;

public class Boid {
    public static final double MAX_SEE_AHEAD = 80;
    public static double alignmentFactor = 5;
    public static double separationForceFactor = 2;
    public static double cohesionFactor = 0.1;
    public static double flockingRadius = 100;
    public static double separationRadius = 30;
    public static double obstacleAvoidanceForce = 15;
    public static double maxNoiseForce = 1;
    public static double predatorAvoidanceRadius = 50;
    public static double predatorFleeForce = 10;
    protected static float maxSpeed = 5;
    private static int idCounter;
    private final Vector worldDimensions;
    private Vector position;
    private Vector velocity;
    private BoidWorld world;
    private int id;

    public Boid(double[] position, double speed, BoidWorld world) {
        this.position = new Vector(position);

        double angle = Math.random() * 2 * Math.PI;
        double[] velocity = {Math.cos(angle) * speed,
                Math.sin(angle) * speed};
        this.velocity = new Vector(velocity);
        this.world = world;
        this.worldDimensions = new Vector(world.getSize());
        this.id = idCounter++;
    }

    public static float getMaxSpeed() {
        return maxSpeed;
    }

    public static void setCohesionFactor(double cohesionFactor) {
        Boid.cohesionFactor = cohesionFactor;
    }

    public static void setAlignmentFactor(double alignmentFactor) {
        Boid.alignmentFactor = alignmentFactor;
    }

    public static void setSeparationForceFactor(double separationForceFactor) {
        Boid.separationForceFactor = separationForceFactor;
    }

    public void update() {
        ArrayList<Boid> neighbours = world.getNeighbours(this);
        ArrayList<Predator> predators = world.getPredators();

        Vector align = calculateAlignmentForce(neighbours);
        Vector cohere = calculateCohesionForce(neighbours);
        Vector separation = calculateSeparationForce(neighbours);
        Vector obstacleAvoidance = calculateObstacleAvoidance();
        Vector predatorAvoidance = calculatePredatorAvoidance(predators);
        Vector predatorSeparation = calculatePredatorSeparation(predators);
        Vector noise = calculateNoise();

        this.velocity = this.velocity.plus(align);
        this.velocity = this.velocity.plus(cohere);
        this.velocity = this.velocity.plus(separation);
        this.velocity = this.velocity.plus(obstacleAvoidance);
        this.velocity = this.velocity.plus(predatorAvoidance);
        this.velocity = this.velocity.plus(predatorSeparation);
        this.velocity = this.velocity.plus(noise);
        this.velocity = this.velocity.limit(this.maxSpeed);

        this.position = this.position.plus(this.velocity);
        this.position = this.position.elementWiseModulo(this.worldDimensions);
    }

    protected Vector calculatePredatorAvoidance(ArrayList<Predator> predators) {
        Vector sumOfDirections = new Vector(new double[]{0, 0});

        if (predators.size() == 0) {
            return sumOfDirections;
        }

        for (Predator p : predators) {
            Vector diff = this.position.minus(p.getPosition());
            if (diff.magnitude() < Boid.predatorAvoidanceRadius) {
                sumOfDirections = sumOfDirections.plus(diff.direction());
            }
        }

        if (sumOfDirections.magnitude() > 0) {
            return sumOfDirections.direction().times(predatorFleeForce);
        }

        return sumOfDirections;
    }

    protected Vector calculateNoise() {
        double angle = Math.random() * 2 * Math.PI;
        double force = Math.random() * maxNoiseForce;
        return new Vector(new double[]{Math.cos(angle) * force, Math.sin(angle) * force});
    }

    protected Vector calculateObstacleAvoidance() {
        double speedRatio = velocity.magnitude() / maxSpeed;
        double aheadLength = speedRatio * MAX_SEE_AHEAD;
        Vector ahead = velocity.direction().times(aheadLength);
        Vector halfAhead = ahead.times(0.5);
        Vector aheadPoint = position.plus(ahead);
        Vector halfAheadPoint = position.plus(halfAhead);

        TreeMap<Double, Obstacle> potentialObstacles = new TreeMap<>();

        for (Obstacle o : world.getObstacles()) {

            double distance = position.distanceTo(o.getPosition());
            double sweepZone = o.getRadius() + aheadLength;
            if (distance <= sweepZone) {
                potentialObstacles.put(distance, o);
            }
        }

        if (potentialObstacles.size() == 0) {
            return new Vector(new double[]{0, 0});
        }

        for (Obstacle o : potentialObstacles.values()) {
            Vector center = o.getPosition();
            double radius = o.getRadius();
            Vector aheadPointToCenter = aheadPoint.minus(center);
            double dist = aheadPointToCenter.magnitude();

            if (dist < radius) {
                Vector heading = center.minus(this.position);
                Vector turnedHeading = new Vector(new double[]{-heading.cartesian(1), heading.cartesian(0)});
                if (turnedHeading.dot(aheadPointToCenter) < 0){
                    turnedHeading = turnedHeading.times(-1);
                }
                return turnedHeading.direction().times(obstacleAvoidanceForce);
            }

            aheadPointToCenter = halfAheadPoint.minus(center);
            dist = aheadPointToCenter.magnitude();
            if (dist < radius) {
                Vector heading = center.minus(this.position);
                Vector turnedHeading = new Vector(new double[]{-heading.cartesian(1), heading.cartesian(0)});
                if (turnedHeading.dot(aheadPointToCenter) < 0){
                    turnedHeading = turnedHeading.times(-1);
                }
                return turnedHeading.direction().times(obstacleAvoidanceForce);
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

        if (neighbours.size() == 0) {
            return sumOfPositions;
        }

        for (Boid n : neighbours) {

            Vector diff = n.getPosition().minus(this.getPosition());

            int distance = diff.length();
            if (distance < Boid.separationRadius) {
                diff = diff.direction();
                diff = diff.times(separationForceFactor / Math.pow(distance, 2));
                sumOfPositions = sumOfPositions.minus(diff);
            }

        }

        return sumOfPositions;
    }

    protected Vector calculateCohesionForce(ArrayList<Boid> neighbours) {
        Vector sumOfPositions = new Vector(new double[]{0, 0});

        if (neighbours.size() == 0) {
            return sumOfPositions;
        }

        for (Boid n : neighbours) {
            sumOfPositions = sumOfPositions.plus(n.getPosition());
        }

        sumOfPositions = sumOfPositions.times(1.0 / neighbours.size());

        Vector d_v = sumOfPositions.minus(this.getPosition());

        return d_v.times(cohesionFactor); // Must be prettified
    }

    protected Vector calculateAlignmentForce(ArrayList<Boid> neighbours) {
        Vector sumOfDirections = new Vector(new double[]{0, 0});

        if (neighbours.size() == 0) {
            return sumOfDirections;
        }

        for (Boid n : neighbours) {
            sumOfDirections = sumOfDirections.plus(n.getVelocity().direction());
        }

        Vector force = sumOfDirections.direction().times(alignmentFactor);
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

    protected Vector calculatePredatorSeparation(ArrayList<Predator> predators) {
        return new Vector(new double[]{0, 0});
    }

    public static double getSeparationForceFactor() {
        return separationForceFactor;
    }

    public static double getCohesionFactor() {
        return cohesionFactor;
    }

    public static double getAlignmentFactor() {
        return alignmentFactor;
    }

    public static double getMaxNoiseForce() {
        return maxNoiseForce;
    }

    public static void setMaxNoiseForce(double maxNoiseForce) {
        Boid.maxNoiseForce = maxNoiseForce;
    }
}

