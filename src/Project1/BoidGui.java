package Project1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

/**
 * Created by eirikvageskar on 29.01.2016.
 */
public class BoidGui extends Application {
    private BoidWorld myBoidWorld;
    private int sizeX, sizeY, initialBoids;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage theStage) {
        myBoidWorld = new BoidWorld(800, 600, 100);

        for (int i = 0; i < 5; i++) {
            myBoidWorld.addObstacle();
        }

        int boidDiameter = 20;

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);

        /*
        Group boids = new Group();
        for (Boid b : myBoidWorld.getBoids()) {
            Circle circle = new Circle(10, Color.web("black", 1.0));
            circle.setCenterX(b.getPositionAsArray()[0]);
            circle.setCenterY(b.getPositionAsArray()[1]);
            boids.getChildren().add(circle);
        }

        root.getChildren().add(boids);
        */

        Canvas canvas = new Canvas(800, 600);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0,0,800,600);
                gc.setLineWidth(5);

                for (Boid b : myBoidWorld.getBoids()){
                    double[] a = b.getPositionAsArray();
                    Vector p = b.getPosition();
                    Vector v = b.getVelocity();
                    Vector d = v.direction();
                    gc.fillOval(p.cartesian(0)-boidDiameter/2, p.cartesian(1)-boidDiameter/2, boidDiameter, boidDiameter);
                    gc.strokeLine(p.cartesian(0), p.cartesian(1), p.cartesian(0)+d.cartesian(0)*15, p.cartesian(1)+d.cartesian(1)*15);
                }


                for (Obstacle o: myBoidWorld.getObstacles()) {
                    Vector p = o.getPosition();
                    double r = o.getRadius();

                    gc.fillOval(p.cartesian(0)-r, p.cartesian(1)-r, r*2, r*2);
                }


                myBoidWorld.update();
            }
        }.start();

        gc.fillOval(300, 300, 20, 20);

        theStage.show();
    }
}
