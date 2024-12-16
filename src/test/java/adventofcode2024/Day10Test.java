package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import adventofcode2024.Day10.Map;

class Day10Test {

	@Test
	void test() {
		String input = """
				...0...
				...1...
				...2...
				6543456
				7.....7
				8.....8
				9.....9
				""";
		Day10.Map map = Day10.parse(input);
		assertThat(map.sumOfTrailheadScores()).isEqualTo(2);
		
		input = """
				..90..9
				...1.98
				...2..7
				6543456
				765.987
				876....
				987....
				""";
		map = Day10.parse(input);
		assertThat(map.sumOfTrailheadScores()).isEqualTo(4);
		
		input = """
				89010123
				78121874
				87430965
				96549874
				45678903
				32019012
				01329801
				10456732
				""";
		map = Day10.parse(input);
		assertThat(map.sumOfTrailheadScores()).isEqualTo(36);
	}
	
	@Test
	void testPartTwo() {
		String input = """
				.....0.
				..4321.
				..5..2.
				..6543.
				..7..4.
				..8765.
				..9....
				""";
		Map map = Day10.parse(input);
		assertThat(map.findTrails()).hasSize(3);

		input = """
				012345
				123456
				234567
				345678
				4.6789
				56789.
				""";
		map = Day10.parse(input);
		assertThat(map.findTrails()).hasSize(227);
		
		input = """
				89010123
				78121874
				87430965
				96549874
				45678903
				32019012
				01329801
				10456732
				""";
		map = Day10.parse(input);
		assertThat(map.findTrails()).hasSize(81);
	}

}
