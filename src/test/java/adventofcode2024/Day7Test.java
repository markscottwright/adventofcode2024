package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

class Day7Test {

	@Test
	void test() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		String input = """
				190: 10 19
				3267: 81 40 27
				83: 17 5
				156: 15 6
				7290: 6 8 6 15
				161011: 16 10 13
				192: 17 8 14
				21037: 9 7 18 13
				292: 11 6 16 20
				""";
		ArrayList<Pair<Long, List<Long>>> calibrations = Day7.parse(input);
		assertThat(Day7.sumOfValidEquations(calibrations, false)).isEqualTo(3749);
		assertThat(Day7.sumOfValidEquations(calibrations, true)).isEqualTo(11387);
	}

	@Test
	void testSolve() throws Exception {

		assertThat(Day7.solve(List.of(1L,2L), new ArrayList<Character>())).isEqualTo(1L);
		assertThat(Day7.solve(List.of(1L,2L), new ArrayList<Character>())).isEqualTo(1L);

		assertThat(Day7.solve(List.of(1L,2L), new ArrayList<Character>(List.of('+')))).isEqualTo(3L);
		assertThat(Day7.solve(List.of(1L,2L), new ArrayList<Character>(List.of('*')))).isEqualTo(2L);

		assertThat(Day7.solve(List.of(1L,2L,3L), new ArrayList<Character>(List.of('+')))).isEqualTo(3L);
		assertThat(Day7.solve(List.of(1L,2L,3L), new ArrayList<Character>(List.of('*')))).isEqualTo(2L);

		assertThat(Day7.solve(List.of(1L,2L,4L), new ArrayList<Character>(List.of('+', '+')))).isEqualTo(7L);
		assertThat(Day7.solve(List.of(1L,2L,4L), new ArrayList<Character>(List.of('*', '*')))).isEqualTo(8L);

	}

}
