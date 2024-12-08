package adventofcode2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class Day7 {
	public static void main(String[] args) {
		ArrayList<Pair<Long, List<Long>>> calibrations = Day7.parse(new DataProtection().decryptDay(7));
		System.out.println("day 7 part 1: " + sumOfValidEquations(calibrations, false));
		
//		long start = System.currentTimeMillis();
		System.out.println("day 7 part 2: " + sumOfValidEquations(calibrations, true));
//		System.out.println(System.currentTimeMillis() - start);
	}

	public static ArrayList<Pair<Long, List<Long>>> parse(String input) {
		var out = new ArrayList<Pair<Long, List<Long>>>();
		for (String line : input.split("\n")) {
			String[] fields = line.split("[: ]+");
			List<Long> operands = Arrays.stream(fields).skip(1).map(Long::parseLong).toList();
			Pair<Long, List<Long>> totalAndOperands = Pair.of(Long.parseLong(fields[0]), operands);
			out.add(totalAndOperands);
		}
		return out;
	}

	/**
	 * returns an empty array if no solution found
	 */
	public static ArrayList<Character> determineOperators(Long total, List<Long> operands, boolean allowConcat) {
		ArrayList<Character> maybeOperators = new ArrayList<Character>();
		determineOperators(total, operands, maybeOperators, allowConcat);
		return maybeOperators;
	}

	/**
	 * returns true if maybeOperators is the solution
	 */
	private static boolean determineOperators(Long total, List<Long> operands, ArrayList<Character> maybeOperators,
			boolean allowConcat) {
		Long solutionSoFar = solve(operands, maybeOperators);

		// too big? we'll never do better with maybeOperators
		if (solutionSoFar > total)
			return false;

		// fully picked operators? did we succeed?
		if (maybeOperators.size() == operands.size() - 1) {
			return solutionSoFar.equals(total);
		}

		maybeOperators.add('+');
		if (determineOperators(total, operands, maybeOperators, allowConcat))
			return true;
		maybeOperators.remove(maybeOperators.size() - 1);

		maybeOperators.add('*');
		if (determineOperators(total, operands, maybeOperators, allowConcat))
			return true;
		maybeOperators.remove(maybeOperators.size() - 1);

		if (allowConcat) {
			maybeOperators.add('|');
			if (determineOperators(total, operands, maybeOperators, allowConcat))
				return true;
			maybeOperators.remove(maybeOperators.size() - 1);
		}

		return false;
	}

	/**
	 * Solve the equation formed by inserting maybeOperators between operands.
	 * Assumes operands is not empty. If maybeOperators isn't long enough to use all
	 * operands, solves a partial equation.
	 */
	public static Long solve(List<Long> operands, ArrayList<Character> maybeOperators) {
		var operandsIterator = operands.iterator();
		var maybeOperatorsIterator = maybeOperators.iterator();
		long total = operandsIterator.next();
		while (operandsIterator.hasNext() && maybeOperatorsIterator.hasNext()) {
			Character nextOperator = maybeOperatorsIterator.next();
			Long operand = operandsIterator.next();
			if (nextOperator == '+') {
				total += operand;
			} else if (nextOperator == '*') {
				total *= operand;
			} else if (nextOperator == '|') {
				// Doing concat using math is much faster. We determine the number of digits
				// (log10(v)+1) in the operand, multiply total by 10^number_of_digits to "slide
				// it to the left" and add the operand.
				int numDigitsInOperand = ((int) Math.log10(operand)) + 1;
				total = total * tenToTheNth(numDigitsInOperand) + operand;
//				total = total * (long) Math.pow(10, numDigitsInOperand) + operand;
//				total = Long.parseLong(Long.toString(total) + Long.toString(operand));
			}
		}
		return total;
	}

	public static long sumOfValidEquations(ArrayList<Pair<Long, List<Long>>> calibrations, boolean allowConcat) {
		long total = 0;
		for (var calibration : calibrations) {
			var operators = determineOperators(calibration.getLeft(), calibration.getRight(), allowConcat);
			if (!operators.isEmpty())
				total += calibration.getLeft();
		}
		return total;
	}

	public static long tenToTheNth(int exponent) {
		// is this faster than using doubles? In theory, I should be doing this by
		// doubling rather than iteration
		long out = 10;
		for (int i = exponent - 1; i > 0; i--)
			out *= 10;
		return out;
	}
}
