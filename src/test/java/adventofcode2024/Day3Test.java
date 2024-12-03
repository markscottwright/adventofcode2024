package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

class Day3Test {

	@Test
	void test() {
		String input = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";
		List<Pair<Integer, Integer>> multiplications = Day3.parse(input);
		assertThat(161).isEqualTo(Day3.sumOfMultiplications(multiplications));
	}

}
