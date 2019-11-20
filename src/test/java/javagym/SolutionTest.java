package javagym;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Test;

import noedit.Cell;
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
        assertEquals(Cell.Exit, maze.get(path.last()),
                "The solution does not solve the maze (it does not end at an exit).");
        assertTrue(path.isSolution(maze),
                "The solution is not valid for the maze — it may cross walls or leave the maze area.");
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
}
