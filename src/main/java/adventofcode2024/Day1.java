package adventofcode2024;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.collections4.bag.HashBag;

public class Day1 {
	public static void main(String[] args) {
		var day1 = parse(new DataProtection().decryptDay(1));
		System.out.println("day 1 part 1: " + day1.totalDistance());
		System.out.println("day 1 part 2: " + day1.simularityScore());
	}

	private ArrayList<Integer> locationIds1 = new ArrayList<Integer>();
	private ArrayList<Integer> locationIds2 = new ArrayList<Integer>();

	public static Day1 parse(String data) {
		Day1 day1 = new Day1();
		Arrays.stream(data.split("\n")).forEach(l -> {
			String[] locs = l.split("\\s+");
			day1.locationIds1.add(Integer.parseInt(locs[0]));
			day1.locationIds2.add(Integer.parseInt(locs[1]));
		});
		day1.locationIds1.sort(Integer::compareTo);
		day1.locationIds2.sort(Integer::compareTo);
		return day1;
	}

	public long totalDistance() {
		long totalDistance = 0;
		for (int i = 0; i < locationIds1.size(); ++i) {
			totalDistance += Math.abs(locationIds1.get(i) - locationIds2.get(i));
		}
		return totalDistance;
	}

	public long simularityScore() {
		var location2Counts = new HashBag<>(locationIds2);
		return locationIds1.stream().mapToLong(loc -> loc * location2Counts.getCount(loc)).sum();
	}
}
