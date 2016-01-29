package Project1;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by eirikvageskar on 28.01.2016.
 */
public class BoidWorld {
    private int sizeX;
    private int sizeY;
    private ArrayList<Boid> boids;

    public BoidWorld(int sizeX, int sizeY, int initialBoids) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.boids = new ArrayList<>();
        for (int i = 0; i < initialBoids; i++) {
            this.addBoid();
        }
    }

    public Boid addBoid() {
        double x = Math.random() * this.sizeX;
        double y = Math.random() * this.sizeY;
        Boid b = new Boid(new double[]{x, y}, Boid.getMaxSpeed(), this);
        this.boids.add(b);
        return b;
    }

    public void removeBoid() {
        int size = this.boids.size();
        if (size > 0) {
            this.boids.remove(size - 1);
        }
    }

    public void update() {
        boids.forEach(Boid::update);
    }

    public double[] getSize() {
        return new double[]{sizeX, sizeY};
    }

    public ArrayList<Boid> getBoids() {
        return boids;
    }

    public ArrayList<Boid> getNeighbours(Boid me) {
        ArrayList<Boid> neighbours = new ArrayList<>();
        double[] myPosition = me.getPosition();

        for (Boid other: this.boids) {
            if (other == me) {
                continue;
            }

            double[] otherPosition = other.getPosition();
            double distanceX = otherPosition[0]-myPosition[0];
            double distanceY = otherPosition[1]-myPosition[1];

            if (Math.hypot(distanceX, distanceY) < Boid.flockingRadius) {
                neighbours.add(other);
            }
        }

        return neighbours;

    }
}
