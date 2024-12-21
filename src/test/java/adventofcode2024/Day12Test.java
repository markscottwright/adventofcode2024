package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Day12Test {

	@Test
	void testPart1a() {
		String input = """
				AAAA
				BBCD
				BBCC
				EEEC
				""";
		Day12.Plots plots = Day12.Plots.parse(input);
		var regions = plots.findRegions();
		assertThat(regions.entries()).hasSize(5);
	}

	@Test
	void testPart1b() {
		String input = """
				OOOOO
				OXOXO
				OOOOO
				OXOXO
				OOOOO
				""";
		Day12.Plots plots = Day12.Plots.parse(input);
		var regions = plots.findRegions();
		assertThat(regions.entries()).hasSize(5);
	}
	
	@Test
	void testPart1c() {
		String input = """
				RRRRIICCFF
				RRRRIICCCF
				VVRRRCCFFF
				VVRCCCJFFF
				VVVVCJJCFE
				VVIVCCJJEE
				VVIIICJJEE
				MIIIIIJJEE
				MIIISIJEEE
				MMMISSJEEE
				""";
		Day12.Plots plots = Day12.Plots.parse(input);
		var regions = plots.findRegions();
		assertThat(regions.entries()).hasSize(11);
		assertThat(plots.fencingCost()).isEqualTo(1930);
	}

	@Test
	void testPart2a() {
		String input = """
				AAAA
				BBCD
				BBCC
				EEEC
				""";
		Day12.Plots plots = Day12.Plots.parse(input);
		assertThat(plots.fencingCostWithBulkDiscount()).isEqualTo(80);
	}
}

