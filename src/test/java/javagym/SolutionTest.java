package javagym;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import noedit.Cell;
import noedit.Maze;
import noedit.MazeGenerator;
import noedit.Path;
import noedit.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SolutionTest {

    private double checkMazeSolution(
            @Nonnull Maze maze,
            @Nonnull Position initialPosition
    ) {
        Solution solution = new Solution();
        long t0 = System.nanoTime();
        Path path = solution.solve(maze, initialPosition);
        long duration = System.nanoTime() - t0;
        assertEquals(path.first(), initialPosition,
                "The solution path does not start at the initial position");
        assertTrue(path.isPhysical(),
                "The solution contains impossible moves, like jumping multiple squares, or going back in time.");
        assertEquals(Cell.Exit, maze.get(path.last()),
                "The solution does not solve the maze (it does not end at an exit).");
        assertTrue(path.isSolution(maze),
                "The solution is not valid for the maze — it may cross walls or leave the maze area.");
        return duration / 1e6;
    }

    @Test
    void testCompletesWithoutErrors() {
        // Hint: Just step to the left.
        checkMazeSolution(
                Maze.fromString("# "),
                Position.initial(1, 0)
        );
    }

    @Test
    void testPerformance() {
        throw new AssertionError("There will be a performance test later on! Don't assume it won't be tested! (you can delete this placeholder if you want)");
    }

    @Test
    void testLongCorridor() {
        // Hint: Just keep walking right.
        checkMazeSolution(
                Maze.fromString("                          #"),
                Position.initial(0, 0)
        );
    }

    @Test
    void testSmallConstant() {
        checkMazeSolution(
                Maze.fromString(
                        "   \n" +
                        " * \n" +
                        " * \n" +
                        "#* \n"
                ),
                Position.initial(2, 3)
        );
    }

    @Test
    void testSmallTwoSteps() {
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        "*  \n" +
                                "** \n" +
                                "** \n" +
                                "#* \n",
                        // next step
                        "  *\n" +
                                " **\n" +
                                " **\n" +
                                "#**\n",
                }),
                Position.initial(2, 3)
        );
    }

    @Test
    void testStaticRoom() {
        // Hint: Walk to the exit, just a few small obstacles.
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        " *                        \n" +
                        "                 *        \n" +
                        "                          \n" +
                        "                       *  \n" +
                        "       *                  \n" +
                        "               *          \n" +
                        "       *                 #",
                }),
                Position.initial(0, 0)
        );
    }

    @Test
    void testLongWayToExit() {
        // Hint: Walk to the exit, just a few small obstacles.
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        " *   *             \n" +
                        " * * * *********** \n" +
                        " * * * *           \n" +
                        "   * * * **********\n" +
                        " * * * *           \n" +
                        " * *   *********** \n" +
                        " * *   *#          ",
                }),
                Position.initial(0, 0)
        );
    }

    @Test
    void testSomeExitsUnreachable() {
        // Hint: Walk to the exit, just a few small obstacles.
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        "                     \n" +
                        "**** * * * * * * * **\n" +
                        "** *** * * * * * *** \n" +
                        "** * *** * * * *** * \n" +
                        "** * * *** * *** * * \n" +
                        "** * * * * *** * * * \n" +
                        "#*#*#*#*#*#*#*#*#*#*#",
                }),
                Position.initial(0, 0)
        );
    }

    @Test
    void testNearbyExitsUnreachable() {
        // Hint: Walk to the exit, just a few small obstacles.
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        " *# #*   *   * \n" +
                        " ***** * * * * \n" +
                        " *   * * * * * \n" +
                        " * * * * * * * \n" +
                        " * * * * * * * \n" +
                        "   * * * * * *#\n" +
                        "**** * * * * * \n" +
                        "  #*   *   *   ",
                }),
                Position.initial(0, 0)
        );
    }

    @Test
    void testStandaloneExit() {
        // Hint: Don't just follow the walls.
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        "     \n" +
                        "     \n" +
                        "  #  \n" +
                        "     \n" +
                        "     "
                }),
                Position.initial(0, 0)
        );
    }

    @Test
    void testExitAppearsLater() {
        // Hint: Just stand still until the exit appears under you.
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        " ",
                        " ",
                        " ",
                        " ",
                        " ",
                        " ",
                        " ",
                        "#",
                }),
                Position.initial(0, 0)
        );
    }

    @Test
    void testSlidingCorridor() {
        // Hint: Take one step per turn, and don't try to escape the sliding window.
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        "  *                       #",
                        "*  *                      #",
                        " *  *                     #",
                        "  *  *                    #",
                        "   *  *                   #",
                        "    *  *                  #",
                        "     *  *                 #",
                        "      *  *                #",
                        "       *  *               #",
                        "        *  *              #",
                        "         *  *             #",
                        "          *  *            #",
                        "           *  *           #",
                        "            *  *          #",
                        "             *  *         #",
                        "              *  *        #",
                        "               *  *       #",
                        "                *  *      #",
                        "                 *  *     #",
                        "                  *  *    #",
                        "                   *  *   #",
                        "                    *  *  #",
                        "                     *  * #",
                        "                      *  *#",
                        "                         *#",
                        "                         *#",
                        "                         *#",
                        "                      *  *#",
                        "                     *  * #",
                        "                    *  *  #",
                        "                   *  *   #",
                        "                  *  *    #",
                        "                 *  *     #",
                        "                *  *      #",
                        "               *  *       #",
                        "              *  *        #",
                        "               *  *       #",
                        "                *  *      #",
                        "                 *  *     #",
                        "                  *  *    #",
                        "                   *  *   #",
                        "                    *  *  #",
                        "                     *  * #",
                        "                      *  *#",
                        "                       *  #",
                }),
                Position.initial(0, 0)
        );
    }

    @Test
    void testExitMovedAround() {
        // Hint: Don't rely on the exit position, look into the future.
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        "     \n" +
                        "**** \n" +
                        "     \n" +
                        " ****\n" +
                        "     ",
                        // next step
                        " #   \n" +
                        "**** \n" +
                        "     \n" +
                        " ****\n" +
                        "     ",
                        // next step
                        "     \n" +
                        "**** \n" +
                        "     \n" +
                        " ****\n" +
                        "  #  ",
                }),
                Position.initial(4, 4)
        );
    }

    @Test
    void testBlinkingWalls() {
        // Hint: Don't rely on the exit position, look into the future.
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        " *   *   \n" +
                        " * * * * \n" +
                        " * * * * \n" +
                        " * * * * \n" +
                        " * * * * \n" +
                        "   *   * \n",
                        // next step
                        "*********\n" +
                        "*********\n" +
                        "*********\n" +
                        "*********\n" +
                        "*********\n" +
                        "******** \n",
                        // next step
                        " *   *   \n" +
                        " * * * * \n" +
                        " * * * * \n" +
                        " * * * * \n" +
                        " * * * * \n" +
                        "   *   * \n",
                        // next step
                        " ********\n" +
                        "*********\n" +
                        "*********\n" +
                        "*********\n" +
                        "*********\n" +
                        "*********\n",
                        // next step
                        " *   *   \n" +
                        " * * * * \n" +
                        " * * * * \n" +
                        " * * * * \n" +
                        " * * * * \n" +
                        "   *   * \n",
                        // next step
                        "*********\n" +
                        "*********\n" +
                        "*********\n" +
                        "*********\n" +
                        "*********\n" +
                        "***** ***\n",
                        // next step
                        "#*   *   \n" +
                        " * * * * \n" +
                        " * * * * \n" +
                        " * * * * \n" +
                        " * * * * \n" +
                        "   *   * \n",
                }),
                Position.initial(0, 0)
        );
    }

    @Test
    void testExitOnlyInNonlastStep() {
        // Hint: Don't only look for exits at the end of time.
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        "     \n" +
                        "*****\n" +
                        "     \n" +
                        " ****\n" +
                        "     ",
                        // next step
                        "     \n" +
                        "**** \n" +
                        "     \n" +
                        "*****\n" +
                        "     ",
                        // next step
                        "#    \n" +
                        "**** \n" +
                        "     \n" +
                        " ****\n" +
                        "     ",
                        // next step
                        "     \n" +
                        "**** \n" +
                        "     \n" +
                        " ****\n" +
                        "     ",
                }),
                Position.initial(4, 4)
        );
    }

    @Test
    void testStartNotInFirstStep() {
        // Hint: Sometimes you don't start at the first timestep. This makes earlier steps useless, but it happens.
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        "#   #\n" +
                        "     \n" +
                        "  #  \n" +
                        "     \n" +
                        "#   #\n",
                        // next step
                        "     \n" +
                        " *** \n" +
                        " * * \n" +
                        " ****\n" +
                        "     \n",
                        // next step
                        "     \n" +
                        " *** \n" +
                        " * * \n" +
                        " ****\n" +
                        "   # ",
                }),
                Position.at(1, 4, 0)
        );
    }

    @Test
    void testBigMill() {
        // Hint: you have to run a circle around the center to reach the end.
        checkMazeSolution(
                Maze.fromStrings(new String[]{
                        "    *  \n" +
                        "    *  \n" +
                        "*****  \n" +
                        "  * *  \n" +
                        "  *****\n" +
                        "  *    \n" +
                        "  *    \n",
                        // next step
                        "       \n" +
                        "       \n" +
                        "*****  \n" +
                        "  * *  \n" +
                        "  *****\n" +
                        "  *    \n" +
                        "  *    \n",
                        // next step
                        "    *  \n" +
                        "    *  \n" +
                        "*****  \n" +
                        "  * *  \n" +
                        "  ***  \n" +
                        "  *    \n" +
                        "  *    \n",
                        // next step
                        "    *  \n" +
                        "    *  \n" +
                        "*****  \n" +
                        "  * *  \n" +
                        "  *****\n" +
                        "       \n" +
                        "       \n",
                        // next step (clear out starting room)
                        "*****  \n" +
                        "*****  \n" +
                        "*****  \n" +
                        "  * *  \n" +
                        "  *****\n" +
                        "  *    \n" +
                        "  *    \n",
                        // next step
                        "    *  \n" +
                        "    *  \n" +
                        "  ***  \n" +
                        "  * *  \n" +
                        "  *****\n" +
                        "  *    \n" +
                        "  *    \n",
                        // next step
                        "   #*  \n" +
                        "   #*  \n" +
                        "*****  \n" +
                        "  * *  \n" +
                        "  *****\n" +
                        "  *    \n" +
                        "  *    \n",
                }),
                Position.initial(0, 0)
        );
    }

    @Test
    void testGeneratedPerfect001() {
        Pair<Maze, Position> puzzle = MazeGenerator.generate(123_456_789, 3, 100, 0.0, 1);
        checkMazeSolution(puzzle.getLeft(), puzzle.getRight());
    }

    @Test
    void testGeneratedPerfect002() {
        Pair<Maze, Position> puzzle = MazeGenerator.generate(123_456_789, 4, 120, 0.0, 2);
        checkMazeSolution(puzzle.getLeft(), puzzle.getRight());
    }

    @Test
    void testGeneratedPerfect003() {
        Pair<Maze, Position> puzzle = MazeGenerator.generate(123_456_789, 5, 140, 0.0, 3);
        checkMazeSolution(puzzle.getLeft(), puzzle.getRight());
    }

    @Test
    void testGeneratedPerfect004() {
        Pair<Maze, Position> puzzle = MazeGenerator.generate(123_456_789, 6, 160, 0.0, 2);
        checkMazeSolution(puzzle.getLeft(), puzzle.getRight());
    }

    @Test
    void testGeneratedPerfect005() {
        Pair<Maze, Position> puzzle = MazeGenerator.generate(123_456_789, 7, 200, 0.0, 1);
        checkMazeSolution(puzzle.getLeft(), puzzle.getRight());
    }
    
    @Test
    void testGeneratedPerfectRepeats() {
        double total = 0;
        for (int i = 0; i < 100; i++) {
            Pair<Maze, Position> puzzle = MazeGenerator.generate(123_456_789, 7, 200, 0.0, 1);
            total += checkMazeSolution(puzzle.getLeft(), puzzle.getRight());
        }
        System.out.println(String.format("took %.3f ms", total));
    }
}
