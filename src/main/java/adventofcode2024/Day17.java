package adventofcode2024;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day17 {

	public static class Computer {

		private long a;
		private long b;
		private long c;
		List<Long> instructions;
		private int instructionPointer = 0;
		List<Long> output = new ArrayList<Long>();
		boolean traceExecution = false;

		void trace(String instruction, long operand) {
			if (traceExecution) {
				long literal = instructions.get(instructionPointer + 1);
				System.out.println("p=%2d a=%8x b=%8x c=%8x %s literal=%2x operand=%8x".formatted(instructionPointer, a,
						b, c, instruction, literal, operand));
			}
		}

		public Computer(long a, long b, long c, List<Long> instructions) {
			this.a = a;
			this.b = b;
			this.c = c;

			this.instructions = instructions;
		}

		void adv() {
			assert instructions.get(instructionPointer) == 0;
			long operand = evaluateCombo();
			trace("adv ", operand);
			a = Math.floorDiv(a, 1 << operand);
			instructionPointer += 2;
		}

		void bxl() {
			assert instructions.get(instructionPointer) == 1;
			long operand = literalOperand();
			trace("bxl ", operand);
			b = b ^ operand;
			instructionPointer += 2;
		}

		private Long literalOperand() {
			return instructions.get(instructionPointer + 1);
		}

		void bst() {
			assert instructions.get(instructionPointer) == 2;
			long operand = evaluateCombo() % 8;
			trace("bst ", operand);
			b = operand;
			instructionPointer += 2;
		}

		void jnz() {
			assert instructions.get(instructionPointer) == 3;
			if (a == 0) {
				trace("jnz ", 0);
				instructionPointer += 2;
			} else {
				long operand = literalOperand();
				trace("jnz ", operand);
				instructionPointer = (int) operand;
			}
		}

		void bxc() {
			assert instructions.get(instructionPointer) == 4;
			trace("bxc ", -1);
			b = b ^ c;
			instructionPointer += 2;
		}

		void out() {
			assert instructions.get(instructionPointer) == 5;
			long operand = evaluateCombo();
			trace("out ", operand);
			output.add(operand % 8);
			instructionPointer += 2;
		}

		void bdv() {
			assert instructions.get(instructionPointer) == 6;
			long operand = evaluateCombo();
			trace("bdv ", operand);
			b = Math.floorDiv(a, 1 << operand);
			instructionPointer += 2;
		}

		void cdv() {
			assert instructions.get(instructionPointer) == 6;
			long operand = evaluateCombo();
			trace("cdv ", operand);
			c = Math.floorDiv(a, 1 << operand);
			instructionPointer += 2;
		}

		private long evaluateCombo() {
			Long operand = instructions.get(instructionPointer + 1);
			if (operand <= 3)
				return operand;

			if (operand == 4)
				return a;
			if (operand == 5)
				return b;
			if (operand == 6)
				return c;

			throw new RuntimeException("Bad operand: " + operand);
		}

		List<Long> run() {
			while (instructionPointer < instructions.size()) {
				Long instruction = instructions.get(instructionPointer);
				// @formatter:off
				if (instruction == 0)		adv();
				else if (instruction == 1)	bxl();
				else if (instruction == 2)	bst();
				else if (instruction == 3)	jnz();
				else if (instruction == 4)	bxc();
				else if (instruction == 5)	out();
				else if (instruction == 6)	bdv();
				else if (instruction == 7)	cdv();
				else
					throw new RuntimeException("Bad instruction: " + instruction);
				// @formatter:on
			}
			return output;
		}

		public void dumpOn(PrintStream out) {
			for (int i = 0; i < instructions.size(); i += 2) {
				Long instruction = instructions.get(i);
				Long operand = instructions.get(i + 1);
				String operandString = switch (operand.intValue()) {
				case 0 -> "0";
				case 1 -> "1";
				case 2 -> "2";
				case 3 -> "3";
				case 4 -> "a";
				case 5 -> "b";
				case 6 -> "c";
				default -> "err";
				};

				//@formatter:off
				if (instruction == 0)		out.println("%2d adv a=a/2^%s".formatted(i, operandString));
				else if (instruction == 1)	out.println("%2d bxl b=b^%s".formatted(i, Integer.toBinaryString(operand.intValue())));
				else if (instruction == 2)	out.println("%2d bst b=%s%%8".formatted(i, operandString));
				else if (instruction == 3)	out.println("%2d jnz %d".formatted(i, operand));
				else if (instruction == 4)	out.println("%2d bxc b=b^c".formatted(i));
				else if (instruction == 5)	out.println("%2d out %s".formatted(i, operandString));
				else if (instruction == 6)	out.println("%2d bdv b=a/2^%s".formatted(i, operandString));
				else if (instruction == 7)	out.println("%2d cdv c=a/2^%s".formatted(i, operandString));
				//@formatter:on
			}
		}

		public static Computer parse(String input) {
			String[] registersAndProgram = input.split("\n\n");
			String[] registers = registersAndProgram[0].split("\n");
			assert registers[0].contains("Register A:");
			assert registers[1].contains("Register B:");
			assert registers[2].contains("Register C:");

			int a = Integer.parseInt(registers[0].split(":")[1].strip());
			int b = Integer.parseInt(registers[1].split(":")[1].strip());
			int c = Integer.parseInt(registers[2].split(":")[1].strip());
			List<Long> instructions = Arrays.stream(registersAndProgram[1].strip().split(" ")[1].split(","))
					.map(Long::parseLong).toList();
			return new Computer(a, b, c, instructions);
		}
	}

	public static void main(String[] args) {
		String input = new DataProtection().decryptDay(17);
		Computer computer = Computer.parse(input);
		System.out.println("Day 17 part 1: " + toCommaDelimitedString(computer.run()));
		long registerA = findRegisterAThatOutputsInstructions(0, 0, computer.instructions);
		System.out.println("Day 17 part 2: " + registerA);
	}

	private static long findRegisterAThatOutputsInstructions(long a, int aLen, List<Long> instructions) {
		// build up register a by appending octal digits that output the
		// tail end of the instructions list until we have an A register that
		// outputs the entire instruction list

		for (int i = 0; i <= 7; ++i) {
			long maybeA = a * 8 + i;
			var results = new Computer(maybeA, 0, 0, instructions).run();
			var instructionsSuffix = suffix(instructions, aLen + 1);

			// we're done
			if (results.equals(instructions))
				return maybeA;

			// we've found a possible partial solution. Recursively add octal
			// digits to see if we're on the right track
			if (results.equals(instructionsSuffix)) {
				long maybeAnswer = findRegisterAThatOutputsInstructions(maybeA, aLen + 1, instructions);
				if (maybeAnswer != Long.MAX_VALUE)
					return maybeAnswer;
			}
		}
		return Long.MAX_VALUE;
	}

	private static List<Long> suffix(List<Long> instructions, int n) {
		return instructions.stream().skip(instructions.size() - n).toList();
	}

	public static String toCommaDelimitedString(List<Long> results) {
		return String.join(",", results.stream().map(i -> i.toString()).toList());
	}

}
