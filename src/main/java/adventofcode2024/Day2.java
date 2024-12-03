package adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {
	public static class Report {

		@Override
		public String toString() {
			return "Report [levels=" + levels + "]";
		}

		private List<Integer> levels;

		public Report(List<Integer> levels) {
			this.levels = levels;
		}

		boolean isSafe() {
			boolean isRising = levels.get(1) > levels.get(0);
			for (int i = 1; i < levels.size(); ++i) {
				int diff = levels.get(i) - levels.get(i - 1);
				if (isRising) {
					if (diff < 1 || diff > 3)
						return false;
				} else {
					if (diff > -1 || diff < -3)
						return false;
				}
			}
			return true;
		}

		boolean isSafeWithProblemDamper(boolean isRising, int skipPosition) {
			int start = 1;
			if (skipPosition == 0)
				start = 2;
			int end = levels.size();

			// no known bad position, let the last one be bad
			if (skipPosition < 0)
				end--;

			for (int i = start; i < end; ++i) {
				if (i == skipPosition)
					continue;

				int diff = (skipPosition == i - 1) ? levels.get(i) - levels.get(i - 2)
						: levels.get(i) - levels.get(i - 1);
				if (isRising) {
					if (diff < 1 || diff > 3) {
						if (skipPosition >= 0) {
							return false;
						} else {
							// there's something wrong with where we are - try skipping current position and previous position,
							return isSafeWithProblemDamper(isRising, i) || isSafeWithProblemDamper(isRising, i - 1);
						}
					}
				} else {
					if (diff > -1 || diff < -3) {
						if (skipPosition >= 0) {
							return false;
						} else {
							// there's something wrong with where we are - try skipping current position and previous position,
							return isSafeWithProblemDamper(isRising, i) || isSafeWithProblemDamper(isRising, i - 1);
						}
					}
				}
			}
			return true;
		}

		boolean isSafeWithProblemDamper() {
			return isSafeWithProblemDamper(true, -1) || isSafeWithProblemDamper(false, -1);
		}
	}

	public static void main(String[] args) throws IOException {
		List<Report> reports = Day2.parse(Files.readString(Path.of("data/day2.dat")));
		System.out.println("day 2 part 1: " + reports.stream().filter(Report::isSafe).count());
		System.out.println("day 2 part 1: " + reports.stream().filter(Report::isSafeWithProblemDamper).count());
	}

	public static List<Report> parse(String input) {
		ArrayList<Report> reports = new ArrayList<Report>();
		for (String line : input.split("\\n+")) {
			Report report = new Report(Arrays.stream(line.split("\\s+")).map(Integer::parseInt).toList());
			reports.add(report);
		}
		return reports;
	}
}
