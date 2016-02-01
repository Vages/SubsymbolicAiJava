package Project1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class BoidGui extends Application {
    int sizeX = 1024, sizeY = 800, boidDiameter = 10, predatorDiameter = 15;
    private BoidWorld myBoidWorld = new BoidWorld(sizeX, sizeY, 200);

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage theStage) {
        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);
        theStage.setTitle("Boids");

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

                drawObstacles(myBoidWorld.getObstacles(), gc);

                gc.setFill(new Color(0,0,0,1));

                drawBoids(myBoidWorld.getBoids(), boidDiameter, gc);
                drawBoids(myBoidWorld.getPredators(), predatorDiameter, gc);

                myBoidWorld.update();
            }
        }.start();

        gc.fillOval(300, 300, 20, 20);

        theStage.show();
    }

    public void drawBoids(ArrayList<? extends Boid> boids, double boidDiameter, GraphicsContext gc) {
        for (Boid b: boids) {
            Vector p = b.getPosition();
            Vector v = b.getVelocity();
            Vector d = v.direction();
            gc.fillOval(p.cartesian(0)-boidDiameter/2, p.cartesian(1)-boidDiameter/2, boidDiameter, boidDiameter);
            gc.strokeLine(p.cartesian(0), p.cartesian(1), p.cartesian(0)-d.cartesian(0)*15, p.cartesian(1)-d.cartesian(1)*15);
        }

    }

    public void drawObstacles(ArrayList<Obstacle> obstacles, GraphicsContext gc) {
        for (Obstacle o: obstacles) {
            Vector p = o.getPosition();
            double r = o.getRadius();

            gc.fillOval(p.cartesian(0)-r, p.cartesian(1)-r, r*2, r*2);
        }
    }
}
