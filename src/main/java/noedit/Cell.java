package noedit;

public enum Cell {
	Wall,
	Open,
	Exit,
	;

	public static Cell parse(char letter) {
		switch (letter) {
			case '*': return Wall;
			case ' ': return Open;
			case '#': return Exit;
		}
		throw new IllegalArgumentException("Cannot convert character '" + letter + "' to a maze cell");
	}

	@Override
	public String toString() {
		switch (this) {
			case Wall: return "*";
			case Open: return " ";
			case Exit: return "#";
		}
		throw new UnsupportedOperationException();
	}
}
