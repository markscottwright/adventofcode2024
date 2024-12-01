package adventofcode2025;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.lang3.tuple.Pair;

public class Day1 {
	public static Pair<List<Integer>, List<Integer>> parse(String data) {
		var loc1 = new ArrayList<Integer>();
		var loc2 = new ArrayList<Integer>();
		Arrays.stream(data.split("\n")).forEach(l -> {
			String[] locs = l.split("\\s+");
			loc1.add(Integer.parseInt(locs[0]));
			loc2.add(Integer.parseInt(locs[1]));
		});
		loc1.sort(Integer::compareTo);
		loc2.sort(Integer::compareTo);
		return Pair.of(loc1, loc2);
	}

	public static long totalDistance(List<Integer> locs1, List<Integer> locs2) {
		long totalDistance = 0;
		for (int i = 0; i < locs1.size(); ++i) {
			totalDistance += Math.abs(locs1.get(i) - locs2.get(i));
		}
		return totalDistance;
	}

	public static long simularityScore(List<Integer> locs1, List<Integer> locs2) {
		var location2Counts = new HashBag<>(locs2);
		long simularityScore = locs1.stream().mapToLong(loc -> loc * location2Counts.getCount(loc)).sum();
		return simularityScore;
	}

	public static void main(String[] args) throws IOException {
		var inputData = parse(Files.readString(Path.of("data/day1.dat")));
		System.out.println("day 1 part 1: " + totalDistance(inputData.getLeft(), inputData.getRight()));
		System.out.println("day 1 part 2: " + simularityScore(inputData.getLeft(), inputData.getRight()));
	}
}
