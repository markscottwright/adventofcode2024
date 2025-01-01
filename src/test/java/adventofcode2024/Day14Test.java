package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import adventofcode2024.Day14.Robots;

class Day14Test {

	@Test
	void testMoving() throws Exception {
		String input = """
				p=2,4 v=2,-3
				""";
		Robots originalRobots = Day14.Robots.parse(11, 7, input);
		originalRobots.printOn(System.out);
		System.out.println();
		originalRobots.moveForSeconds(1).printOn(System.out);
		System.out.println();
		Robots moved = originalRobots.moveForSeconds(4);
		moved.printOn(System.out);
		System.out.println(moved.robots);
	}

	@Test
	void test() {
		String input = """
				p=0,4 v=3,-3
				p=6,3 v=-1,-3
				p=10,3 v=-1,2
				p=2,0 v=2,-1
				p=0,0 v=1,3
				p=3,0 v=-2,-2
				p=7,6 v=-1,-3
				p=3,0 v=-1,-2
				p=9,3 v=2,3
				p=7,3 v=-1,2
				p=2,4 v=2,-3
				p=9,5 v=-3,-3
				""";
		Day14.Robots robots = Day14.Robots.parse(11, 7, input);
		Day14.Robots robotsAfter100Seconds = robots.moveForSeconds(100);
		assertThat(robotsAfter100Seconds.safetyScore()).isEqualTo(12);
	}

}
