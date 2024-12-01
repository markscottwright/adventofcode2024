package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class Day1Test {

	private static final String TEST_INPUT = """
			3   4
			4   3
			2   5
			1   3
			3   9
			3   3""";

	@Test
	void testPart1() {
		var day1 = Day1.parse(TEST_INPUT);
		long totalDistance = day1.totalDistance();
		assertThat(11).isEqualTo(totalDistance);
	}

	@Test
	void testPart2() {
		var day1 = Day1.parse(TEST_INPUT);
		long simularityScore = day1.simularityScore();
		assertThat(31).isEqualTo(simularityScore);
	}
}
