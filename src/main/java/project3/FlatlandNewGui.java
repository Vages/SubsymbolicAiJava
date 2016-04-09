package project3;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import project2.AdultSelection;
import project2.MatingSelection;

import java.util.ArrayList;

public class FlatlandNewGui extends Application {
    private double sizeX = 600;
    private double sizeY = 600;
    private double refreshRate = 60;
    private double crossingRateValue = 0.5,
            mutationRateValue = 0.8;

    private FlatlandEvolutionWorld world;

    final Label generationsLabel = new Label("Generations: "),
            childrenLabel = new Label("Children :"),
            adultsLabel = new Label("Adults :"),
            crossingRateLabel = new Label("Crossing rate:"),
            mutationRateLabel = new Label("Mutation rate:"),
            dynamicLabel = new Label("Dynamic?"),
            spfLabel = new Label("Seconds per frame: ");

    final Label crossingRateDisplay = new Label(Double.toString(crossingRateValue)),
            mutationRateDisplay = new Label(Double.toString(mutationRateValue)),
            spfDisplay = new Label(Double.toString(refreshRate / 60));

    final Slider crossingRateSlider = new Slider(0, 1, crossingRateValue),
            mutationRateSlider = new Slider(0, 1, mutationRateValue);

    final TextField generationsField = new TextField("40"),
            childrenField = new TextField("40"),
            adultsField = new TextField("40");

    final CheckBox dynamicButton = new CheckBox();

    final Button startEvolutionButton = new Button("Start Evolution"),
            halveSpfButton = new Button("/2"),
            doubleSpfButton = new Button("*2"),
            startSimulationButton = new Button("Start simulation"),
            generateNewScenariosButton = new Button("Generate new scenarios");

    // Used for simulation
    int noOfBoards;
    int currentlyExaminedBoard = 0;
    int currentNumberOfSteps = 0;
    int maxSteps = 61;
    FlatlandIndividual bestIndividual;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene primaryScene = new Scene(root);
        primaryScene.setFill(Color.BLACK);
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("Flatland");

        GridPane grid = new GridPane();
        primaryScene.setRoot(grid);

        Canvas canvas = new Canvas(sizeX, sizeY);

        GridPane.setConstraints(canvas, 0, 0);
        GridPane.setRowSpan(canvas, 7);
        grid.getChildren().add(canvas);

        // Text fields
        GridPane.setConstraints(generationsLabel, 1, 0);
        grid.getChildren().add(generationsLabel);

        GridPane.setConstraints(generationsField, 2, 0);
        grid.getChildren().add(generationsField);

        GridPane.setConstraints(childrenLabel, 1, 1);
        grid.getChildren().add(childrenLabel);

        GridPane.setConstraints(childrenField, 2, 1);
        grid.getChildren().add(childrenField);

        GridPane.setConstraints(adultsLabel, 1, 2);
        grid.getChildren().add(adultsLabel);

        GridPane.setConstraints(adultsField, 2, 2);
        grid.getChildren().add(adultsField);

        // Sliders
        GridPane.setConstraints(crossingRateLabel, 1, 3);
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

        GridPane.setConstraints(crossingRateSlider, 2, 3);
        grid.getChildren().add(crossingRateSlider);

        GridPane.setConstraints(crossingRateDisplay, 3, 3);
        grid.getChildren().add(crossingRateDisplay);

        mutationRateSlider.setMajorTickUnit(0.5);
        mutationRateSlider.setMinorTickCount(4);
        mutationRateSlider.setSnapToTicks(true);
        mutationRateSlider.setShowTickLabels(true);
        mutationRateSlider.setShowTickMarks(true);

        mutationRateSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mutationRateValue = (double) newValue;
            mutationRateDisplay.setText(String.format("%.1f", mutationRateValue));
        });

        GridPane.setConstraints(mutationRateLabel, 1, 4);
        grid.getChildren().add(mutationRateLabel);

        GridPane.setConstraints(mutationRateSlider, 2, 4);
        grid.getChildren().add(mutationRateSlider);

        GridPane.setConstraints(mutationRateDisplay, 3, 4);
        grid.getChildren().add(mutationRateDisplay);

        // Toggle Buttons
        GridPane.setConstraints(dynamicLabel, 1, 5);
        grid.getChildren().add(dynamicLabel);

        GridPane.setConstraints(dynamicButton, 2, 5);
        grid.getChildren().add(dynamicButton);

        startEvolutionButton.setOnAction(event -> startEvolution());

        GridPane.setConstraints(startEvolutionButton, 1, 6);
        GridPane.setColumnSpan(startEvolutionButton, 2);
        grid.getChildren().add(startEvolutionButton);

        // Simulation stuff
        GridPane.setConstraints(spfLabel, 1, 7);
        grid.getChildren().add(spfLabel);

        GridPane.setConstraints(spfDisplay, 2, 7);
        grid.getChildren().add(spfDisplay);

        halveSpfButton.setOnAction(event -> halveSecondsPerFrame());

        GridPane.setConstraints(halveSpfButton, 3, 7);
        grid.getChildren().add(halveSpfButton);

        doubleSpfButton.setOnAction(event -> doubleSecondsPerFrame());

        GridPane.setConstraints(doubleSpfButton, 4, 7);
        grid.getChildren().add(doubleSpfButton);

        startSimulationButton.setOnAction(event -> runSimulation(canvas));

        GridPane.setConstraints(startSimulationButton, 1, 8);
        GridPane.setColumnSpan(startSimulationButton, 2);
        startSimulationButton.setDisable(true);
        grid.getChildren().add(startSimulationButton);

        generateNewScenariosButton.setOnAction(event -> world.generateNewScenarios());

        GridPane.setConstraints(generateNewScenariosButton, 3, 8);
        GridPane.setColumnSpan(generateNewScenariosButton, 2);
        generateNewScenariosButton.setDisable(true);
        grid.getChildren().add(generateNewScenariosButton);

        primaryStage.show();

    }

    private void runSimulation(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        bestIndividual = world.getBestIndividual();
        ArrayList<Board> boards = world.getScenarios();
        for (Board b : boards) {
            b.reset();
        }
        noOfBoards = boards.size();
        currentlyExaminedBoard = 0;
        currentNumberOfSteps = 0;
        maxSteps = 61;

        new AnimationTimer() {
            int framesSkipped = 0;

            @Override
            public void handle(long now) {
                if (framesSkipped >= refreshRate) {
                    Board b = boards.get(currentlyExaminedBoard);
                    drawBoard(b, gc);
                    bestIndividual.doOneMoveOnBoard(b);

                    currentNumberOfSteps++;
                    if (currentNumberOfSteps > maxSteps) {
                        currentNumberOfSteps = 0;
                        currentlyExaminedBoard++;
                    }
                    if (currentlyExaminedBoard >= noOfBoards) {
                        this.stop();
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

        FlatlandEvolutionWorld.ScenarioPolicy scenarioPolicy = FlatlandEvolutionWorld.ScenarioPolicy.STATIC;
        if (dynamicButton.isSelected()) {
            scenarioPolicy = FlatlandEvolutionWorld.ScenarioPolicy.DYNAMIC;
        }

        world = new FlatlandEvolutionWorld(
                AdultSelection.GENERATIONAL_MIXING,
                MatingSelection.SIGMA_SCALING,
                noOfChildren,
                noOfAdults,
                noOfGenerations,
                1,
                5,
                0.1,
                "project3/log",
                new int[]{6, 3},
                crossingRateValue,
                mutationRateValue,
                1,
                -2,
                2,
                new double[]{0.33, 0.33},
                new double[]{1, -5},
                5,
                scenarioPolicy
        );

        world.runAllEpochs();

        startSimulationButton.setDisable(false);
        generateNewScenariosButton.setDisable(false);
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

    public void drawBoard(Board b, GraphicsContext gc) {
        gc.clearRect(0, 0, sizeX, sizeY);
        gc.setLineWidth(3);
        gc.setFill(new Color(0.9, 0.9, 0.9, 1));
        gc.fillRect(0, 0, sizeX, sizeY);

        int bs = b.getBoardSize();
        double cellSize = sizeX / bs;

        for (int i = 0; i < bs; i++) {
            for (int j = 0; j < bs; j++) {
                CellType cell = b.getCell(j, i);
                if (cell == CellType.FOOD) {
                    gc.setFill(new Color(0, 1, 0, 1));
                } else if (cell == CellType.POISON) {
                    gc.setFill(new Color(1, 0, 0, 1));
                } else {
                    gc.setFill(new Color(0.9, 0.9, 0.9, 0));
                }

                gc.fillOval(j * cellSize + cellSize / 6, i * cellSize + cellSize / 6, cellSize * 2 / 3, cellSize * 2 / 3);

            }
        }

        int playerX = b.getPlayerX();
        int playerY = b.getPlayerY();
        Heading h = b.getPlayerHeading();

        gc.setFill(new Color(0, 0, 0, 1));
        gc.fillRect(playerX * cellSize + 10, playerY * cellSize + 10, cellSize - 20, cellSize - 20);
        double xCenter = cellSize * (playerX + 0.5);
        double yCenter = cellSize * (playerY + 0.5);
        gc.strokeLine(xCenter, yCenter, xCenter + h.getVectorX() * cellSize / 2, yCenter + h.getVectorY() * cellSize / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
