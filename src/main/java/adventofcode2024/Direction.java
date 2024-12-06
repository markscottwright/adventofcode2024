package adventofcode2024;

enum Direction {
	n, e, s, w;

	Direction turnRight() {
		switch (this) {
		case e:
			return s;
		case n:
			return e;
		case s:
			return w;
		case w:
			return n;
		default:
			throw new IllegalStateException(); 
		}
	}
}