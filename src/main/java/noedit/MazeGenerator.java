package noedit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.index.qual.NonNegative;
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

		Spatial initialPos = Spatial.at(
				rand.nextInt(width),
				rand.nextInt(height)
		);

		Cell[][][] maze = new Cell[2 * duration - 1][width][height];

		// Add the mazes
		Spatial layerPos = initialPos;
		for (int t = 0; t < duration - 1; t++) {
			maze[2 * t] = generatePerfect2D(rand, layerPos, width, height);
			layerPos = Spatial.at(
					rand.nextInt(width),
					rand.nextInt(height)
			);
			maze[2 * t + 1] = generateWallLayer(width, height);
			maze[2 * t + 1][layerPos.x][layerPos.y] = Open;
		}
		maze[2 * duration - 2] = generatePerfect2D(rand, layerPos, width, height);

		// Add the exits
		while (exitCount > 0) {
			if (maze[2 * duration - 2][rand.nextInt(width)][rand.nextInt(height)] != Exit) {
				maze[2 * duration - 2][rand.nextInt(width)][rand.nextInt(height)] = Exit;
				exitCount -= 1;
			}
		}

		return Pair.of(
				new Maze(maze),
				initialPos.atTime(0)
		);
	}

	@Nonnull
	@CheckReturnValue
	private static Cell[][] generateWallLayer(int width, int height) {
		Cell[][] layer = new Cell[2 * width - 1][2 * height - 1];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				layer[x][y] = Wall;
			}
		}
		return layer;
	}

	@Nonnull
	@CheckReturnValue
	private static Cell[][] generateCheckeredLayer(int width, int height) {
		Cell[][] layer = new Cell[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (x % 2 == 0 && y % 2 == 0) {
					layer[x][y] = Open;
				} else {
					layer[x][y] = Wall;
				}
			}
		}
		return layer;
	}

	@Nonnull
	@CheckReturnValue
	private static Cell[][] generatePerfect2D(
			@Nonnull Random rand,
			@Nonnull Spatial initialPos,
			@Positive int width,
			@Positive int height
	) {
		Cell[][] isOpen = generateCheckeredLayer(2 * width - 1, 2 * height - 1);
		boolean[][] isVisited = new boolean[width][height];
		Stack<Spatial> toVisit = new Stack<>();
		toVisit.push(initialPos);

		// This object is cached, cleared each loop.
		List<Direction> adjecentWalls = new ArrayList<>(4);

		while (!toVisit.isEmpty()) {
			Spatial current = toVisit.pop();
			isVisited[current.x][current.y] = true;

			// Find unvisited neighbours (which are still walls, as walls are removed when visited).
			adjecentWalls.clear();
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
				continue;
			}

			// Clear the chosen direction.
			isOpen[2 * current.x + choice.dx][2 * current.y + choice.dy] = Open;
			Spatial next = Spatial.at(current.x + choice.dx, current.y + choice.dy);
			toVisit.push(next);
		}

		return isOpen;
	}
}
