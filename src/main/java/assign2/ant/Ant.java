package assign2.ant;

import java.util.List;

import javafx.scene.paint.Color;

public class Ant {
    double x, y, dx, dy;
    MovementStyle movementStyle;
    Status status;
    GameApplication app;
    double timeInside;
    boolean insideSomething;

    Ant(double x, double y, GameApplication app) {
        this.x = x;
        this.y = y;
        this.dx = Math.random() * 2 - 1;
        this.dy = Math.random() * 2 - 1;
        this.movementStyle = MovementStyle.ROAMING;
        this.status = Status.HUNGRY;
        this.app = app;
        this.timeInside = 0;
        this.insideSomething = false;
    }

    void updateTime(double elapsedSeconds) {
        if (insideSomething) {
            timeInside += elapsedSeconds;
        } else {
            timeInside = 0;
        }
        insideSomething = false;
    }

    boolean shouldBeKilled() {
        return timeInside > 3;
    }

    boolean updateAndCheckKill(double elapsedSeconds, List<double[]> elements, List<Ant> ants, double[] homeLocation, GameApplication app) {
        updateTime(elapsedSeconds);
        if (shouldBeKilled()) {
            return true;
        }
        move(elements, ants, homeLocation);
        return false;
    }

    void move(List<double[]> elements, List<Ant> ants, double[] homeLocation) {
        switch (movementStyle) {
            case ROAMING:
                roam(elements, ants);
                break;
            case RETURNING:
                returnHome(elements, homeLocation);
                break;
        }
    }

    private void roam(List<double[]> elements, List<Ant> ants) {
        x += dx;
        y += dy;

        // Change direction if the ant hits the canvas borders
        if (x < 0 || x > GameApplication.CANVAS_WIDTH - GameApplication.ANT_SIZE) {
            x = Math.max(0, Math.min(x, GameApplication.CANVAS_WIDTH - GameApplication.ANT_SIZE));
            changeDirection();
        }

        if (y < 0 || y > GameApplication.CANVAS_HEIGHT - GameApplication.ANT_SIZE) {
            y = Math.max(0, Math.min(y, GameApplication.CANVAS_HEIGHT - GameApplication.ANT_SIZE));
            changeDirection();
        }

        for (double[] element : elements) {
            if (isOverlapping(x, y, GameApplication.ANT_SIZE, GameApplication.ANT_SIZE, element)) {
                int type = (int) element[4];
                if (type == 1 && status == Status.HUNGRY) { // Contact with food
                    movementStyle = MovementStyle.RETURNING;
                } else if (type == 2 && status == Status.THIRSTY) { // Contact with water
                    status = Status.HUNGRY;
                    changeDirection();
                } else if (type == 3) { // Contact with poison
                    app.kill(this);
                    return;
                }
                insideSomething = true;
                changeDirection();
                break;
            }
        }

        for (Ant ant : ants) {
            if (ant != this && isOverlapping(x, y, GameApplication.ANT_SIZE, GameApplication.ANT_SIZE, new double[]{ant.x, ant.y, GameApplication.ANT_SIZE, GameApplication.ANT_SIZE})) {
                changeDirection();
                break;
            }
        }
    }

    private void returnHome(List<double[]> elements, double[] homeLocation) {
        double targetX = homeLocation[0] + homeLocation[2] / 2 - GameApplication.ANT_SIZE / 2;
        double targetY = homeLocation[1] + homeLocation[3] / 2 - GameApplication.ANT_SIZE / 2;
        double deltaX = targetX - x;
        double deltaY = targetY - y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        dx = (deltaX / distance);
        dy = (deltaY / distance);

        x += dx;
        y += dy;

        // Change direction if the ant hits the canvas borders
        if (x < 0 || x > GameApplication.CANVAS_WIDTH - GameApplication.ANT_SIZE) {
            x = Math.max(0, Math.min(x, GameApplication.CANVAS_WIDTH - GameApplication.ANT_SIZE));
            changeDirection();
        }

        if (y < 0 || y > GameApplication.CANVAS_HEIGHT - GameApplication.ANT_SIZE) {
            y = Math.max(0, Math.min(y, GameApplication.CANVAS_HEIGHT - GameApplication.ANT_SIZE));
            changeDirection();
        }

        for (double[] element : elements) {
            if (isOverlapping(x, y, GameApplication.ANT_SIZE, GameApplication.ANT_SIZE, element)) {
                if ((int) element[4] == 3) { // Contact with poison
                    app.kill(this);
                    return;
                }
                insideSomething = true;
                changeDirection();
                break;
            }
        }

        if (isOverlapping(x, y, GameApplication.ANT_SIZE, GameApplication.ANT_SIZE, homeLocation)) {
            if (status == Status.HUNGRY) {
                status = Status.THIRSTY;
            } else if (status == Status.THIRSTY) {
                status = Status.HUNGRY;
            }
            movementStyle = MovementStyle.ROAMING;
            app.born();
        }
    }

    private void changeDirection() {
        this.dx = Math.random() * 2 - 1;
        this.dy = Math.random() * 2 - 1;
    }

    private boolean isOverlapping(double x, double y, double width, double height, double[] element) {
        double otherX = element[0];
        double otherY = element[1];
        double otherWidth = element[2];
        double otherHeight = element[3];

        return x < otherX + otherWidth && x + width > otherX && y < otherY + otherHeight && y + height > otherY;
    }

    Color getColor() {
        switch (status) {
            case HUNGRY:
                return Color.BROWN;
            case THIRSTY:
                return Color.LIGHTBLUE;
            default:
                return Color.BLACK;
        }
    }
}
