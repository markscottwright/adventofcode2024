package adventofcode2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.Pair;

public class Day3 {
	public static void main(String[] args) {
		String input = new DataProtection().decryptDay(3);
		System.out.println("day 3 part 1: " + sumOfMultiplications(parse(input)));
		System.out.println("day 3 part 2: " + sumOfMultiplications(parseWithConditionals(input)));
	}

	public static List<Pair<Integer, Integer>> parse(String input) {
		try (var scanner = new Scanner(input)) {
			return scanner.findAll("mul\\(([0-9][0-9]?[0-9]?),([0-9][0-9]?[0-9]?)\\)")
					.map(m -> Pair.of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)))).toList();
		}
	}

	public static List<Pair<Integer, Integer>> parseWithConditionals(String input) {
		var out = new ArrayList<Pair<Integer, Integer>>();
		boolean enabled = true;
		try (var scanner = new Scanner(input)) {
			for (var match : scanner.findAll(
			//@formatter:off
					"do\\(\\)"
					+ "|"
					+ "don't\\(\\)"
					+ "|"
					+ "mul\\("
						+ "([0-9][0-9]?[0-9]?),"
						+ "([0-9][0-9]?[0-9]?)"
					+ "\\)").toList()) {
				//@formatter:on
				if (match.group().equals("do()"))
					enabled = true;
				else if (match.group().equals("don't()"))
					enabled = false;
				else if (enabled) {
					out.add(Pair.of(Integer.parseInt(match.group(1)), Integer.parseInt(match.group(2))));
				}
			}
		}
		return out;
	}

	public static long sumOfMultiplications(List<Pair<Integer, Integer>> multiplications) {
		return multiplications.stream().mapToLong(p -> p.getLeft() * p.getRight()).sum();
	}
}
