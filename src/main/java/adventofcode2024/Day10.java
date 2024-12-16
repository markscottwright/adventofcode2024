package adventofcode2024;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day10 {

	public static class Map {

		HashMap<Point, Integer> heights;

		public Map(HashMap<Point, Integer> heights) {
			this.heights = heights;
		}

		Collection<Point> possibleTrailheads() {
			return heights.entrySet().stream().filter(e -> e.getValue() == 0).map(e -> e.getKey()).toList();
		}

		List<List<Point>> findTrails() {
			var trails = new ArrayList<List<Point>>();
			possibleTrailheads().stream().map(this::findTrails).forEach(newTrails -> trails.addAll(newTrails));
			return trails;
		}

		List<List<Point>> findTrails(Point trailhead) {
			var trails = new ArrayList<List<Point>>();
			assert heights.get(trailhead).equals(0);

			int elevation = 0;
			findTrails(trailhead, elevation, trails, new ArrayList<Point>());
			return trails;
		}

		/**
		 * Don't really need to collect the trails, but it's nice for debugging
		 * 
		 * @param position current position
		 * @param elevation elevation of current position
		 * @param trails trails we've found from our starting position
		 * @param soFar current trails positions
		 */
		private void findTrails(Point position, int elevation, List<List<Point>> trails, ArrayList<Point> soFar) {
			assert heights.get(position).equals(elevation);
			if (soFar.size() == 9) {
				ArrayList<Point> trail = new ArrayList<Point>(soFar);
				trail.add(position);
				trails.add(trail);
			}

			for (Point next : List.of(position.n(), position.e(), position.s(), position.w())) {
				if (heights.containsKey(next) && heights.get(next).equals(elevation + 1)) {
					soFar.add(position);
					findTrails(next, elevation + 1, trails, soFar);
					soFar.remove(soFar.size() - 1);
				}
			}
		}

		Set<Point> findReachedHighs(Point trailhead) {
			var reachedHighs = new HashSet<Point>();
			assert heights.get(trailhead).equals(0);

			int elevation = 0;
			findReachedHighs(trailhead, elevation, reachedHighs, new ArrayList<Point>());
			return reachedHighs;
		}

		private void findReachedHighs(Point pos, int elevation, HashSet<Point> reachedHighs, ArrayList<Point> soFar) {
			assert heights.get(pos).equals(elevation);
			if (soFar.size() == 9) {
				reachedHighs.add(pos);
			}

			for (Point next : List.of(pos.n(), pos.e(), pos.s(), pos.w())) {
				if (heights.containsKey(next) && heights.get(next).equals(elevation + 1)) {
					soFar.add(pos);
					findReachedHighs(next, elevation + 1, reachedHighs, soFar);
					soFar.remove(soFar.size() - 1);
				}
			}
		}

		public Set<Point> findReachedHighs() {
			var reachedHighs = new HashSet<Point>();
			possibleTrailheads().stream().map(this::findReachedHighs).forEach(newHighs -> reachedHighs.addAll(newHighs));
			return reachedHighs;
		}
		
		public int findScore(Point trailhead) {
			return findReachedHighs(trailhead).size();
		}
		
		public int sumOfTrailheadScores() {
			return possibleTrailheads().stream().mapToInt(this::findScore).sum();
		}
	}

	public static void main(String[] args) {
        var readings = parse(new DataProtection().decryptDay(10));
        System.out.println("day 10 part 1: " + readings.sumOfTrailheadScores());
        System.out.println("day 10 part 2: " + readings.findTrails().size());
	}

	public static Day10.Map parse(String input) {
		HashMap<Point, Integer> heights = new HashMap<Point, Integer>();
		int y = 0;
		for (String line : input.split("\n")) {
			int x = 0;
			for (char c : line.toCharArray()) {
				if (c == '.')
					heights.put(new Point(x, y), 100);
				else
					heights.put(new Point(x, y), c - '0');
				x++;
			}
			y++;
		}
		return new Map(heights);
	}

}
