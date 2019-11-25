package javagym;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import noedit.Cell;
import noedit.Maze;
import noedit.Path;
import noedit.PathBuilder;
import noedit.Position;

import static noedit.Cell.Exit;
import static noedit.Cell.Wall;

public class Solution {

    @Nonnull
    @CheckReturnValue
    public Path solve(@Nonnull Maze maze, @Nonnull Position initialPosition) {
        assert Wall != maze.get(initialPosition):
                "Started inside a wall; this should never happen";

        System.out.println(maze.asStringAll());
        PathBuilder path = new PathBuilder(initialPosition);
        while (true) {
            Position leftPos = path.latest().left();
            Cell leftCell = maze.getOrElse(leftPos, Wall);
            if (leftCell == Wall) {
                // Let's just give up here.
                break;
            }
            path.left();
            if (leftCell == Exit) {
                break;
            }
        }
        return path.build();
    }
}
