package adventofcode2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class Day18 {

	static public class MemoryGrid {

		private List<Point> corruptedMemory;
		private int width;
		private int height;

		public MemoryGrid(int width, int height, List<Point> corruptedMemory) {
			this.width = width;
			this.height = height;
			this.corruptedMemory = corruptedMemory;
		}

		public static MemoryGrid parse(int width, int height, String input) {
			List<Point> corruptedMemory = Arrays.stream(input.split("\n")).map(l -> {
				String[] xAndY = l.split(",");
				return new Point(Integer.parseInt(xAndY[0]), Integer.parseInt(xAndY[1]));
			}).toList();
			return new MemoryGrid(width, height, corruptedMemory);
		}

		List<Point> calculateRun(int numBytesCorrupted) {
			HashSet<Point> corruptedMemoryLocations = new HashSet<Point>();
			corruptedMemory.stream().limit(numBytesCorrupted).forEach(corruptedMemoryLocations::add);
			Point end = new Point(width - 1, height - 1);

			HashSet<Point> seen = new HashSet<Point>();
			var moves = new ArrayList<Pair<Point, ArrayList<Point>>>();
			moves.add(Pair.of(new Point(0, 0), new ArrayList<>()));
			while (!moves.isEmpty()) {
				Pair<Point, ArrayList<Point>> move = moves.remove(0);
				Point pos = move.getLeft();
				if (pos.equals(end)) {
					return move.getRight();
				}

				for (Point maybeNext : List.of(pos.n(), pos.e(), pos.s(), pos.w())) {
					if (maybeNext.insideBox(width, height) && !seen.contains(maybeNext)
							&& !corruptedMemoryLocations.contains(maybeNext)) {
						seen.add(maybeNext);
						ArrayList<Point> newPath = new ArrayList<Point>();
						newPath.addAll(move.getRight());
						newPath.add(maybeNext);
						moves.add(Pair.of(maybeNext, newPath));
					}
				}
			}

			return null;
		}

		public Point corruptedMemoryLocationThatPreventsExit() {
			for (int i = 0; i < corruptedMemory.size(); ++i) {
				if (calculateRun(i) == null) {
					return corruptedMemory.get(i - 1);
				}
			}

			return null;
		}
	}

	public static void main(String[] args) {
		String input = new DataProtection().decryptDay(18);
		Day18.MemoryGrid memory = Day18.MemoryGrid.parse(71, 71, input);
		List<Point> run = memory.calculateRun(1024);
		System.out.println("Day 18 part 1: " + run.size());
		Point point = memory.corruptedMemoryLocationThatPreventsExit();
		System.out.println("Day 18 part 2: " + point.getX() + "," + point.getY());
	}

}
