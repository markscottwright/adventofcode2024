package adventofcode2024;

public class Day9 {

	public static class Checksum {
		int pos = 0;
		public long checksum = 0;

		public void add(int fileNumber) {
			checksum += fileNumber * pos;
			pos++;
		}

	}

	public static class ReverseBlocksReader {

		private String map;
		private int pos;
		private int fileNumber;
		private int blocks;

		public ReverseBlocksReader(String map) {
			this.map = map;
			pos = map.length() - 1;
			if (Day9.isEven(map.length()))
				pos--;
			fileNumber = numFiles(map) - 1;
			blocks = map.charAt(pos) - '0';
		}

		public int nextFileNum() {
			if (blocks == 0) {
				pos -= 2;
				blocks = map.charAt(pos) - '0';
				fileNumber--;
			}
			blocks--;
			return fileNumber;
		}

	}

	static int numFiles(String input) {
		return input.length() / 2 + input.length() % 2;
	}

	public static void main(String[] args) {
		String input = new DataProtection().decryptDay(9);
		System.out.println("Day 9 part 1: " + reorderFileSystem(input).checksum);
	}

	public static Checksum reorderFileSystem(String input) {
		var reverser = new ReverseBlocksReader(input);
		int pos = 0;
		int frontFileNumber = 0;
		long totalBlocks = Day9.numFileBlocks(input);
		int blocksOut = 0;
		var checksum = new Checksum();

		while (blocksOut < totalBlocks) {
			int blocks = input.charAt(pos) - '0';
			if (Day9.isEven(pos)) {
				for (int b = 0; (b < blocks) && (blocksOut < totalBlocks); b++, blocksOut++) {
					checksum.add(frontFileNumber);
				}
				frontFileNumber++;
			} else {
				for (int b = 0; (b < blocks) && (blocksOut < totalBlocks); b++, blocksOut++) {
					int rearFileNumber = reverser.nextFileNum();
					checksum.add(rearFileNumber);
				}
			}
			pos++;
		}
		return checksum;
	}

	public static int numFileBlocks(String input) {
		int total = 0;
		for (int i = 0; i < input.length(); i += 2) {
			total += (input.charAt(i) - '0');
		}
		return total;
	}

	public static int numBlocks(String input) {
		int total = 0;
		for (int i = 0; i < input.length(); i ++) {
			total += (input.charAt(i) - '0');
		}
		return total;
	}

	public static boolean isEven(int n) {
		return n % 2 == 0;
	}

}
