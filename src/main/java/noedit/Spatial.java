package noedit;

import java.util.Objects;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.checkerframework.checker.index.qual.NonNegative;

/**
 * This is a position in space, without time.
 */
public final class Spatial {
	@NonNegative public final int x;
	@NonNegative public final int y;

	public Spatial(@NonNegative int x, @NonNegative int y) {
		this.x = x;
		this.y = y;
	}

	@Nonnull
	@CheckReturnValue
	public static Spatial at(@NonNegative int x, @NonNegative int y) {
		return new Spatial(x, y);
	}

	@Nonnull
	@CheckReturnValue
	public Spatial left() {
		return at(x - 1, y);
	}

	@Nonnull
	@CheckReturnValue
	public Spatial right() {
		return at(x + 1, y);
	}

	@Nonnull
	@CheckReturnValue
	public Spatial up() {
		return at(x, y - 1);
	}

	@Nonnull
	@CheckReturnValue
	public Spatial down() {
		return at(x, y + 1);
	}

	@Nonnull
	@CheckReturnValue
	public Position atTime(@NonNegative int t) {
		return Position.at(t, x, y);
	}

	public boolean equalsPosition(@Nonnull Spatial other) {
		return x == other.x && y == other.y;
	}

	@Override
	public boolean equals(@Nullable Object other) {
		if (!(other instanceof Spatial)) {
			return false;
		}
		return equalsPosition((Spatial) other);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "(x=" + x + ", y=" + y + ")";
	}
}
