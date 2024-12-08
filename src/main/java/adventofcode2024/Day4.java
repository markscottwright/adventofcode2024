package adventofcode2024;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class Day4 {

	public static class WordSearch {
		private HashMap<Point, Character> wordSearch;

		public WordSearch(HashMap<Point, Character> wordSearch) {
			this.wordSearch = wordSearch;
		}

		List<Point> findStarts(char startCharacter) {
			return wordSearch.entrySet().stream().filter(e -> {
				return e.getValue() == startCharacter;
			}).map(e -> e.getKey()).toList();
		}

		public String wordAt(Point start, Function<Point, Point> nextProvider, int length) {
			if (!wordSearch.containsKey(start))
				return "";
			String out = "" + wordSearch.get(start);
			for (int i = 0; i < length-1; ++i) {
				start = nextProvider.apply(start);
				if (!wordSearch.containsKey(start))
					return null;
				else
					out += wordSearch.get(start);
			}
			return out;
		}

		public int xmasCount() {
			int count = 0;
			var starts = findStarts('X');
			for (var start : starts) {
				//@formatter:off
				if ("XMAS".equals(wordAt(start, Point::toN, 4))) count++;
				if ("XMAS".equals(wordAt(start, Point::toNe, 4))) count++;
				if ("XMAS".equals(wordAt(start, Point::toE, 4))) count++;
				if ("XMAS".equals(wordAt(start, Point::toSe, 4))) count++;
				if ("XMAS".equals(wordAt(start, Point::toS, 4))) count++;
				if ("XMAS".equals(wordAt(start, Point::toSw, 4))) count++;
				if ("XMAS".equals(wordAt(start, Point::toW, 4))) count++;
				if ("XMAS".equals(wordAt(start, Point::toNw, 4))) count++;
				//@formatter:on
			}
			return count;
		}

		public int x_MasCount() {
			int count = 0;
			var starts = findStarts('A');
			for (var start : starts) {
				if (
				//@formatter:off
					(  "MAS".equals(wordAt(start.nw(), Point::toSe, 3))
					|| "MAS".equals(wordAt(start.se(), Point::toNw, 3)))

					&&
					
					(  "MAS".equals(wordAt(start.sw(), Point::toNe, 3))
					|| "MAS".equals(wordAt(start.ne(), Point::toSw, 3)))
					//@formatter:on
				)
					count++;
			}
			return count;
		}

	}

	public static WordSearch parse(String input) {
		String[] lines = input.split("\n");
		HashMap<Point, Character> wordSearch = new HashMap<>();
		for (int y = 0; y < lines.length; ++y) {
			for (int x = 0; x < lines[0].length(); ++x)
				wordSearch.put(new Point(x, y), lines[y].charAt(x));
		}
		return new WordSearch(wordSearch);
	}

	public static void main(String[] args) {
		String input = new DataProtection().decryptDay(4);
		WordSearch wordSearch = Day4.parse(input);
		System.out.println("Day 4 part 1: " + wordSearch.xmasCount());
		System.out.println("Day 4 part 2: " + wordSearch.x_MasCount());
	}

}
