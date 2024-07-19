package assign2.ant;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class HelloApplication extends Application {

    private static final Color BACKGROUND_COLOR = Color.BEIGE;
    private static final Color FOOD_COLOR = Color.BROWN;
    private static final Color WATER_COLOR = Color.LIGHTBLUE;
    private static final Color POISON_COLOR = Color.LIGHTGREEN;
    private static final int FOOD_SIZE_MIN = 20;
    private static final int FOOD_SIZE_MAX = 40;
    private static final int WATER_SIZE_MIN = 20;
    private static final int WATER_SIZE_MAX = 40;
    private static final int POISON_SIZE_MIN = 10;
    private static final int POISON_SIZE_MAX = 30;
    private static final int TERRAIN_COUNT_MIN = 1;
    private static final int TERRAIN_COUNT_MAX = 5;

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 800, 600); // Set your desired window size here

        // Create a canvas to draw on
        Canvas canvas = new Canvas(scene.getWidth(), scene.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Set background color to light brown
        gc.setFill(BACKGROUND_COLOR);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw terrain elements
        Random random = new Random();
        drawTerrain(gc, random, FOOD_COLOR, FOOD_SIZE_MIN, FOOD_SIZE_MAX, TERRAIN_COUNT_MIN, TERRAIN_COUNT_MAX, canvas);
        drawTerrain(gc, random, WATER_COLOR, WATER_SIZE_MIN, WATER_SIZE_MAX, TERRAIN_COUNT_MIN, TERRAIN_COUNT_MAX, canvas);
        drawTerrain(gc, random, POISON_COLOR, POISON_SIZE_MIN, POISON_SIZE_MAX, TERRAIN_COUNT_MIN, TERRAIN_COUNT_MAX, canvas);

        // Draw a small rectangle at the middle bottom of the map (ant home)
        double rectWidth = 50;  // Width of the rectangle
        double rectHeight = 30; // Height of the rectangle
        double rectX = (canvas.getWidth() - rectWidth) / 2;
        double rectY = canvas.getHeight() - rectHeight;

        gc.setFill(Color.BROWN);
        gc.fillRect(rectX, rectY, rectWidth, rectHeight);

        root.getChildren().add(canvas);

        stage.setTitle("Ant Game");
        stage.setScene(scene);
        stage.show();
    }

    private void drawTerrain(GraphicsContext gc, Random random, Color color, int sizeMin, int sizeMax, int countMin, int countMax, Canvas canvas) {
        int count = random.nextInt(countMax - countMin + 1) + countMin;
        gc.setFill(color);

        for (int i = 0; i < count; i++) {
            int width = random.nextInt(sizeMax - sizeMin + 1) + sizeMin;
            int height = random.nextInt(sizeMax - sizeMin + 1) + sizeMin;
            double x = random.nextDouble() * (canvas.getWidth() - width);
            double y = random.nextDouble() * (canvas.getHeight() - height);
            gc.fillRect(x, y, width, height);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
