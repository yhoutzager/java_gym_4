package noedit;

import java.util.Collections;
import java.util.List;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.Validate;
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
		if (1==1) throw new NotImplementedException("todo: ");  //TODO @mark:

		validatedPhysical = true;
		return true;
	}

	public boolean isSolution(@Nonnull Maze maze) {
		Validate.isTrue(validatedPhysical);

		if (1==1) throw new NotImplementedException("todo: ");  //TODO @mark:
		return true;
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
}
