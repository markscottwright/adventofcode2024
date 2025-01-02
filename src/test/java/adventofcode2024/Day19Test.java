package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import adventofcode2024.Day19.Towels;

class Day19Test {

	@Test
	void test() {
		String input = """
				r, wr, b, g, bwu, rb, gb, br

				brwrr
				bggr
				gbbr
				rrbgbr
				ubwu
				bwurrg
				brgr
				bbrgwb
				""";
		Towels towels = Towels.parse(input);
		List<String> possiblePatterns = towels.possiblePatterns();
		assertThat(possiblePatterns.size()).isEqualTo(6);
	}

}
