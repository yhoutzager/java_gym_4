package javagym;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Test;

import noedit.Maze;
import noedit.Path;
import noedit.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SolutionTest {

    private void checkMazeSolution(
            @Nonnull Maze maze,
            @Nonnull Position initialPosition
    ) {
        Solution solution = new Solution();
        Path path = solution.solve(maze, initialPosition);
        assertEquals(path.first(), initialPosition,
                "The solution path does not start at the initial position");
        assertTrue(path.isPhysical(),
                "The solution contains impossible moves, like jumping multiple squares, or going back in time.");
        assertEquals(path.last(), initialPosition,
                "The solution does not solve the maze (it does not end at an exit).");
        assertTrue(path.isSolution(maze),
                "The solution is not valid for the maze — it may cross walls or leave the maze area.");
    }

    @Test
    void testSolutionCompletesWithoutErrors() {
        checkMazeSolution(
                Maze.fromString("# "),
                Position.initial(0, 1)
        );
    }
}
