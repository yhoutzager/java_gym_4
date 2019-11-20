package noedit;

import java.util.Objects;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.checkerframework.checker.index.qual.NonNegative;

/**
 * This is a position in space-time - don't worry, there won't be any other relativistic effects!
 */
public final class Position {
	@NonNegative public final int t;
	@NonNegative public final int x;
	@NonNegative public final int y;

	public Position(@NonNegative int t, @NonNegative int x, @NonNegative int y) {
		this.t = t;
		this.x = x;
		this.y = y;
	}

	@Nonnull
	@CheckReturnValue
	public static Position at(@NonNegative int t, @NonNegative int x, @NonNegative int y) {
		return new Position(t, x, y);
	}

	@Nonnull
	@CheckReturnValue
	public static Position initial(@NonNegative int x, @NonNegative int y) {
		return new Position(0, x, y);
	}

	@Nonnull
	@CheckReturnValue
	public Position left() {
		return at(t, x - 1, y);
	}

	@Nonnull
	@CheckReturnValue
	public Position right() {
		return at(t, x + 1, y);
	}

	@Nonnull
	@CheckReturnValue
	public Position up() {
		return at(t, x, y - 1);
	}

	@Nonnull
	@CheckReturnValue
	public Position down() {
		return at(t, x, y + 1);
	}

	@Nonnull
	@CheckReturnValue
	public Position nextStep() {
		return at(t + 1, x, y);
	}

	public boolean equalsPosition(@Nonnull Position other) {
		return t == other.t && x == other.x && y == other.y;
	}

	@Override
	public boolean equals(@Nullable Object other) {
		if (!(other instanceof Position)) {
			return false;
		}
		return equalsPosition((Position) other);
	}

	@Override
	public int hashCode() {
		return Objects.hash(t, x, y);
	}

	@Override
	public String toString() {
		return "(t=" + t + ", x=" + x + ", y=" + y + ")";
	}
}
