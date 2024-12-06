package adventofcode2024;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.tuple.Pair;

public class Day6 {
	static public class Map {
		HashSet<Point> obstructions;
		Point startingGuardPosition;
		Direction startingGuardDirection;
		int width;
		int height;

		public static Map parse(String input) {
			Map map = new Map();
			map.obstructions = new HashSet<Point>();

			String[] lines = input.split("\n");
			map.height = lines.length;
			map.width = lines[0].length();

			for (int y = 0; y < lines.length; ++y) {
				for (int x = 0; x < lines[y].length(); ++x) {
					if (lines[y].charAt(x) == '#') {
						map.obstructions.add(new Point(x, y));
					} else if (lines[y].charAt(x) == '^') {
						map.startingGuardPosition = new Point(x, y);
						map.startingGuardDirection = Direction.n;
					} else if (lines[y].charAt(x) == '>') {
						map.startingGuardPosition = new Point(x, y);
						map.startingGuardDirection = Direction.e;
					} else if (lines[y].charAt(x) == '<') {
						map.startingGuardPosition = new Point(x, y);
						map.startingGuardDirection = Direction.w;
					} else if (lines[y].charAt(x) == 'v') {
						map.startingGuardPosition = new Point(x, y);
						map.startingGuardDirection = Direction.s;
					}
				}
			}

			assert map.startingGuardPosition != null;
			return map;
		}

		public Set<Point> doRounds() {
			var visited = new HashSet<Point>();
			var pos = startingGuardPosition;
			var direction = startingGuardDirection;
			while (pos.insideBox(width, height)) {
				if (obstructions.contains(pos.moveInDirection(direction)))
					direction = direction.turnRight();
				visited.add(pos);
				pos = pos.moveInDirection(direction);
			}
			return visited;
		}

		public boolean doRoundsCheckingForLoop() {
			var visited = new HashSet<Pair<Point, Direction>>();
			var pos = startingGuardPosition;
			var direction = startingGuardDirection;
			while (pos.insideBox(width, height)) {
				// been here before moving in the same direction, so this must be a loop
				if (visited.contains(Pair.of(pos, direction)))
					return true;

				visited.add(Pair.of(pos, direction));

				// use a while here - maybe more than one adjacent obstruction
				while (obstructions.contains(pos.moveInDirection(direction)))
					direction = direction.turnRight();
				pos = pos.moveInDirection(direction);
			}
			return false;
		}

		public int numberOfLoopCausingPositions() {
			int numLoops = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					Point newObstruction = new Point(x, y);
					if (!newObstruction.equals(startingGuardPosition) && !obstructions.contains(newObstruction)) {
						obstructions.add(newObstruction);
						if (doRoundsCheckingForLoop()) {
							numLoops++;
						}
						obstructions.remove(newObstruction);
					}
				}
			}

			return numLoops;
		}

	}

	public static void main(String[] args)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		String input = new DataProtection().decrypt(6);
		Map map = Map.parse(input);
		int positionsVisited = map.doRounds().size();
		System.out.println("Day 6 part 1: " + positionsVisited);
		System.out.println("Day 6 part 2: " + map.numberOfLoopCausingPositions());
	}
}
