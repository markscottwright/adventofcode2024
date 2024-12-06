package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;

class Day6Test {

	@Test
	void test() {
		String input = """
				....#.....
				.........#
				..........
				..#.......
				.......#..
				..........
				.#..^.....
				........#.
				#.........
				......#...
				""";
		Day6.Map map = Day6.Map.parse(input);
		Set<Point> visited = map.doRounds();
		assertThat(visited.size()).isEqualTo(41);
		assertThat(map.numberOfLoopCausingPositions()).isEqualTo(6);
	}

}
