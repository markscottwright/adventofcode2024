package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Day16Test {

	@Test
	void test() {
		String input = """
				###############
				#.......#....E#
				#.#.###.#.###.#
				#.....#.#...#.#
				#.###.#####.#.#
				#.#.#.......#.#
				#.#.#####.###.#
				#...........#.#
				###.#.#####.#.#
				#...#.....#.#.#
				#.#.#.###.#.#.#
				#.....#...#.#.#
				#.###.#.#.#.#.#
				#S..#.....#...#
				###############
				""";
		Day16.Maze maze = Day16.Maze.parse(input);
		assertThat(maze.shortestPathScore()).isEqualTo(7036);
		for (var path : maze.getSolutionPaths()) {
			System.out.println(path);
		}
	}

	@Test
	void test2() {
		String input = """
				#################
				#...#...#...#..E#
				#.#.#.#.#.#.#.#.#
				#.#.#.#...#...#.#
				#.#.#.#.###.#.#.#
				#...#.#.#.....#.#
				#.#.#.#.#.#####.#
				#.#...#.#.#.....#
				#.#.#####.#.###.#
				#.#.#.......#...#
				#.#.###.#####.###
				#.#.#...#.....#.#
				#.#.#.#####.###.#
				#.#.#.........#.#
				#.#.#.#########.#
				#S#.............#
				#################
				""";
		Day16.Maze maze = Day16.Maze.parse(input);
		assertThat(maze.shortestPathScore()).isEqualTo(11048);
		assertThat(maze.pointsInAllSolutions()).isEqualTo(64);
	}
}
