package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Day17Test {

	@Test
	void test() {
		String input = """
				Register A: 729
				Register B: 0
				Register C: 0
				
				Program: 0,1,5,4,3,0
				""";
		Day17.Computer computer = Day17.Computer.parse(input);
		var results = computer.run();
		assertThat(Day17.toCommaDelimitedString(results)).isEqualTo("4,6,3,5,6,3,5,2,1,0");
	}
	
}
