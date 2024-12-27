package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import adventofcode2024.Day15.Warehouse;
import adventofcode2024.Day15.Warehouse2;

class Day15Test {

	@Test
	void test() {
		String input = """
				########
				#..O.O.#
				##@.O..#
				#...O..#
				#.#.O..#
				#...O..#
				#......#
				########

				<^^>>>vv<v>>v<<
				""";
		Day15.Warehouse warehouse = Day15.Warehouse.parse(input);
		warehouse.moveRobot();
		assertThat(warehouse.boxGpsCoordinateSum()).isEqualTo(2028);
	}

	@Test
	void testLarger() throws Exception {
		String input = """
				##########
				#..O..O.O#
				#......O.#
				#.OO..O.O#
				#..O@..O.#
				#O#..O...#
				#O..O..O.#
				#.OO.O.OO#
				#....O...#
				##########

				<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
				vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
				><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
				<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
				^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
				^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
				>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
				<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
				^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
				v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
				""";
		Warehouse warehouse = Warehouse.parse(input);
		warehouse.moveRobot();
		assertThat(warehouse.boxGpsCoordinateSum()).isEqualTo(10092);
	}

	@Test
	void testPart2() throws Exception {
		var input = """
				#######
				#...#.#
				#.....#
				#..OO@#
				#..O..#
				#.....#
				#######

				<vv<<^^<<^^
				""";
		Warehouse2 warehouse = Warehouse2.parse(input);
		warehouse.printOn(System.out);
		warehouse.moveRobot(Direction.w);
		warehouse.printOn(System.out);
		warehouse.moveRobot(Direction.s);
		warehouse.moveRobot(Direction.s);
		warehouse.moveRobot(Direction.w);
		warehouse.moveRobot(Direction.w);
		warehouse.moveRobot(Direction.n);
		warehouse.printOn(System.out);
	}

	@Test
	void testLargerPart2() throws Exception {
		String input = """
				##########
				#..O..O.O#
				#......O.#
				#.OO..O.O#
				#..O@..O.#
				#O#..O...#
				#O..O..O.#
				#.OO.O.OO#
				#....O...#
				##########

				<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
				vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
				><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
				<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
				^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
				^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
				>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
				<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
				^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
				v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
				""";
		Warehouse2 warehouse = Warehouse2.parse(input);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		warehouse.printOn(new PrintStream(out));
		String expected = """
				####################
				##....[]....[]..[]##
				##............[]..##
				##..[][]....[]..[]##
				##....[]@.....[]..##
				##[]##....[]......##
				##[]....[]....[]..##
				##..[][]..[]..[][]##
				##........[]......##
				####################
				""".strip();
		assertThat(out.toString().replaceAll("\r", "").strip()).isEqualTo(expected);
		warehouse.moveRobot();
		assertThat(warehouse.boxGpsCoordinateSum()).isEqualTo(9021);
		
		String expectedAfter = """
				####################
				##[].......[].[][]##
				##[]...........[].##
				##[]........[][][]##
				##[]......[]....[]##
				##..##......[]....##
				##..[]............##
				##..@......[].[][]##
				##......[][]..[]..##
				####################
				""";
		ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		warehouse.printOn(new PrintStream(out2));
		assertThat(out2.toString().replaceAll("\r", "").strip()).isEqualTo(expectedAfter.strip());
	}
}
