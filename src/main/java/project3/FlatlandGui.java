package project3;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;

public class FlatlandGui extends Application {
    public static final CountDownLatch latch = new CountDownLatch(1);
    public static FlatlandGui startUpTest = null;

    private double sizeX = 600;
    private double sizeY = 600;

    GraphicsContext gc;

    public static FlatlandGui waitForStartUpTest() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return startUpTest;
    }

    public static void setStartUpTest(FlatlandGui startUpTest0) {
        startUpTest = startUpTest0;
        latch.countDown();
    }

    public FlatlandGui() {
        setStartUpTest(this);
    }

    public void printSomething() {
        System.out.println("You called a method on the application");
    }


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

        GridPane.setConstraints(canvas,0,0);
        grid.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();

        primaryStage.show();

    }

    public void drawBoard(Board b) {
        gc.clearRect(0,0,sizeX,sizeY);
        gc.setLineWidth(3);
        gc.setFill(new Color(0.9, 0.9, 0.9, 1));
        gc.fillRect(0, 0, sizeX, sizeY);
    }

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(FlatlandGui.class);
            }
        }.start();
        FlatlandGui startUpTest = FlatlandGui.waitForStartUpTest();
        startUpTest.drawBoard(new Board(0.33, 0.33, new int[]{0,0}));
    }
}
