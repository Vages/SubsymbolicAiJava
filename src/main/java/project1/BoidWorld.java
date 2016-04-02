package project1;

import java.util.ArrayList;

public class BoidWorld {
    private int sizeX;
    private int sizeY;
    private ArrayList<Boid> boids;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Predator> predators;
    int defaultObstacleRadius = 40;

    public BoidWorld(int sizeX, int sizeY, int initialBoids) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.boids = new ArrayList<>();
        this.predators = new ArrayList<>();
        this.obstacles = new ArrayList<>();
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
        predators.forEach(Predator::update);
    }

    public double[] getSize() {
        return new double[]{sizeX, sizeY};
    }

    public ArrayList<Boid> getBoids() {
        return boids;
    }

    public ArrayList<Boid> getNeighbours(Boid me) {
        ArrayList<Boid> neighbours = new ArrayList<>();
        double[] myPosition = me.getPositionAsArray();

        for (Boid other: this.boids) {
            if (other == me) {
                continue;
            }

            double[] otherPosition = other.getPositionAsArray();
            double distanceX = otherPosition[0]-myPosition[0];
            double distanceY = otherPosition[1]-myPosition[1];

            if (Math.hypot(distanceX, distanceY) < Boid.flockingRadius) {
                neighbours.add(other);
            }
        }

        return neighbours;

    }

    public void addObstacle() {
        obstacles.add(new Obstacle(Math.random()*sizeX, Math.random()*sizeY, defaultObstacleRadius));
    }

    public ArrayList<Obstacle> getObstacles() {
        return this.obstacles;
    }

    public void addObstacle(double x, double y) {
        obstacles.add(new Obstacle(x, y, defaultObstacleRadius));
    }

    public void addPredator() {
        double x = Math.random() * this.sizeX;
        double y = Math.random() * this.sizeY;
        Predator p = new Predator(new double[]{x, y}, Boid.getMaxSpeed(), this);
        predators.add(p);
    }

    public ArrayList<Predator> getPredators() {
        return predators;
    }

    public void addPredator(double x, double y) {
        Predator p = new Predator(new double[]{x, y}, Boid.getMaxSpeed(), this);
        predators.add(p);
    }

    public void killAllPredators(){
        predators.clear();
    }

    public void removeAllObstacles() {
        obstacles.clear();
    }
}
