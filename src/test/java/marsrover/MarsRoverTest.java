package marsrover;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MarsRoverTest {

    @Test
    void turnsLeftThroughFullCircle() {
        MarsRover rover = new MarsRover(0, 0, Direction.N);
        rover.rotate('L');
        assertEquals(Direction.W, rover.getDirection());
        rover.rotate('L');
        assertEquals(Direction.S, rover.getDirection());
        rover.rotate('L');
        assertEquals(Direction.E, rover.getDirection());
        rover.rotate('L');
        assertEquals(Direction.N, rover.getDirection());
    }

    @Test
    void turnsRightThroughFullCircle() {
        MarsRover rover = new MarsRover(0, 0, Direction.N);
        rover.rotate('R');
        assertEquals(Direction.E, rover.getDirection());
        rover.rotate('R');
        assertEquals(Direction.S, rover.getDirection());
        rover.rotate('R');
        assertEquals(Direction.W, rover.getDirection());
        rover.rotate('R');
        assertEquals(Direction.N, rover.getDirection());
    }

    @Test
    void movesInEachDirection() {
        assertMove(Direction.N, 5, 6);
        assertMove(Direction.E, 6, 5);
        assertMove(Direction.S, 5, 4);
        assertMove(Direction.W, 4, 5);
    }

    private void assertMove(Direction direction, int expectedX, int expectedY) {
        MarsRover rover = new MarsRover(5, 5, direction);
        assertTrue(rover.move());
        assertEquals(expectedX, rover.getX());
        assertEquals(expectedY, rover.getY());
    }

    @Test
    void obstacleBlocksMove() {
        MarsRover rover = new MarsRover(1, 1, Direction.N);
        rover.addObstacle(1, 2);
        assertFalse(rover.move());
        assertEquals("(1,1,N)", rover.getPosition());
    }

    @Test
    void boundaryBlocksMove() {
        MarsRover rover = new MarsRover(0, 0, Direction.S);
        assertFalse(rover.move());
        assertEquals("(0,0,S)", rover.getPosition());

        MarsRover edge = new MarsRover(2, 2, Direction.E, 2, 2);
        assertFalse(edge.move());
        assertEquals("(2,2,E)", edge.getPosition());
    }

    @Test
    void executesDemoSequence() {
        MarsRover rover = new MarsRover(0, 0, Direction.N);
        rover.addObstacle(2, 2);
        rover.addObstacle(3, 5);
        rover.executeCommands("MMRMLM");
        assertEquals("(1,3,N)", rover.getPosition());
    }

    @Test
    void acceptsLowercaseCommands() {
        MarsRover rover = new MarsRover(0, 0, Direction.N);
        rover.executeCommands("mmrm");
        assertEquals("(1,2,E)", rover.getPosition());
    }

    @Test
    void rejectsUnknownCommand() {
        MarsRover rover = new MarsRover(0, 0, Direction.N);
        assertThrows(IllegalArgumentException.class, () -> rover.executeCommands("MXM"));
    }

    @Test
    void rejectsInvalidRotation() {
        MarsRover rover = new MarsRover(0, 0, Direction.N);
        assertThrows(IllegalArgumentException.class, () -> rover.rotate('X'));
    }

    @Test
    void rejectsOffGridStartPosition() {
        assertThrows(IllegalArgumentException.class, () -> new MarsRover(-1, 0, Direction.N));
        assertThrows(IllegalArgumentException.class, () -> new MarsRover(5, 5, Direction.N, 4, 4));
    }

    @Test
    void usesConfigurableGridBounds() {
        MarsRover rover = new MarsRover(0, 0, Direction.N, 1, 1);
        assertTrue(rover.move());
        assertFalse(rover.move());
        assertEquals("(0,1,N)", rover.getPosition());
    }
}
