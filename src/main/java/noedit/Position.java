package noedit;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.checkerframework.checker.index.qual.NonNegative;

public class Position {
	@NonNegative public final int x;
	@NonNegative public final int y;

	public Position(@NonNegative int x, @NonNegative int y) {
		this.x = x;
		this.y = y;
	}

	@Nonnull
	@CheckReturnValue
	public static Position at(@NonNegative int x, @NonNegative int y) {
		return new Position(x, y);
	}
}
