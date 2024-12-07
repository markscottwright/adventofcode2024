package adventofcode2024;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.tuple.Pair;

public class Day7 {
	public static void main(String[] args)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		ArrayList<Pair<Long, List<Long>>> calibrations = Day7.parse(new DataProtection().decryptDay(7));
		System.out.println("day 7 part 1: " + sumOfValidEquations(calibrations, false));
		System.out.println("day 7 part 2: " + sumOfValidEquations(calibrations, true));
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

	public static ArrayList<Character> determineOperators(Long total, List<Long> operands, boolean allowConcat) {
		ArrayList<Character> maybeOperators = new ArrayList<Character>();
		determineOperators(total, operands, maybeOperators, allowConcat);
		return maybeOperators;
	}

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

	public static Long solve(List<Long> operands, ArrayList<Character> maybeOperators) {
		long total = operands.get(0);
		for (int i = 1; i < operands.size(); ++i) {
			if (i - 1 >= maybeOperators.size())
				return total;
			Character nextOperator = maybeOperators.get(i - 1);
			if (nextOperator == '+') {
				total += operands.get(i);
			} else if (nextOperator == '*') {
				total *= operands.get(i);
			} else if (nextOperator == '|') {
				long operand = operands.get(i);
				// Doing concat using math is much faster.  We determine the number of digits (log10(v)+1)
				// in the operand, multiply total by 10^number_of_digits and add the operand.
				total = total * ((long) Math.pow(10, ((long) Math.log10(operand)) + 1)) + operand;
				// total = Long.parseLong(Long.toString(total) + Long.toString(operands.get(i)));
			}
		}
		return total;
	}

	public static long sumOfValidEquations(ArrayList<Pair<Long, List<Long>>> calibrations, boolean allowConcat) {
		long total = 0;
		for (var calibration : calibrations) {
			ArrayList<Character> operators = determineOperators(calibration.getLeft(), calibration.getRight(),
					allowConcat);
			if (!operators.isEmpty())
				total += calibration.getLeft();
		}
		return total;
	}
}
