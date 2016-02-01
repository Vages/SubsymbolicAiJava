package Project1;

import java.util.ArrayList;

public class Predator extends Boid {

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
}
