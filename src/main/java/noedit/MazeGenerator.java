package noedit;

import java.util.Random;
import java.util.Stack;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.index.qual.Positive;

import static noedit.Cell.Exit;
import static noedit.Cell.Open;
import static noedit.Cell.Wall;

public final class MazeGenerator {

	/**
	 * Generate a random maze.
	 *
	 * @param seed Random seed, for reproducible results.
	 * @param duration Time dimension of the maze.
	 * @param circumference Spatial dimension of the maze.
	 * @param porosity How many extra holes are in the maze. Zero for a perfect maze, otherwise a number much smaller than one.
	 * @param exitCount Number of exits.
	 */
	@Nonnull
	@CheckReturnValue
	public static Pair<Maze, Position> generate(
			int seed,
			@Positive int duration,
			@Positive int circumference,
			@Positive double porosity,
			@Positive int exitCount
	) {
		//noinspection ConstantConditions
		Validate.isTrue(duration >= 1);
		Validate.isTrue(circumference >= 4);
		Validate.isTrue(porosity >= 0 && porosity < 1);
		//noinspection ConstantConditions
		Validate.isTrue(exitCount > 0 && exitCount <= circumference);

		Random rand = new Random(seed);
		int width = Math.max((int) Math.ceil((rand.nextDouble() * 0.8 + 0.1) * circumference), 2);
		int height = Math.max(circumference - width, 2);

		int t = 0;
		int initX = rand.nextInt(width);
		int initY = rand.nextInt(height);

		Cell[][][] maze = new Cell[duration][width][height];

		Cell[][] isOpen = new Cell[2 * width - 1][2 * height - 1];
		for (int x = 0; x < isOpen.length; x++) {
			for (int y = 0; y < isOpen[x].length; y++) {
				if (x % 2 == 0 && y % 2 == 0) {
					isOpen[x][y] = Open;
				} else {
					isOpen[x][y] = Wall;
				}
			}

		}
		boolean[][] isVisited = new boolean[width][height];
		Stack<Position> toVisit = new Stack<>();
		toVisit.add(Position.at(t, initX, initY));

		while (!toVisit.isEmpty()) {
			Position current = toVisit.pop();
			isVisited[current.x][current.y] = true;
			if (current.x > 0) {
				Position next = current.left();
				if (!isVisited[next.x][next.y]) {
					isOpen[2 * current.x - 1][2 * current.y] = Open;
					toVisit.push(next);
				}
			}
			if (current.y > 0) {
				Position next = current.up();
				if (!isVisited[next.x][next.y]) {
					isOpen[2 * current.x][2 * current.y - 1] = Open;
					toVisit.push(next);
				}
			}
			if (current.x < width - 1) {
				Position next = current.right();
				if (!isVisited[next.x][next.y]) {
					isOpen[2 * current.x + 1][2 * current.y] = Open;
					toVisit.push(next);
				}
			}
			if (current.y < height - 1) {
				Position next = current.down();
				if (!isVisited[next.x][next.y]) {
					isOpen[2 * current.x][2 * current.y + 1] = Open;
					toVisit.push(next);
				}
			}
		}
		maze[t] = isOpen;

		return Pair.of(new Maze(maze), Position.initial(initX, initY));
	}
}
























