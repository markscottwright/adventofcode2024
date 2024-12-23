package adventofcode2024;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 {

	public static class Game {

		private long aButtonX;
		private long aButtonY;
		private long bButtonX;
		private long bButtonY;
		private long prizeX;
		private long prizeY;

		public Game(long aButtonX, long aButtonY, long bButtonX, long bButtonY, long prizeX, long prizeY) {
			this.aButtonX = aButtonX;
			this.aButtonY = aButtonY;
			this.bButtonX = bButtonX;
			this.bButtonY = bButtonY;
			this.prizeX = prizeX;
			this.prizeY = prizeY;
		}

		@Override
		public String toString() {
			return "Game [aButtonX=" + aButtonX + ", aButtonY=" + aButtonY + ", bButtonX=" + bButtonX + ", bButtonY="
					+ bButtonY + ", prizeX=" + prizeX + ", prizeY=" + prizeY + "]";
		}

		public long minTokens() {
			// we're just brute forcing here
			long minTokens = Integer.MAX_VALUE;
			for (long a = 100; a >= 0; a--) {
				for (long b = 100; b >= 0; b--) {
					if (a * aButtonX + b * bButtonX == prizeX && a * aButtonY + b * bButtonY == prizeY) {
						if (a * 3 + b < minTokens) {
							minTokens = a * 3 + b;
						}
					}
				}
			}

			return minTokens;
		}

		/**
		 * @return tokens to win or Integer.MAX_VALUE if no solution
		 */
		public static long minTokensToWin(List<Game> games) {
			return games.stream().mapToLong(Game::minTokens).filter(t -> t != Integer.MAX_VALUE).sum();
		}

		/**
		 * @return tokens to win or Integer.MAX_VALUE if no solution
		 */
		public static long betterMinTokensToWin(List<Game> games) {
			return games.stream().mapToLong(Game::betterMinTokens).filter(t -> t != Integer.MAX_VALUE).sum();
		}

		public double minB() {
			/*
			 * Find B by substitution
			 * 
			 * aButtonX * a + bButtonX * b = prizeX aButtonY * a + bButtonY * b = prizeY
			 * 
			 * m = (aButtonX/aButtonY) multiply second equation aButtonX * a + m * b = m *
			 * prizeY
			 * 
			 * subtract 2nd eq from 1st (bButtonX - m*bButtonY) * b = prizeX - m*prizeY b =
			 * (prizeX - m*prizeY) / (bButtonX - m*bButtonY)
			 */
			double m = ((double) aButtonX) / aButtonY;
			return (((double) prizeX) - m * prizeY) / (((double) bButtonX) - m * bButtonY);
		}

		public double minA() {
			// aButtonX * a + bButtonX * minB() = prizeX
			// a = (prizeX - bButtonX * minB()) / aButtonX
			return (prizeX - bButtonX * minB()) / ((double) aButtonX);

		}

		public long betterMinTokens() {
			double doubleMinA = minA();
			double doubleMinB = minB();

			if (isInteger(doubleMinA) && isInteger(doubleMinB)) {
				return (3 * ((long) Math.rint(doubleMinA))) + ((long) Math.rint(doubleMinB));
			} else {
				return Integer.MAX_VALUE;
			}
		}

		private boolean isInteger(double value) {
			// needed to have a pretty large "iota" for this to work...  My numerics skills are
			// to rusty to know why off hand.
			boolean isInt = Math.abs(value - Math.rint(value)) < .001;
			return isInt;
		}
	}

	public static void main(String[] args) {
		String input = new DataProtection().decryptDay(13);
		List<Game> games = Day13.parse(input);
		System.out.println("Day 13 part 1: " + Game.minTokensToWin(games));
//		System.out.println("Day 13 part 1: " + Game.betterMinTokensToWin(games));
		System.out.println("Day 13 part 2: " + Game.betterMinTokensToWin(offset(games)));
	}

	public static List<Game> parse(String input) {
		var out = new ArrayList<Game>();
		for (String gameDescription : input.split("\n\n")) {
			String[] lines = gameDescription.split("\n");
			String buttonPattern = "Button .: X\\+([0-9]+), Y\\+([0-9]+)";
			Matcher matcher = Pattern.compile(buttonPattern).matcher(lines[0]);
			matcher.matches();
			long aButtonX = Integer.parseInt(matcher.group(1));
			long aButtonY = Integer.parseInt(matcher.group(2));
			matcher = Pattern.compile(buttonPattern).matcher(lines[1]);
			matcher.matches();
			long bButtonX = Integer.parseInt(matcher.group(1));
			long bButtonY = Integer.parseInt(matcher.group(2));

			String prizePattern = "Prize: X=([0-9]+), Y=([0-9]+)";
			matcher = Pattern.compile(prizePattern).matcher(lines[2]);
			matcher.matches();
			long prizeX = Integer.parseInt(matcher.group(1));
			long prizeY = Integer.parseInt(matcher.group(2));

			out.add(new Game(aButtonX, aButtonY, bButtonX, bButtonY, prizeX, prizeY));
		}
		return out;
	}

	public static List<Game> offset(List<Game> games) {
		return games.stream().map(g -> new Game(g.aButtonX, g.aButtonY, g.bButtonX, g.bButtonY,
				g.prizeX + 10000000000000L, g.prizeY + 10000000000000L)).toList();
	}

}
