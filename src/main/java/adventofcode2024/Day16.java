package adventofcode2024;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;

public class Day16 {

	public static class Move {

		@Override
		public int hashCode() {
			return Objects.hash(direction, position);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Move other = (Move) obj;
			return direction == other.direction && Objects.equals(position, other.position);
		}

		private Direction direction;

		@Override
		public String toString() {
			return "Move [direction=" + direction + ", position=" + position + ", score=" + score + "]";
		}

		private Point position;
		private int score;
		private Move lastMove;

		public Move(Direction direction, Point position, int score, Move lastMove) {
			this.direction = direction;
			this.position = position;
			this.score = score;
			this.lastMove = lastMove;
		}

		public HashSet<Point> path() {
			HashSet<Point> out = new HashSet<Point>();
			out.add(position);
			Move p = lastMove;
			while (p != null) {
				out.add(p.position);
				p = p.lastMove;
			}
			return out;
		}
	}

	public static class Maze {

		private HashSet<Point> walls = new HashSet<Point>();
		private Point start;
		private Point end;
		private GridParser gridParser;
		private HashSet<HashSet<Point>> solutionPaths;

		public static Maze parse(String input) {
			Maze out = new Maze();
			out.gridParser = new GridParser(input);
			out.gridParser.forEach((p, c) -> {
				if (c == '#')
					out.walls.add(p);
				else if (c == 'S')
					out.start = p;
				else if (c == 'E')
					out.end = p;
			});
			return out;
		}

		void printOn(PrintStream out) {
			gridParser.printOn(System.out, p -> {
				if (walls.contains(p))
					return '#';
				else if (start.equals(p))
					return 'S';
				else if (end.equals(p))
					return 'E';
				else
					return '.';
			});
		}

		public int shortestPathScore() {
			HashMap<Move, Integer> movesSeen = new HashMap<Move, Integer>();
			var moves = new PriorityQueue<Move>((m1, m2) -> {
				return Integer.compare(m1.score, m2.score);
			});

			if (!walls.contains(start.moveInDirection(Direction.e))) {
				addIfNotWorse(new Move(Direction.e, start.moveInDirection(Direction.e), 1, null), moves, movesSeen);
			}
			addIfNotWorse(new Move(Direction.n, start, 1000, null), moves, movesSeen);
			addIfNotWorse(new Move(Direction.s, start, 1000, null), moves, movesSeen);

			solutionPaths = new HashSet<HashSet<Point>>();
			int bestSoFar = Integer.MAX_VALUE;
			Move m;
			while ((m = moves.poll()) != null) {
				if (m.score > bestSoFar)
					continue;

				if (m.position.equals(end)) {
					if (bestSoFar == m.score) {
						solutionPaths.add(m.path());
					} else if (m.score < bestSoFar) {
						solutionPaths.clear();
						solutionPaths.add(m.path());
						bestSoFar = m.score;
					}
				} else {
					if (!walls.contains(m.position.moveInDirection(m.direction))) {
						addIfNotWorse(new Move(m.direction, m.position.moveInDirection(m.direction), m.score + 1, m),
								moves, movesSeen);
					}
					addIfNotWorse(new Move(m.direction.turnLeft(), m.position, m.score + 1000, m), moves, movesSeen);
					addIfNotWorse(new Move(m.direction.turnRight(), m.position, m.score + 1000, m), moves, movesSeen);
				}
			}
			return bestSoFar;
		}

		private void addIfNotWorse(Move move, PriorityQueue<Move> moves, HashMap<Move, Integer> movesSeen) {
			if (move.score <= movesSeen.getOrDefault(move, Integer.MAX_VALUE)) {
				moves.add(move);
				movesSeen.put(move, move.score);
			}
		}

		public HashSet<HashSet<Point>> getSolutionPaths() {
			return solutionPaths;
		}
		
		public int pointsInAllSolutions() {
			HashSet<Point> allSolutionPoints = new HashSet<Point>();
			solutionPaths.forEach(allSolutionPoints::addAll);
			return allSolutionPoints.size();
		}
	}

	public static void main(String[] args) {
		Maze maze = Maze.parse(new DataProtection().decryptDay(16));
		System.out.println("Day 16 part 1: " + maze.shortestPathScore());
		System.out.println("Day 16 part 2: " + maze.pointsInAllSolutions());
	}

}
