package adventofcode2024;

import static java.lang.Integer.parseInt;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.bag.HashBag;

public class Day14 {
	public static class Robot {

		private Point position;
		private int vx;
		private int vy;

		public Robot(int x, int y, int vx, int vy) {
			this.position = new Point(x, y);
			this.vx = vx;
			this.vy = vy;
		}

		public int xAfterSeconds(Robots robots, int seconds) {
			int nextX = (position.getX() + vx * seconds) % robots.width;
			if (nextX < 0)
				nextX += robots.width;
			return nextX;
		}

		public int yAfterSeconds(Robots robots, int seconds) {
			int nextY = (position.getY() + vy * seconds) % robots.height;
			if (nextY < 0)
				nextY += robots.height;
			return nextY;
		}

		@Override
		public String toString() {
			return "Robot [position=" + position + ", vx=" + vx + ", vy=" + vy + "]";
		}
	}

	public static class Robots {
		@Override
		public String toString() {
			return "Robots [robots=" + robots + ", width=" + width + ", height=" + height + "]";
		}

		List<Robot> robots;
		private int width;
		private int height;

		public Robots(int width, int height, List<Robot> robots) {
			this.width = width;
			this.height = height;
			this.robots = robots;
		}

		public static Robots parse(int width, int height, String input) {
			Pattern pattern = Pattern.compile("p=(-?[0-9]+),(-?[0-9]+) v=(-?[0-9]+),(-?[0-9]+)");
			String[] lines = input.split("\n");

			List<Robot> robots = Arrays.stream(lines).map(line -> {
				Matcher matcher = pattern.matcher(line);
				if (!matcher.matches())
					throw new RuntimeException("Parse error " + line);
				return new Robot(parseInt(matcher.group(1)), parseInt(matcher.group(2)), parseInt(matcher.group(3)),
						parseInt(matcher.group(4)));
			}).toList();
			return new Robots(width, height, robots);
		}

		public Robots moveForSeconds(int seconds) {
			return new Robots(width, height,
					robots.stream().map(
							r -> new Robot(r.xAfterSeconds(this, seconds), r.yAfterSeconds(this, seconds), r.vx, r.vy))
							.toList());
		}

		public boolean contains4DiagonalLines() {
			HashBag<Point> robotPositions = new HashBag<Point>();
			robots.forEach(r -> {
				robotPositions.add(r.position);
			});

			return robotPositions.stream().filter(p -> 
				robotPositions.contains(p.ne()) 
				&& robotPositions.contains(p.ne().ne())   
				&& robotPositions.contains(p.ne().ne().ne())   
				&& robotPositions.contains(p.ne().ne().ne().ne())   
			).count() > 3;
		
		}
		public boolean maybeSymmetric() {
			HashBag<Point> robotPositions = new HashBag<Point>();
			robots.forEach(r -> {
				robotPositions.add(r.position);
			});
			int quadrant1 = quadrant1SafetyScore(robotPositions);
			int quadrant2 = quadrant2SafetyScore(robotPositions);
			int quadrant3 = quadrant3SafetyScore(robotPositions);
			int quadrant4 = quadrant4SafetyScore(robotPositions);
			return quadrant1 == quadrant2 && quadrant3 == quadrant4;
		}

		boolean isSymmetric() {
			HashBag<Point> robotPositions = new HashBag<Point>();
			robots.forEach(r -> {
				robotPositions.add(r.position);
			});
			for (int y = 0; y < height; ++y)
				for (int x = 0; x < width / 2; ++x) {
					if (robotPositions.contains(new Point(x, y))
							&& !robotPositions.contains(new Point(width - x - 1, y))) {
						return false;
					}
				}
			return true;
		}

		public int safetyScore() {
			HashBag<Point> robotPositions = new HashBag<Point>();
			robots.forEach(r -> {
				robotPositions.add(r.position);
			});
			int quadrant1 = quadrant1SafetyScore(robotPositions);
			int quadrant2 = quadrant2SafetyScore(robotPositions);
			int quadrant3 = quadrant3SafetyScore(robotPositions);
			int quadrant4 = quadrant4SafetyScore(robotPositions);
			return quadrant1 * quadrant2 * quadrant3 * quadrant4;
		}

		private int quadrant4SafetyScore(HashBag<Point> robotPositions) {
			int quadrant4 = 0;
			for (int x = width / 2 + 1; x < width; x++)
				for (int y = height / 2 + 1; y < height; y++)
					quadrant4 += robotPositions.getCount(new Point(x, y));
			return quadrant4;
		}

		private int quadrant3SafetyScore(HashBag<Point> robotPositions) {
			int quadrant3 = 0;
			for (int x = 0; x < width / 2; x++)
				for (int y = height / 2 + 1; y < height; y++)
					quadrant3 += robotPositions.getCount(new Point(x, y));
			return quadrant3;
		}

		private int quadrant2SafetyScore(HashBag<Point> robotPositions) {
			int quadrant2 = 0;
			for (int x = width / 2 + 1; x < width; x++)
				for (int y = 0; y < height / 2; y++)
					quadrant2 += robotPositions.getCount(new Point(x, y));
			return quadrant2;
		}

		private int quadrant1SafetyScore(HashBag<Point> robotPositions) {
			int quadrant1 = 0;
			for (int x = 0; x < width / 2; x++)
				for (int y = 0; y < height / 2; y++)
					quadrant1 += robotPositions.getCount(new Point(x, y));
			return quadrant1;
		}

		void printOn(PrintStream out) {
			HashBag<Point> robotPositions = new HashBag<Point>();
			robots.forEach(r -> {
				robotPositions.add(r.position);
			});

			for (int y = 0; y < height; ++y) {
				for (int x = 0; x < width; ++x) {
					int count = robotPositions.getCount(new Point(x, y));
					if (count == 0) {
						out.print(".");
					} else {
						out.print(count);
					}
				}
				out.println();
			}
		}
	}

	public static void main(String[] args) {
		String input = new DataProtection().decryptDay(14);
		Robots robots = Robots.parse(101, 103, input);
		System.out.println("Day 14 part 1: " + robots.moveForSeconds(100).safetyScore());

		int i;
		for (i = 0; i < 1000000000; ++i) {
			Robots moved = robots.moveForSeconds(i);
			if (moved.contains4DiagonalLines()) {
//				System.out.println(i);
//				moved.printOn(System.out);
//				System.out.println();
				break;
			}
		}
		System.out.println("Day 14 part 2: " + i);
	}
}
