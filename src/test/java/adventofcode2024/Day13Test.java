package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import adventofcode2024.Day13.Game;

class Day13Test {

	@Test
	void test() {
		String input = """
				Button A: X+94, Y+34
				Button B: X+22, Y+67
				Prize: X=8400, Y=5400

				Button A: X+26, Y+66
				Button B: X+67, Y+21
				Prize: X=12748, Y=12176

				Button A: X+17, Y+86
				Button B: X+84, Y+37
				Prize: X=7870, Y=6450

				Button A: X+69, Y+23
				Button B: X+27, Y+71
				Prize: X=18641, Y=10279
				""";
		
		List<Game> games = Day13.parse(input);
		assertThat(games.get(0).minTokens()).isEqualTo(280);
		assertThat(games.get(1).minTokens()).isEqualTo(Integer.MAX_VALUE);
		assertThat(games.get(2).minTokens()).isEqualTo(200);
		assertThat(games.get(3).minTokens()).isEqualTo(Integer.MAX_VALUE);

		System.out.println(games.get(0).minB());
		System.out.println(games.get(0).betterMinB());

		assertThat(Day13.Game.minTokensToWin(games)).isEqualTo(480);
		assertThat(Day13.Game.betterMinTokensToWin(games)).isEqualTo(480);
		
		List<Game> gamesWithOffset = Day13.offset(games);
		assertThat(Day13.Game.betterMinTokensToWin(gamesWithOffset)).isEqualTo(416082282239L);

	}

}
