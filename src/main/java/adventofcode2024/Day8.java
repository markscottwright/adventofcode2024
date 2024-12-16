package adventofcode2024;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class Day8 {
	static class Map {
		HashMap<Character, HashSet<Point>> frequencyPositions = new HashMap<>();
		int maxX;
		int maxY;

		static Map parse(String input) {
			Map map = new Map();

			String[] lines = input.split("\n");
			map.maxY = lines.length;
			map.maxX = lines[0].length();
			for (int y = 0; y < lines.length; y++) {
				char[] line = lines[y].toCharArray();
				for (int x = 0; x < line.length; x++) {
					char c = line[x];
					if (c != '.') {
						if (!map.frequencyPositions.containsKey(c))
							map.frequencyPositions.put(c, new HashSet<>());
						map.frequencyPositions.get(c).add(new Point(x, y));
					}
				}
			}

			return map;
		}

		public void printOn(PrintStream out) {
			for (int y = 0; y < maxY; y++) {
				for (int x = 0; x < maxX; x++) {
					boolean printed = false;
					Point point = new Point(x, y);
					for (Entry<Character, HashSet<Point>> entry : frequencyPositions.entrySet()) {
						if (entry.getValue().contains(point)) {
							out.print(entry.getKey());
							printed = true;
						}
					}
					if (!printed)
						out.print('.');
				}
				out.println();
			}
		}

		public HashSet<Point> findAntiNodes() {
			HashSet<Point> antiNodes = new HashSet<>();
			this.frequencyPositions.forEach((c, points) -> {
				if (c != '#') {
					for (Point p1 : points) {
						for (Point p2 : points) {
							if (!p1.equals(p2)) {
								Distance distance = p1.distance(p2);
								Point antiNode = p2.move(distance);
								if (antiNode.insideBox(maxX, maxY))
									antiNodes.add(antiNode);
							}
						}
					}
				}
			});
			return antiNodes;
		}
		
		public HashSet<Point> findAntiNodesWithResonantHarmonics() {
			HashSet<Point> antiNodes = new HashSet<>();
			this.frequencyPositions.forEach((c, points) -> {
				if (c != '#') {
					for (Point p1 : points) {
						for (Point p2 : points) {
							if (!p1.equals(p2)) {
								Distance distance = p1.distance(p2);
								Point antiNode = p2;
								while (antiNode.insideBox(maxX, maxY)) {
									antiNodes.add(antiNode);
									antiNode = antiNode.move(distance);
								}
							}
						}
					}
				}
			});
			return antiNodes;
		}
	}

	public static void main(String[] args)  {
		String input = new DataProtection().decryptDay(8);
		Map map = Day8.Map.parse(input);
		System.out.println("Day 8 part 1: " + map.findAntiNodes().size());
		System.out.println("Day 8 part 2: " + map.findAntiNodesWithResonantHarmonics().size());
	}

}
