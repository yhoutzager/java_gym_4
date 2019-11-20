package javagym;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;

import noedit.Maze;
import noedit.Position;

import static noedit.Cell.Open;
import static noedit.Cell.Wall;
import static noedit.Cell.Exit;

public class Solution {

    @Nonnull
    public void solve(@Nonnull Maze maze, @Nonnull Position initialPosition) {
        Validate.isTrue(Open == maze.get(0, initialPosition),
                "Started inside a wall; this should never happen");

        System.out.println(maze.asStringAll());
        //TODO: Implement your solution here
        throw new UnsupportedOperationException("solution not yet implemented");
    }
}
