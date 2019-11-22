package noedit;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.common.value.qual.IntRange;
import org.checkerframework.common.value.qual.MinLen;

/**
 * Represents the positions in a solution path.
 */
public final class Path {

	private boolean validatedPhysical = false;
	@MinLen(1)
	@Nonnull
	private final List<Position> steps;

	public Path(@Nonnull List<Position> steps) {
		this.steps = Collections.unmodifiableList(steps);
	}

	/**
	 * Is this path physically possible?
	 *
	 * <ul>
	 * <li> Can only move one cell at a time.
	 * <li> Cannot move backwards in time.
	 * </ul>
	 */
	public boolean isPhysical() {
		if (validatedPhysical) {
			return true;
		}
		Set<Position> seen = new HashSet<>();
		Position prev = first();
		for (int i = 1; i < size(); i++) {
			Position current = get(i);
			if (seen.contains(current)) {
				System.err.println("Warning: move #" + i + " revisits position " + current + " which was already visited");
				// This must be a valid position, since it was visited before.
			} else if (current.t < prev.t) {
				System.err.println("Illegal move (#" + i + ", " + current + ") — cannot travel back in time");
				return false;
			} else if (current.t > prev.t + 1) {
				System.err.println("Illegal move (#" + i + ", " + current + ") — cannot move than one timestep forward per turn");
				return false;
			} else if (Math.abs(current.x - prev.x) > 1) {
				System.err.println("Illegal move (#" + i + ", " + current + ") — cannot travel more than one step horizontally");
				return false;
			} else if (Math.abs(current.y - prev.y) > 1) {
				System.err.println("Illegal move (#" + i + ", " + current + ") — cannot travel more than one step vertically");
				return false;
			} else if (current.x != prev.x && current.y != prev.y) {
				System.err.println("Illegal move (#" + i + ", " + current + ") — cannot travel both horizontally and vertically in the same turn");
				return false;
			} else if (current.t != prev.t && (current.x != prev.x || current.y != prev.y)) {
				System.err.println("Illegal move (#" + i + ", " + current + ") — cannot travel both spatially and advance to the next timestep in the same turn");
				return false;
			}
			seen.add(current);
			prev = current;
		}
		validatedPhysical = true;
		return true;
	}

	public boolean isSolution(@Nonnull Maze maze) {
		Validate.isTrue(isPhysical());
		// It is assumed (and checked separately) that the initial position is correct.
		for (int i = 0; i < size() - 1; i++) {
			Position step = get(i);
			if (Cell.Wall == maze.get(step)) {
				System.err.println("Illegal move (#" + i + ", " + step + ") — there is a wall there!");
				return false;
			}
			if (Cell.Exit == maze.get(step)) {
				System.err.println("Warning: move (#" + i + ", " + step + ") was on the exit, but you did not stop!");
				return true;
			}
		}
		return Cell.Exit == maze.get(last());
	}

	@Nonnull
	@CheckReturnValue
	public Position first() {
		return steps.get(0);
	}

	@Nonnull
	@CheckReturnValue
	public Position last() {
		return steps.get(steps.size() - 1);
	}

	@Nonnull
	@CheckReturnValue
	public Position get(@NonNegative int index) {
		return steps.get(index);
	}

	@IntRange(from = 0)
	public int size() {
		return steps.size();
	}
}
