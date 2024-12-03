package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import adventofcode2024.Day2.Report;

class Day2Test {
	String INPUT = """
			7 6 4 2 1
			1 2 7 8 9
			9 7 6 2 1
			1 3 2 4 5
			8 6 4 4 1
			1 3 6 7 9
			""";

	@Test
	void testDay1() {
		List<Report> reports = Day2.parse(INPUT);
		long numSafe = reports.stream().filter(Report::isSafe).count();
		assertThat(numSafe).isEqualTo(2);
	}

	@Test
	void testDay2() {
		List<Report> reports = Day2.parse(INPUT);
		long numSafe = reports.stream().filter(Report::isSafeWithProblemDamper).count();
		assertThat(numSafe).isEqualTo(4);
	}
}
