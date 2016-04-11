package project4;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import project2.AdultSelection;
import project2.MatingSelection;
import project3.*;

import java.util.ArrayList;
import java.util.Set;

public class BeerTrackerGui extends Application {
    private double
            sizeX = 800,
            sizeY = 400,
            refreshRate = 60,
            crossingRateValue = 0.5,
            mutationRateValue = 0.8;

    private BeerTrackerEvolutionWorld world;

    final Label
            generationsLabel = new Label("Generations: "),
            childrenLabel = new Label("Children :"),
            adultsLabel = new Label("Adults :"),
            crossingRateLabel = new Label("Crossing rate:"),
            mutationRateLabel = new Label("Mutation rate:"),
            spfLabel = new Label("Seconds per frame: "),
            numberOfMutationsLabel = new Label("Number of mutations: "),
            crossingRateDisplay = new Label(Double.toString(crossingRateValue)),
            mutationRateDisplay = new Label(Double.toString(mutationRateValue)),
            spfDisplay = new Label(Double.toString(refreshRate / 60));

    final Slider
            crossingRateSlider = new Slider(0, 1, crossingRateValue),
            mutationRateSlider = new Slider(0, 1, mutationRateValue);

    final TextField
            generationsField = new TextField("80"),
            childrenField = new TextField("400"),
            adultsField = new TextField("200"),
            numberOfMutationsField = new TextField("1");

    final Button
            startEvolutionButton = new Button("Start Evolution"),
            halveSpfButton = new Button("Faster"),
            doubleSpfButton = new Button("Slower"),
            startSimulationButton = new Button("Start simulation");

    // Used for simulation
    BeerTrackerIndividual bestIndividual;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene primaryScene = new Scene(root);
        primaryScene.setFill(Color.BLACK);
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("Beer Tracker");

        GridPane grid = new GridPane();
        primaryScene.setRoot(grid);

        Canvas canvas = new Canvas(sizeX, sizeY);

        GridPane.setConstraints(canvas, 0, 0);
        GridPane.setRowSpan(canvas, 7);
        grid.getChildren().add(canvas);

        // Text fields
        int rowNumber = 0;
        GridPane.setConstraints(generationsLabel, 1, rowNumber);
        grid.getChildren().add(generationsLabel);

        GridPane.setConstraints(generationsField, 2, rowNumber);
        grid.getChildren().add(generationsField);

        rowNumber++;

        GridPane.setConstraints(childrenLabel, 1, rowNumber);
        grid.getChildren().add(childrenLabel);

        GridPane.setConstraints(childrenField, 2, rowNumber);
        grid.getChildren().add(childrenField);

        rowNumber++;

        GridPane.setConstraints(adultsLabel, 1, rowNumber);
        grid.getChildren().add(adultsLabel);

        GridPane.setConstraints(adultsField, 2, rowNumber);
        grid.getChildren().add(adultsField);

        rowNumber++;

        // Sliders
        GridPane.setConstraints(crossingRateLabel, 1, rowNumber);
        grid.getChildren().add(crossingRateLabel);

        crossingRateSlider.setMajorTickUnit(0.5);
        crossingRateSlider.setMinorTickCount(4);
        crossingRateSlider.setSnapToTicks(true);
        crossingRateSlider.setShowTickLabels(true);
        crossingRateSlider.setShowTickMarks(true);

        crossingRateSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            crossingRateValue = (double) newValue;
            crossingRateDisplay.setText(String.format("%.1f", crossingRateValue));
        });

        GridPane.setConstraints(crossingRateSlider, 2, rowNumber);
        grid.getChildren().add(crossingRateSlider);

        GridPane.setConstraints(crossingRateDisplay, 3, rowNumber);
        grid.getChildren().add(crossingRateDisplay);

        rowNumber++;

        mutationRateSlider.setMajorTickUnit(0.5);
        mutationRateSlider.setMinorTickCount(4);
        mutationRateSlider.setSnapToTicks(true);
        mutationRateSlider.setShowTickLabels(true);
        mutationRateSlider.setShowTickMarks(true);

        mutationRateSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mutationRateValue = (double) newValue;
            mutationRateDisplay.setText(String.format("%.1f", mutationRateValue));
        });

        GridPane.setConstraints(mutationRateLabel, 1, rowNumber);
        grid.getChildren().add(mutationRateLabel);

        GridPane.setConstraints(mutationRateSlider, 2, rowNumber);
        grid.getChildren().add(mutationRateSlider);

        GridPane.setConstraints(mutationRateDisplay, 3, rowNumber);
        grid.getChildren().add(mutationRateDisplay);

        rowNumber++;

        GridPane.setConstraints(numberOfMutationsLabel, 1, rowNumber);
        grid.getChildren().add(numberOfMutationsLabel);

        GridPane.setConstraints(numberOfMutationsField, 2, rowNumber);
        grid.getChildren().add(numberOfMutationsField);

        rowNumber++;

        startEvolutionButton.setOnAction(event -> startEvolution());

        GridPane.setConstraints(startEvolutionButton, 1, rowNumber);
        GridPane.setColumnSpan(startEvolutionButton, 2);
        grid.getChildren().add(startEvolutionButton);

        rowNumber++;

        // Simulation stuff
        GridPane.setConstraints(spfLabel, 1, rowNumber);
        grid.getChildren().add(spfLabel);

        GridPane.setConstraints(spfDisplay, 2, rowNumber);
        grid.getChildren().add(spfDisplay);

        halveSpfButton.setOnAction(event -> halveSecondsPerFrame());

        GridPane.setConstraints(halveSpfButton, 3, rowNumber);
        grid.getChildren().add(halveSpfButton);

        doubleSpfButton.setOnAction(event -> doubleSecondsPerFrame());

        GridPane.setConstraints(doubleSpfButton, 4, rowNumber);
        grid.getChildren().add(doubleSpfButton);

        startSimulationButton.setOnAction(event -> runSimulation(canvas));

        rowNumber++;

        GridPane.setConstraints(startSimulationButton, 1, rowNumber);
        GridPane.setColumnSpan(startSimulationButton, 2);
        startSimulationButton.setDisable(true);
        grid.getChildren().add(startSimulationButton);

        primaryStage.show();

    }

    private void runSimulation(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        bestIndividual = world.getBestIndividual();

        BeerTrackerGame g = new BeerTrackerGame();

        new AnimationTimer() {
            int framesSkipped = 0;

            @Override
            public void handle(long now) {
                if (framesSkipped >= refreshRate) {
                    drawGame(g, gc);
                    GameEvent lastEvent = bestIndividual.doOneMoveInGame(g);
                    if (lastEvent != GameEvent.NOTHING) {
                        System.out.println(lastEvent);
                    }

                    if (lastEvent == GameEvent.GAME_OVER) {
                        this.stop();
                        startEvolutionButton.setDisable(false);
                    }
                    framesSkipped = 0;
                } else {
                    framesSkipped++;
                }
            }

        }.start();
    }

    private void startEvolution() {
        int noOfGenerations = Integer.parseInt(generationsField.getText());
        int noOfChildren = Integer.parseInt(childrenField.getText());
        int noOfAdults = Integer.parseInt(adultsField.getText());
        int noOfMutations = Integer.parseInt(numberOfMutationsField.getText());

        world = new BeerTrackerEvolutionWorld(
                AdultSelection.GENERATIONAL_MIXING,
                MatingSelection.SIGMA_SCALING,
                noOfChildren,
                noOfAdults,
                noOfGenerations,
                "project4/log",
                crossingRateValue,
                mutationRateValue,
                noOfMutations);

        world.runAllEpochs();

        startSimulationButton.setDisable(false);
        startEvolutionButton.setDisable(true);
    }

    private void halveSecondsPerFrame() {
        refreshRate /= 2;
        if (refreshRate <= 3) {
            halveSpfButton.setDisable(true);
        }
        spfDisplay.setText(Double.toString(refreshRate / 60));
    }

    private void doubleSecondsPerFrame() {
        refreshRate *= 2;
        halveSpfButton.setDisable(false);
        spfDisplay.setText(Double.toString(refreshRate / 60));
    }

    private void drawGame(BeerTrackerGame g, GraphicsContext gc) {
        gc.clearRect(0, 0, sizeX, sizeY);

        double horizontalCellSize = sizeX / 30;
        double verticalCellSize = sizeY / 15;

        gc.setFill(new Color(0.9, 0.9, 0.9, 1));
        gc.fillRect(0, 0, sizeX, sizeY);

        gc.setFill(new Color(0, 0, 0, 1));

        int trackerPos = g.getTrackerPosition();
        int trackerSize = g.getTrackerSize();


        Set<Integer> trackerCells = g.getTrackerCells();

        for (Integer c : trackerCells) {
            gc.fillRect(c*horizontalCellSize, 14*verticalCellSize, horizontalCellSize, horizontalCellSize);
        }

        int objectYPos = g.getFallingObjectYPosition();
        double visualObjectYPos = (15-objectYPos) * verticalCellSize;

        Set<Integer> objectCells = g.getFallingObjectCells();

        if (objectCells.size() > 4) {
            gc.setFill(new Color(1, 0, 0, 0.5));
        } else {
            gc.setFill(new Color(0, 0, 1, 0.5));
        }

        for (Integer c : objectCells) {
            gc.fillRect(c*horizontalCellSize, visualObjectYPos, horizontalCellSize, verticalCellSize);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
