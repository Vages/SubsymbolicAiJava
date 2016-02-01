package Project1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class BoidGui extends Application {
    int sizeX = 1024, sizeY = 800, boidDiameter = 10, predatorDiameter = 15;
    private BoidWorld myBoidWorld = new BoidWorld(sizeX, sizeY, 300);

    final Slider cohesionSlider = new Slider(0, 2*Boid.getCohesionFactor(), Boid.getCohesionFactor()),
            alignmentSlider = new Slider(0, 2*Boid.getAlignmentFactor(), Boid.getAlignmentFactor()),
            separationSlider = new Slider(0, 2*Boid.getSeparationForceFactor(), Boid.getSeparationForceFactor());
    final Label cohesionLabel = new Label("Cohesion: "), alignmentLabel = new Label("Alignment: "), separationLabel = new Label("Separation: ");
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage theStage) {
        Group root = new Group();
        Scene theScene = new Scene(root);
        theScene.setFill(Color.BLACK);
        theStage.setScene(theScene);
        theStage.setTitle("Boids");

        GridPane grid = new GridPane();
        theScene.setRoot(grid);

        Canvas canvas = new Canvas(sizeX, sizeY);

        GridPane.setConstraints(canvas, 0, 0);
        GridPane.setRowSpan(canvas, 4);
        grid.getChildren().add(canvas);

        GridPane.setConstraints(cohesionLabel, 1, 0);
        GridPane.setConstraints(cohesionSlider, 2, 0);
        cohesionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Boid.setCohesionFactor(newValue.doubleValue());
        });
        grid.getChildren().add(cohesionLabel);
        grid.getChildren().add(cohesionSlider);

        GridPane.setConstraints(alignmentLabel, 1, 1);
        GridPane.setConstraints(alignmentSlider, 2, 1);
        alignmentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Boid.setAlignmentFactor(newValue.doubleValue());
        });
        grid.getChildren().add(alignmentLabel);
        grid.getChildren().add(alignmentSlider);

        GridPane.setConstraints(separationLabel, 1, 2);
        GridPane.setConstraints(separationSlider, 2, 2);
        separationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Boid.setSeparationForceFactor(newValue.doubleValue());
        });
        grid.getChildren().add(separationLabel);
        grid.getChildren().add(separationSlider);



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

                gc.setFill(new Color(0.9, 0.9, 0.9, 1));
                gc.fillRect(0,0,sizeX,sizeY);

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
