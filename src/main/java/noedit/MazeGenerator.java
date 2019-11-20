package noedit;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import static noedit.Cell.Exit;
import static noedit.Cell.Open;
import static noedit.Cell.Wall;

public class MazeGenerator {

	@Nonnull
	@CheckReturnValue
	public static Maze generate(int seed) {
		Cell[][][] maze = new Cell[][][]{{
				{Wall, Wall, Wall, Wall, Wall},
				{Wall, Open, Open, Open, Wall},
				{Wall, Open, Wall, Open, Wall},
				{Wall, Open, Wall, Open, Wall},
				{Wall, Exit, Wall, Wall, Wall},
		}};
		return new Maze(maze);
	}
}
