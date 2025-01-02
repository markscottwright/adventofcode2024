package adventofcode2024;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Day15 {

	static public class Warehouse {

		private int width;
		private int height;
		private HashSet<Point> walls;
		private HashSet<Point> boxes;
		private Point robot;
		private List<Direction> moves;

		public Warehouse(int width, int height, HashSet<Point> walls, HashSet<Point> boxes, Point robot,
				List<Direction> moves) {
			this.width = width;
			this.height = height;
			this.walls = walls;
			this.boxes = boxes;
			this.robot = robot;
			this.moves = moves;
		}

		void moveRobot(Direction direction) {
			if (canMove(robot, direction)) {
				shiftBoxes(robot, direction);
				robot = robot.moveInDirection(direction);
			}
		}

		boolean canMove(Point robot, Direction direction) {
			Point nextPosition = robot.moveInDirection(direction);
			while (boxes.contains(nextPosition))
				nextPosition = nextPosition.moveInDirection(direction);

			if (walls.contains(nextPosition))
				return false;

			return true;
		}

		void shiftBoxes(Point robot, Direction direction) {
			ArrayList<Point> boxesToMove = new ArrayList<>();
			var p = robot.moveInDirection(direction);
			while (boxes.contains(p)) {
				boxesToMove.add(p);
				p = p.moveInDirection(direction);
			}
			assert !boxes.contains(p) && !walls.contains(p);

			Collections.reverse(boxesToMove);
			for (Point b : boxesToMove) {
				boxes.remove(b);
				boxes.add(b.moveInDirection(direction));
			}
		}

		void printOn(PrintStream out) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (walls.contains(new Point(x, y)))
						out.print('#');
					else if (boxes.contains(new Point(x, y)))
						out.print('O');
					else if (new Point(x, y).equals(robot))
						out.print('@');
					else
						out.print('.');
				}
				out.println();
			}
		}

		public static Warehouse parse(String input) {
			String[] mapAndMoves = input.split("\n\n");
			List<Direction> moves = mapAndMoves[1].replaceAll("\\s+", "").chars().mapToObj(d -> {
				if (d == '>')
					return Direction.e;
				else if ((char) d == '<')
					return Direction.w;
				else if ((char) d == '^')
					return Direction.n;
				else {
					assert ((char) d == 'v');
					return Direction.s;
				}
			}).toList();
			HashSet<Point> walls = new HashSet<>();
			HashSet<Point> boxes = new HashSet<>();
			Point robot = null;
			String[] lines = mapAndMoves[0].split("\n");
			int height = lines.length;
			int width = lines[0].length();

			for (int y = 0; y < lines.length; y++) {
				for (int x = 0; x < lines[y].toCharArray().length; x++) {
					char c = lines[y].toCharArray()[x];
					if (c == '#')
						walls.add(new Point(x, y));
					else if (c == 'O')
						boxes.add(new Point(x, y));
					else if (c == '@')
						robot = new Point(x, y);
				}
			}

			return new Warehouse(width, height, walls, boxes, robot, moves);
		}

		public void moveRobot() {
			for (Direction direction : moves) {
				moveRobot(direction);
			}
		}

		long boxGpsCoordinateSum() {
			return boxes.stream().mapToLong(b -> b.getX() + 100 * b.getY()).sum();
		}
	}

	static public class Warehouse2 {

		private int width;
		private int height;
		private HashSet<Point> walls;
		private HashSet<Point> boxesLhs;
		private Point robot;
		private List<Direction> moves;

		public Warehouse2(int width, int height, HashSet<Point> walls, HashSet<Point> boxes, Point robot,
				List<Direction> moves) {
			this.width = width;
			this.height = height;
			this.walls = walls;
			this.boxesLhs = boxes;
			this.robot = robot;
			this.moves = moves;
		}

		public boolean moveRobot(Direction direction) {
			if (walls.contains(robot.moveInDirection(direction))) {
				return false;
			} else if (!isBox(robot.moveInDirection(direction))) {
				robot = robot.moveInDirection(direction);
				return true;
			}

			// boxes in the way to east
			if (direction == Direction.e) {
				var boxesToMove = new ArrayList<Point>();

				var maybeBoxLhs = robot.moveInDirection(direction);
				while (boxesLhs.contains(maybeBoxLhs)) {
					boxesToMove.add(maybeBoxLhs);
					maybeBoxLhs = maybeBoxLhs.moveInDirection(direction).moveInDirection(direction);
				}
				if (walls.contains(maybeBoxLhs)) {
					return false;
				} else {
					Collections.reverse(boxesToMove);
					for (Point b : boxesToMove) {
						boxesLhs.remove(b);
						boxesLhs.add(b.moveInDirection(direction));
					}
					robot = robot.moveInDirection(direction);
					return true;
				}
			}

			// boxes in the way to the west
			else if (direction == Direction.w) {
				var boxesToMove = new ArrayList<Point>();

				var maybeBoxLhs = robot.moveInDirection(direction).moveInDirection(direction);
				while (boxesLhs.contains(maybeBoxLhs)) {
					boxesToMove.add(maybeBoxLhs);
					maybeBoxLhs = maybeBoxLhs.moveInDirection(direction).moveInDirection(direction);
				}

				// we're past the end of box lhs, move to right to check for a wall
				maybeBoxLhs = maybeBoxLhs.e();
				if (walls.contains(maybeBoxLhs)) {
					return false;
				} else {
					Collections.reverse(boxesToMove);
					for (Point b : boxesToMove) {
						boxesLhs.remove(b);
						boxesLhs.add(b.moveInDirection(direction));
					}
					robot = robot.moveInDirection(direction);
					return true;
				}
			}

			// boxes in the way to north or south
			else {
				var maybeBoxLhs = robot.moveInDirection(direction);

				// we're below/above the lhs of a box
				if (boxesLhs.contains(maybeBoxLhs) && canShiftBox(maybeBoxLhs, direction)) {
					shiftBox(maybeBoxLhs, direction);
					robot = maybeBoxLhs;
					return true;
				}

				// we're below/above the rhs of a box
				else if (boxesLhs.contains(maybeBoxLhs.moveInDirection(Direction.w))
						&& canShiftBox(maybeBoxLhs.moveInDirection(Direction.w), direction)) {
					shiftBox(maybeBoxLhs.moveInDirection(Direction.w), direction);
					robot = maybeBoxLhs;
					return true;
				}

				// we know we're above/below a box, so we must have failed to move them
				return false;
			}
		}

		private void shiftBox(Point boxLhs, Direction direction) {
			// should only be called of canShiftBox is true - we assume this is a valid
			// operation
			Point maybeBoxLhs = boxLhs.moveInDirection(direction);
			if (boxesLhs.contains(maybeBoxLhs)) {
				shiftBox(maybeBoxLhs, direction);
			}
			maybeBoxLhs = boxLhs.moveInDirection(direction).moveInDirection(Direction.w);
			if (boxesLhs.contains(maybeBoxLhs)) {
				shiftBox(maybeBoxLhs, direction);
			}
			maybeBoxLhs = boxLhs.moveInDirection(direction).moveInDirection(Direction.e);
			if (boxesLhs.contains(maybeBoxLhs)) {
				shiftBox(maybeBoxLhs, direction);
			}
			boxesLhs.remove(boxLhs);
			boxesLhs.add(boxLhs.moveInDirection(direction));
		}

		private boolean canShiftBox(Point boxLhs, Direction direction) {
			assert direction == Direction.n || direction == Direction.s;
			Point aheadOfLhs = boxLhs.moveInDirection(direction);

			// if there's a wall ahead of either edge of our box, we cant move
			if (walls.contains(aheadOfLhs) || walls.contains(aheadOfLhs.moveInDirection(Direction.e)))
				return false;

			// if aheadOfLhs is a boxLhs, we have a box right in front of us
			if (boxesLhs.contains(aheadOfLhs)) {
				return canShiftBox(aheadOfLhs, direction);
			}

			// theres two boxes ahead of us, staggered
			else if (boxesLhs.contains(aheadOfLhs.moveInDirection(Direction.w))
					&& boxesLhs.contains(aheadOfLhs.moveInDirection(Direction.e))) {
				return canShiftBox(aheadOfLhs.moveInDirection(Direction.w), direction)
						&& canShiftBox(aheadOfLhs.moveInDirection(Direction.e), direction);
			}

			// theres one box ahead of us, to the left of the grid
			else if (boxesLhs.contains(aheadOfLhs.moveInDirection(Direction.w))) {
				return canShiftBox(aheadOfLhs.moveInDirection(Direction.w), direction);
			}

			// theres one box ahead of us, to the right of the grid
			else if (boxesLhs.contains(aheadOfLhs.moveInDirection(Direction.e))) {
				return canShiftBox(aheadOfLhs.moveInDirection(Direction.e), direction);
			}

			// no boxes or walls in front of us
			else {
				return true;
			}
		}

		private boolean isBox(Point point) {
			return boxesLhs.contains(point) || boxesLhs.contains(point.w());
		}

		void printOn(PrintStream out) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (walls.contains(new Point(x, y)))
						out.print('#');
					else if (boxesLhs.contains(new Point(x, y)))
						out.print('[');
					else if (boxesLhs.contains(new Point(x - 1, y)))
						out.print(']');
					else if (new Point(x, y).equals(robot))
						out.print('@');
					else
						out.print('.');
				}
				out.println();
			}
		}

		public static Warehouse2 parse(String input) {
			String[] mapAndMoves = input.split("\n\n");
			List<Direction> moves = mapAndMoves[1].replaceAll("\\s+", "").chars().mapToObj(d -> {
				if (d == '>')
					return Direction.e;
				else if ((char) d == '<')
					return Direction.w;
				else if ((char) d == '^')
					return Direction.n;
				else {
					assert ((char) d == 'v');
					return Direction.s;
				}
			}).toList();
			HashSet<Point> walls = new HashSet<>();
			HashSet<Point> boxes = new HashSet<>();
			Point robot = null;
			String[] lines = mapAndMoves[0].split("\n");
			int height = lines.length;
			int width = lines[0].length() * 2;

			for (int y = 0; y < lines.length; y++) {
				for (int x = 0; x < lines[y].toCharArray().length; x++) {
					char c = lines[y].toCharArray()[x];
					if (c == '#') {
						walls.add(new Point(2 * x, y));
						walls.add(new Point(2 * x + 1, y));
					} else if (c == 'O') {
						boxes.add(new Point(2 * x, y));
					} else if (c == '@') {
						robot = new Point(2 * x, y);
					}
				}
			}

			return new Warehouse2(width, height, walls, boxes, robot, moves);
		}

		public void moveRobot() {
			for (Direction direction : moves) {
				moveRobot(direction);
			}
		}

		long boxGpsCoordinateSum() {
			return boxesLhs.stream().mapToLong(b -> b.getX() + 100 * b.getY()).sum();
		}

	}

	public static void main(String[] args) {
		String input = new DataProtection().decryptDay(15);
		Warehouse warehouse = Warehouse.parse(input);
		warehouse.moveRobot();
		System.out.println("Day 15 part 1: " + warehouse.boxGpsCoordinateSum());

		Warehouse2 warehouse2 = Warehouse2.parse(input);
		warehouse2.moveRobot();
		System.out.println("Day 15 part 2: " + warehouse2.boxGpsCoordinateSum());

		// interactive code for testing
//		warehouse2 = Warehouse2.parse(input);
//		System.out.println(warehouse2.robot);
//		while (true) {
//			warehouse2.printOn(System.out);
//			String userInput = System.console().readLine().toLowerCase();
//			try {
//				Direction direction = Direction.valueOf(userInput);
//					warehouse2.moveRobot(direction);
//			} catch (Exception e) {
//				System.out.println("Error:");
//			}
//		}
	}

}
