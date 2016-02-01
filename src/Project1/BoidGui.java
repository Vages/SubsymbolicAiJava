package Project1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by eirikvageskar on 29.01.2016.
 */
public class BoidGui extends Application {
    private BoidWorld myBoidWorld;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage theStage) {
        int sizeX = 1024, sizeY = 800;
        myBoidWorld = new BoidWorld(sizeX, sizeY, 100);


        int boidDiameter = 10, predatorDiameter = 15;

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);

        Canvas canvas = new Canvas(sizeX, sizeY);
        root.getChildren().add(canvas);

        canvas.setOnMouseClicked(
                event-> {
                    if (event.getButton() == MouseButton.PRIMARY){
                        myBoidWorld.addObstacle(event.getX(), event.getY());
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        myBoidWorld.addPredator(event.getX(), event.getY());
                    }
                }
        );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0,0,sizeX,sizeY);
                gc.setLineWidth(3);

                gc.setFill(new Color(0.5, 0.5, 0.5, 1));

                for (Obstacle o: myBoidWorld.getObstacles()) {
                    Vector p = o.getPosition();
                    double r = o.getRadius();

                    gc.fillOval(p.cartesian(0)-r, p.cartesian(1)-r, r*2, r*2);
                }

                gc.setFill(new Color(0,0,0,1));

                for (Boid b : myBoidWorld.getBoids()){
                    Vector p = b.getPosition();
                    Vector v = b.getVelocity();
                    Vector d = v.direction();
                    gc.fillOval(p.cartesian(0)-boidDiameter/2, p.cartesian(1)-boidDiameter/2, boidDiameter, boidDiameter);
                    gc.strokeLine(p.cartesian(0), p.cartesian(1), p.cartesian(0)-d.cartesian(0)*15, p.cartesian(1)-d.cartesian(1)*15);
                }

                for (Predator pr : myBoidWorld.getPredators()){
                    Vector p = pr.getPosition();
                    Vector v = pr.getVelocity();
                    Vector d = v.direction();
                    gc.fillOval(p.cartesian(0)-predatorDiameter/2, p.cartesian(1)-predatorDiameter/2, predatorDiameter, predatorDiameter);
                    gc.strokeLine(p.cartesian(0), p.cartesian(1), p.cartesian(0)-d.cartesian(0)*15, p.cartesian(1)-d.cartesian(1)*15);
                }

                myBoidWorld.update();
            }
        }.start();

        gc.fillOval(300, 300, 20, 20);

        theStage.show();
    }
}
