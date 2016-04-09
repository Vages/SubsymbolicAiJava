package project3;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FlatlandNewGui extends Application {
    private double sizeX = 600;
    private double sizeY = 600;
    private int refreshRate = 60;
    private double crossingRateValue = 0.5,
            mutationRateValue = 0.8;

    final Label generationsLabel = new Label("Generations: "),
            childrenLabel = new Label("Children :"),
            adultsLabel = new Label("Adults :"),
            crossingRateLabel = new Label("Crossing rate:"),
            mutationRateLabel = new Label("Mutation rate:"),
            dynamicLabel = new Label("Dynamic?"),
            spfLabel = new Label("Seconds per frame: ");

    final Label crossingRateDisplay = new Label(Double.toString(crossingRateValue)),
            mutationRateDisplay = new Label(Double.toString(mutationRateValue)),
            spfDisplay = new Label(Double.toString((double) refreshRate / 60));

    final Slider crossingRateSlider = new Slider(0, 1, crossingRateValue),
            mutationRateSlider = new Slider(0, 1, mutationRateValue);

    final TextField generationsField = new TextField("40"),
            childrenField = new TextField("40"),
            adultsField = new TextField("40");

    final CheckBox dynamicButton = new CheckBox();

    final Button startEvolutionButton = new Button("Start Evolution"),
            halveSpfButton = new Button("/2"),
            doubleSpfButton = new Button("*2"),
            startSimulationButton = new Button("Start simulation");

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

        GridPane.setConstraints(startEvolutionButton, 1, 6);
        GridPane.setColumnSpan(startEvolutionButton, 2);
        grid.getChildren().add(startEvolutionButton);

        // Simulation stuff
        GridPane.setConstraints(spfLabel, 1, 7);
        grid.getChildren().add(spfLabel);

        GridPane.setConstraints(spfDisplay, 2, 7);
        grid.getChildren().add(spfDisplay);

        halveSpfButton.setOnAction(event -> {
            refreshRate /= 2;
            if (refreshRate <= 15) {
                halveSpfButton.setDisable(true);
            }
            spfDisplay.setText(Double.toString((double) refreshRate / 60));
        });

        GridPane.setConstraints(halveSpfButton, 3, 7);
        grid.getChildren().add(halveSpfButton);

        doubleSpfButton.setOnAction(event -> {
            refreshRate *= 2;
            halveSpfButton.setDisable(false);
            spfDisplay.setText(Double.toString((double) refreshRate / 60));
        });

        GridPane.setConstraints(doubleSpfButton, 4, 7);
        grid.getChildren().add(doubleSpfButton);

        GridPane.setConstraints(startSimulationButton, 1, 8);
        GridPane.setColumnSpan(startSimulationButton, 2);
        startSimulationButton.setDisable(true);
        grid.getChildren().add(startSimulationButton);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
