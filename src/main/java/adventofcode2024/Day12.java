package adventofcode2024;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public class Day12 {

	public static class Plots {

		public HashMap<Point, Character> plots;

		public static Plots parse(String input) {
			var plots = new HashMap<Point, Character>();
			for (int y = 0; y < input.split("\n").length; y++) {
				String line = input.split("\n")[y];
				for (int x = 0; x < line.toCharArray().length; x++) {
					char c = line.toCharArray()[x];
					Point point = new Point(x, y);
					plots.put(point, c);
				}
			}
			Plots out = new Plots();
			out.plots = plots;
			return out;
		}

		public ArrayListValuedHashMap<Character, Set<Point>> findRegions() {

			var regions = new ArrayListValuedHashMap<Character, Set<Point>>();
			HashSet<Point> assigned = new HashSet<Point>();
			for (Entry<Point, Character> entry : plots.entrySet()) {
				if (!assigned.contains(entry.getKey())) {
					HashSet<Point> region = new HashSet<Point>();
					addAllInRegion(region, entry.getValue(), entry.getKey());
					regions.put(entry.getValue(), region);
					assigned.addAll(region);
				}
			}

			return regions;
		}

		private void addAllInRegion(HashSet<Point> regionSoFar, Character plotType, Point point) {
			regionSoFar.add(point);
			for (Point neighbor : point.getCardinalNeighbors()) {
				if (plotType.equals(plots.get(neighbor)) && !regionSoFar.contains(neighbor)) {
					addAllInRegion(regionSoFar, plotType, neighbor);
				}
			}
		}

		public int calculatePerimiter(Set<Point> region) {
			int perimiter = 0;
			for (Point point : region) {
				for (Point pointNeighbor : point.getCardinalNeighbors()) {
					if (!region.contains(pointNeighbor))
						perimiter += 1;
				}
			}
			return perimiter;
		}

		public int calculateNumSides(Set<Point> region) {
			int numSides = 0;
			
			var inWestSide = new HashSet<Point>();
			var inEastSide = new HashSet<Point>();
			var inNorthSide = new HashSet<Point>();
			var inSouthSide = new HashSet<Point>();
			for (Point point : region) {
				boolean newNorthSideFound, newSouthSideFound, newEastSideFound, newWestSideFound;
				newNorthSideFound = newSouthSideFound = newEastSideFound = newWestSideFound = false;

				// find west side by walking north
				var p = point;
				while (region.contains(p) && !inWestSide.contains(p) && !region.contains(p.w())) {
					inWestSide.add(p);
					p = p.n();
					newWestSideFound = true;
				}
				// then south
				p = point.s();
				while (region.contains(p) && !inWestSide.contains(p) && !region.contains(p.w())) {
					inWestSide.add(p);
					p = p.s();
					newWestSideFound = true;
				}
				// find east side by walking north
				p = point;
				while (region.contains(p) && !inEastSide.contains(p) && !region.contains(p.e())) {
					inEastSide.add(p);
					p = p.n();
					newEastSideFound = true;
				}
				// then south
				p = point.s();
				while (region.contains(p) && !inEastSide.contains(p) && !region.contains(p.e())) {
					inEastSide.add(p);
					p = p.s();
					newEastSideFound = true;
				}
				// find north side by walking east
				p = point;
				while (region.contains(p) && !inNorthSide.contains(p) && !region.contains(p.n())) {
					inNorthSide.add(p);
					p = p.e();
					newNorthSideFound = true;
				}
				// then west
				p = point.w();
				while (region.contains(p) && !inNorthSide.contains(p) && !region.contains(p.n())) {
					inNorthSide.add(p);
					p = p.w();
					newNorthSideFound = true;
				}
				// find south side by walking east
				p = point;
				while (region.contains(p) && !inSouthSide.contains(p) && !region.contains(p.s())) {
					inSouthSide.add(p);
					p = p.e();
					newSouthSideFound = true;
				}
				// then west
				p = point.w();
				while (region.contains(p) && !inSouthSide.contains(p) && !region.contains(p.s())) {
					inSouthSide.add(p);
					p = p.w();
					newSouthSideFound = true;
				}
				if (newNorthSideFound)
					numSides++;
				if (newSouthSideFound)
					numSides++;
				if (newEastSideFound)
					numSides++;
				if (newWestSideFound)
					numSides++;
			}
			return numSides;
		}

		public long fencingCost() {
			long fencingCost = 0;
			for (var typeAndRegion : findRegions().entries()) {
				var region = typeAndRegion.getValue();
				int perimiter = calculatePerimiter(region);
				fencingCost += perimiter * region.size();
			}
			return fencingCost;
		}

		public long fencingCostWithBulkDiscount() {
			long fencingCost = 0;
			for (var typeAndRegion : findRegions().entries()) {
				var region = typeAndRegion.getValue();
				int numSides = calculateNumSides(region);
//				System.out.println("num sides = " + numSides + " : " + typeAndRegion.getKey() + " : " + region);
				fencingCost += numSides * region.size();
			}
			return fencingCost;
		}
	}

	public static void main(String[] args) {
		String input = new DataProtection().decryptDay(12);
		Plots plots = Plots.parse(input);
		System.out.println("Day 12 part 1: " + plots.fencingCost());
		System.out.println("Day 12 part 2: " + plots.fencingCostWithBulkDiscount());
	}

}
