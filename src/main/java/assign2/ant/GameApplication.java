package assign2.ant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameApplication extends Application {
    public static final double CANVAS_WIDTH = 800;
    public static final double CANVAS_HEIGHT = 600;
    public static final double ANT_SIZE = 10;
    private final Random random = new Random();
    private List<Ant> ants = new ArrayList<>();
    private List<double[]> existingElements = new ArrayList<>();
    private double[] homeLocation = new double[4]; // Store the home location
    private long lastUpdateTime;

    @Override
    public void start(Stage stage) {
        // Create the initial input scene
        VBox inputLayout = new VBox(10);
        inputLayout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label questionLabel = new Label("How many ants do you want to start with?");
        TextField inputField = new TextField();
        inputField.setPromptText("1-10");
        inputField.setMaxWidth(50);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            String input = inputField.getText();
            if (input.matches("[1-9]|10")) {
                int numberOfAnts = Integer.parseInt(input);
                startGame(stage, numberOfAnts);
            } else {
                inputField.setText("");
                inputField.setPromptText("Invalid input! Enter 1-10");
            }
        });

        inputLayout.getChildren().addAll(questionLabel, inputField, okButton);
        Scene inputScene = new Scene(inputLayout, CANVAS_WIDTH, CANVAS_HEIGHT);

        stage.setTitle("Ant Game");
        stage.setScene(inputScene);
        stage.show();
    }

    private void startGame(Stage stage, int numberOfAnts) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);

        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Set background color to light brown
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw the ant home
        drawAntHome(gc);

        // Add terrain elements
        addTerrainElements(gc);

        // Spawn ants
        spawnAnts(numberOfAnts);

        lastUpdateTime = System.nanoTime();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                double elapsedSeconds = (now - lastUpdateTime) / 1_000_000_000.0;
                lastUpdateTime = now;
                update(gc, elapsedSeconds);
            }
        }.start();

        root.getChildren().add(canvas);

        stage.setTitle("Ant Game");
        stage.setScene(scene);
        stage.show();
    }

    private void drawAntHome(GraphicsContext gc) {
        double rectWidth = 50;
        double rectHeight = 30;
        double rectX = (CANVAS_WIDTH - rectWidth) / 2;
        double rectY = CANVAS_HEIGHT - rectHeight;

        gc.setFill(Color.RED);
        gc.fillRect(rectX, rectY, rectWidth, rectHeight);
        homeLocation = new double[]{rectX, rectY, rectWidth, rectHeight};
        existingElements.add(new double[]{rectX, rectY, rectWidth, rectHeight, 0});
    }

    private void addTerrainElements(GraphicsContext gc) {
        // Food: brown color, size 30-50
        addElements(gc, Color.BROWN, 30, 50, 2, 6, 1);

        // Water: light blue color, size 30-50
        addElements(gc, Color.LIGHTBLUE, 30, 50, 2, 6, 2);

        // Poison: light green color, size 20-40
        addElements(gc, Color.LIGHTGREEN, 20, 40, 2, 6, 3);
    }

    private void addElements(GraphicsContext gc, Color color, int minSize, int maxSize, int minCount, int maxCount, int type) {
        int count = random.nextInt((maxCount - minCount) + 1) + minCount;

        for (int i = 0; i < count; i++) {
            boolean overlaps;
            double x, y, width, height;

            do {
                overlaps = false;
                width = random.nextInt((maxSize - minSize) + 1) + minSize;
                height = random.nextInt((maxSize - minSize) + 1) + minSize;
                x = random.nextDouble() * (CANVAS_WIDTH - width);
                y = random.nextDouble() * (CANVAS_HEIGHT - height);

                for (double[] element : existingElements) {
                    if (isOverlapping(x, y, width, height, element)) {
                        overlaps = true;
                        break;
                    }
                }
            } while (overlaps);

            existingElements.add(new double[]{x, y, width, height, type});
            gc.setFill(color);
            gc.fillRect(x, y, width, height);
        }
    }

    private boolean isOverlapping(double x, double y, double width, double height, double[] element) {
        double otherX = element[0];
        double otherY = element[1];
        double otherWidth = element[2];
        double otherHeight = element[3];

        return x < otherX + otherWidth && x + width > otherX && y < otherY + otherHeight && y + height > otherY;
    }

    private void spawnAnts(int numberOfAnts) {
        for (int i = 0; i < numberOfAnts; i++) {
            double x, y;
            boolean overlaps;

            do {
                overlaps = false;
                x = random.nextDouble() * (CANVAS_WIDTH - ANT_SIZE);
                y = random.nextDouble() * (CANVAS_HEIGHT - ANT_SIZE);

                for (double[] element : existingElements) {
                    if (isOverlapping(x, y, ANT_SIZE, ANT_SIZE, element)) {
                        overlaps = true;
                        break;
                    }
                }
            } while (overlaps);

            ants.add(new Ant(x, y, this));
        }
    }

    public void born() {
        double x, y;
        boolean overlaps;

        do {
            overlaps = false;
            x = random.nextDouble() * (CANVAS_WIDTH - ANT_SIZE);
            y = random.nextDouble() * (CANVAS_HEIGHT - ANT_SIZE);

            for (double[] element : existingElements) {
                if (isOverlapping(x, y, ANT_SIZE, ANT_SIZE, element)) {
                    overlaps = true;
                    break;
                }
            }
        } while (overlaps);

        ants.add(new Ant(x, y, this));
    }

    public void kill(Ant ant) {
        ants.remove(ant); // remove the ant from the list
    }

    private void update(GraphicsContext gc, double elapsedSeconds) {
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        // Redraw the ant home
        drawAntHome(gc);

        // Redraw terrain elements
        existingElements.forEach(element -> {
            if (element[4] == 0) {
                gc.setFill(Color.RED);
            } else if (element[4] == 1) {
                gc.setFill(Color.BROWN);
            } else if (element[4] == 2) {
                gc.setFill(Color.LIGHTBLUE);
            } else if (element[4] == 3) {
                gc.setFill(Color.LIGHTGREEN);
            }
            gc.fillRect(element[0], element[1], element[2], element[3]);
        });

        // Use an iterator to safely remove ants during iteration
        ants.removeIf(ant -> ant.updateAndCheckKill(elapsedSeconds, existingElements, ants, homeLocation, this));

        // Draw ants
        for (Ant ant : ants) {
            gc.setFill(ant.getColor());
            gc.fillOval(ant.x, ant.y, ANT_SIZE, ANT_SIZE);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
