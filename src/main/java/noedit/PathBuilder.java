package noedit;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.checkerframework.common.value.qual.MinLen;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

/**
 * Create {@link Path} using {@link #build()}.
 */
public class PathBuilder {

	@Nonnull
	@MinLen(1)
	private final List<Position> steps = new ArrayList<>(256);

	public PathBuilder(@Nonnull Position initialPosition) {
		steps.add(initialPosition);
	}

	public void add(@Nonnull Position position) {
		steps.add(position);
	}

	@Nonnull
	@CanIgnoreReturnValue
	public Position left() {
		Position next = latest().left();
		steps.add(next);
		return next;
	}

	@Nonnull
	@CanIgnoreReturnValue
	public Position right() {
		Position next = latest().right();
		steps.add(next);
		return next;
	}

	@Nonnull
	@CanIgnoreReturnValue
	public Position up() {
		Position next = latest().up();
		steps.add(next);
		return next;
	}

	@Nonnull
	@CanIgnoreReturnValue
	public Position down() {
		Position next = latest().down();
		steps.add(next);
		return next;
	}

	@Nonnull
	@CanIgnoreReturnValue
	public Position waitStep() {
		Position next = latest().nextStep();
		steps.add(next);
		return next;
	}

	@Nonnull
	@CheckReturnValue
	public Position latest() {
		return steps.get(steps.size() - 1);
	}

	@Nonnull
	@CheckReturnValue
	public Path build() {
		return new Path(steps);
	}
}
