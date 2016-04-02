package project1;

import java.util.ArrayList;

public class Predator extends Boid {

    protected static float maxSpeed = 8;
    protected static float flockingRadius = 10;
    public Predator(double[] position, double speed, BoidWorld world) {
        super(position, speed, world);
    }

    @Override
    protected Vector calculateAlignmentForce(ArrayList<Boid> neighbours) {
        return new Vector(new double[]{0, 0});
    }

    @Override
    protected Vector calculateNoise() {
        return new Vector(new double[]{0, 0});
    }

    @Override
    protected Vector calculatePredatorAvoidance(ArrayList<Predator> predators) {
        return new Vector(new double[]{0, 0});
    }

    protected Vector calculatePredatorSeparation(ArrayList<Predator> predators) {
        Vector sumOfPositions = new Vector(new double[]{0, 0});

        if (predators.size() <= 1){
            return sumOfPositions;
        }

        for (Predator p: predators) {

            if (p == this) {
                continue;
            }
            Vector diff = p.getPosition().minus(this.getPosition());

            int distance = diff.length();
            if (distance < Boid.separationRadius) {
                diff = diff.direction();
                diff = diff.times(separationForceFactor /Math.pow(distance, 2));
                sumOfPositions = sumOfPositions.minus(diff);
            }

        }

        return sumOfPositions;
    }
}
