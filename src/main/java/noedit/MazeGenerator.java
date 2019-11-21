package noedit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.index.qual.Positive;

import static noedit.Cell.Exit;
import static noedit.Cell.Open;
import static noedit.Cell.Wall;

public final class MazeGenerator {

	enum Direction {
		Right(+1, 0),
		Up(0, -1),
		Left(-1, 0),
		Down(0, +1);

		private final int dx;
		private final int dy;

		Direction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}
	}

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

			// Find unvisited neighbours (which are still walls, as walls are removed when visited).
			List<Direction> adjecentWalls = new ArrayList<>(4);   //TODO @mark: recycle this object
			if (current.x > 0 && !isVisited[current.x - 1][current.y]) {
				adjecentWalls.add(Direction.Left);
			}
			if (current.y > 0 && !isVisited[current.x][current.y - 1]) {
				adjecentWalls.add(Direction.Up);
			}
			if (current.x < width - 1 && !isVisited[current.x + 1][current.y]) {
				adjecentWalls.add(Direction.Right);
			}
			if (current.y < height - 1 && !isVisited[current.x][current.y + 1]) {
				adjecentWalls.add(Direction.Down);
			}

			Direction choice;
			if (adjecentWalls.size() > 1) {
				// We have to come back to this tile later.
				toVisit.push(current);
				choice = adjecentWalls.get(rand.nextInt(adjecentWalls.size()));
			} else if (adjecentWalls.size() == 1) {
				choice = adjecentWalls.get(0);
			} else {
				// This is a dead end.
				//TODO @mark: maybe collect ends, to build exists?
				continue;
			}

			// Clear the chosen direction.
			isOpen[2 * current.x + choice.dx][2 * current.y + choice.dy] = Open;
			Position next = Position.at(t, current.x + choice.dx, current.y + choice.dy);
			toVisit.push(next);
		}
		maze[t] = isOpen;

		return Pair.of(new Maze(maze), Position.initial(initX, initY));
	}

}
























