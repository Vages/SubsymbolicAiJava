package Project1;

/**
 * Created by eirikvageskar on 28.01.2016.
 */
public class Boid {
    private static int idCounter;

    private static float maxSpeed = (float) 20.0;
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
        this.position = this.position.plus(this.velocity);
        this.position = this.position.elementWiseModulo(this.worldDimensions);
    }

    public double[] getPosition() {
        return this.position.getData();
    }
}

