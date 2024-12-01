package adventofcode2025;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Day1Test<E> {

	private static final String TEST_INPUT = """
			3   4
			4   3
			2   5
			1   3
			3   9
			3   3""";

	@Test
	void testPart1() {
		var inputData = Day1.parse(TEST_INPUT);
		long totalDistance = Day1.totalDistance(inputData.getLeft(), inputData.getRight());
		assertThat(11).isEqualTo(totalDistance);
	}

	@Test
	void testPart2() {
		var inputData = Day1.parse(TEST_INPUT);
		long simularityScore = Day1.simularityScore(inputData.getLeft(), inputData.getRight());
		assertThat(31).isEqualTo(simularityScore);
	}
}
