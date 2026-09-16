package marsrover;

import java.util.HashSet;
import java.util.Set;

public class MarsRover {

    private static final int DEFAULT_MAX_X = 10;
    private static final int DEFAULT_MAX_Y = 10;

    private int x;
    private int y;
    private Direction direction;
    private final int maxX;
    private final int maxY;
    private final Set<String> obstacles;

    public MarsRover(int x, int y, Direction direction) {
        this(x, y, direction, DEFAULT_MAX_X, DEFAULT_MAX_Y);
    }

    public MarsRover(int x, int y, Direction direction, int maxX, int maxY) {
        if (direction == null) {
            throw new IllegalArgumentException("Direction must not be null");
        }
        if (maxX < 0 || maxY < 0) {
            throw new IllegalArgumentException("Grid bounds must not be negative");
        }
        this.maxX = maxX;
        this.maxY = maxY;
        if (!isValidPosition(x, y)) {
            throw new IllegalArgumentException(
                    "Start position (" + x + "," + y + ") is outside the grid 0.." + maxX + " x 0.." + maxY);
        }
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.obstacles = new HashSet<>();
    }

    public void rotate(char rotation) {
        switch (Character.toUpperCase(rotation)) {
            case 'L':
                direction = direction.turnLeft();
                break;
            case 'R':
                direction = direction.turnRight();
                break;
            default:
                throw new IllegalArgumentException("Invalid rotation command: " + rotation);
        }
    }

    public boolean move() {
        int nextX = x;
        int nextY = y;
        switch (direction) {
            case N:
                nextY++;
                break;
            case E:
                nextX++;
                break;
            case S:
                nextY--;
                break;
            case W:
                nextX--;
                break;
        }
        if (obstacles.contains(key(nextX, nextY)) || !isValidPosition(nextX, nextY)) {
            return false;
        }
        x = nextX;
        y = nextY;
        return true;
    }

    public void executeCommands(String commands) {
        if (commands == null) {
            throw new IllegalArgumentException("Commands must not be null");
        }
        for (char command : commands.toCharArray()) {
            char normalized = Character.toUpperCase(command);
            switch (normalized) {
                case 'L':
                case 'R':
                    rotate(normalized);
                    break;
                case 'M':
                    if (!move()) {
                        System.out.println("Move blocked at " + getPosition());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown command: " + command);
            }
        }
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x <= maxX && y >= 0 && y <= maxY;
    }

    public void addObstacle(int x, int y) {
        obstacles.add(key(x, y));
    }

    private static String key(int x, int y) {
        return x + ":" + y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getPosition() {
        return "(" + x + "," + y + "," + direction + ")";
    }

    public static void main(String[] args) {
        MarsRover rover = new MarsRover(0, 0, Direction.N);
        rover.addObstacle(2, 2);
        rover.addObstacle(3, 5);
        rover.executeCommands("MMRMLM");
        System.out.println("Final position: " + rover.getPosition());
    }
}
