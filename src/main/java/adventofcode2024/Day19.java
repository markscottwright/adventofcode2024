package adventofcode2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class Day19 {
	static class Towels {
		private ArrayList<String> towels;
		private List<String> desiredPatterns;
		private Pattern pattern;

		public Towels(List<String> towels, List<String> desiredPatterns) {
			this.towels = new ArrayList<String>();
			this.towels.addAll(towels);
			this.desiredPatterns = desiredPatterns;
			pattern = getRegexPattern();
		}

		@Override
		public String toString() {
			return "Towels [towels=" + towels + ", desiredPatterns=" + desiredPatterns + "]";
		}

		static Towels parse(String input) {
			var towelsAndPatterns = input.split("\n\n");
			List<String> towels = List.of(towelsAndPatterns[0].split(", "));
			List<String> desiredPatterns = List.of(towelsAndPatterns[1].split("\n"));
			return new Towels(towels, desiredPatterns);
		}

		public Pattern getRegexPattern() {
			return Pattern.compile("(" + String.join("|", towels) + ")*");
		}

		public List<String> possiblePatterns() {
			return desiredPatterns.stream().filter(p -> pattern.matcher(p).matches()).toList();
		}

		boolean canMatch(String pattern, HashSet<String> unmatchablePatterns) {
			if (pattern.isBlank())
				return true;

			if (unmatchablePatterns.contains(pattern))
				return false;

			for (var towel : towels) {
				if (pattern.startsWith(towel) && canMatch(pattern.substring(towel.length()), unmatchablePatterns))
					return true;
			}

			unmatchablePatterns.add(pattern);
			return false;
		}

		private long numMatches(String pattern, HashMap<String, Long> solutionCache) {
			if (pattern.isBlank())
				return 1;

			if (solutionCache.containsKey(pattern))
				return solutionCache.get(pattern);

			long c = 0;
			for (var towel : towels) {
				if (pattern.startsWith(towel)) {
					long numMatches = numMatches(pattern.substring(towel.length()), solutionCache);
					c += numMatches;
				}
			}
			solutionCache.put(pattern, c);

			return c;
		}

		public long numMatches(String pattern) {
			return numMatches(pattern, new HashMap<String, Long>());
		}
	}

	public static void main(String[] args) {
		String input = new DataProtection().decryptDay(19);
		Towels towels = Towels.parse(input);
		List<String> possiblePatterns = towels.possiblePatterns();
		System.out.println("Day 19 part 1: " + possiblePatterns.size());
		long numPossibleCombinations = possiblePatterns.stream().mapToLong(p -> towels.numMatches(p)).sum();
		System.out.println("Day 19 part 2: " + numPossibleCombinations);
	}
}
