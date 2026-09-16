# Mars Rover

A small Java simulation of a rover navigating a grid-based terrain. The rover accepts
a string of commands, moves forward, turns left/right, refuses to drive into obstacles
or off the grid, and reports its current position and heading.

## Project structure

```
.
├── pom.xml
├── README.md
└── src
    ├── main/java/marsrover
    │   ├── Direction.java     # N/E/S/W enum with turnLeft() / turnRight()
    │   └── MarsRover.java     # rover state, movement, obstacles, command execution
    └── test/java/marsrover
        └── MarsRoverTest.java # JUnit 5 tests
```

## Requirements

- Java 17
- Maven 3.6+

## Build and run

```bash
mvn compile                       # compile sources
mvn test                          # run the unit tests
mvn clean test                    # clean build + tests
mvn exec:java                     # run the demo in main()
java -cp target/classes marsrover.MarsRover   # run the demo without the exec plugin
```

Packaging (`mvn package`) produces `target/mars-rover-1.0.0.jar` with `marsrover.MarsRover`
as the main class, so it can also be run with `java -jar target/mars-rover-1.0.0.jar`.

## Commands

| Command | Meaning                       |
| ------- | ----------------------------- |
| `M`     | Move one step forward         |
| `L`     | Turn 90° left                 |
| `R`     | Turn 90° right                |

Commands are case-insensitive (`"mmrmlm"` works the same as `"MMRMLM"`). An unknown
command character makes `executeCommands` throw `IllegalArgumentException`.

## Grid size

The grid is configurable. The default constructor uses a 10x10 grid (coordinates 0..10
inclusive on both axes); the overloaded constructor takes explicit bounds:

```java
MarsRover rover = new MarsRover(0, 0, Direction.N);          // default 0..10 x 0..10
MarsRover small = new MarsRover(0, 0, Direction.N, 4, 4);    // 0..4 x 0..4
```

A start position outside the grid throws `IllegalArgumentException`.

## Obstacles and blocked moves

Obstacles are registered with `addObstacle(x, y)`. `move()` returns `boolean`: `true`
when the rover moved, `false` when the step was blocked by an obstacle or by the grid
boundary. Blocked moves leave the position and heading unchanged, and
`executeCommands` prints a message such as `Move blocked at (1,1,N)` before continuing
with the remaining commands.

## Example

```java
MarsRover rover = new MarsRover(0, 0, Direction.N);
rover.addObstacle(2, 2);
rover.addObstacle(3, 5);
rover.executeCommands("MMRMLM");
System.out.println("Final position: " + rover.getPosition());
```

Output:

```
Final position: (1,3,N)
```
