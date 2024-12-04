package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Day4Test {

	@Test
	void test() {
		String input = """
				MMMSXXMASM
				MSAMXMSMSA
				AMXSXMAAMM
				MSAMASMSMX
				XMASAMXAMM
				XXAMMXXAMA
				SMSMSASXSS
				SAXAMASAAA
				MAMMMXMMMM
				MXMXAXMASX""";
		Day4.WordSearch wordSearch = Day4.parse(input);
		assertThat(wordSearch.xmasCount()).isEqualTo(18);
	}

}
