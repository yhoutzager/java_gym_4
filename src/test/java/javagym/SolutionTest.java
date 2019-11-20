package javagym;

import org.junit.jupiter.api.Test;

import noedit.Maze;
import noedit.Position;

public class SolutionTest {

    @Test
    void testSolutionCompletesWithoutErrors() {
        Solution solution = new Solution();
        solution.solve(
                Maze.fromString("# "),
                Position.at(0, 1)
        );
    }
}
