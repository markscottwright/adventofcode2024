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

	Direction turnLeft() {
		switch (this) {
		case e:
			return n;
		case n:
			return w;
		case s:
			return e;
		case w:
			return s;
		default:
			throw new IllegalStateException(); 
		}
	}
}