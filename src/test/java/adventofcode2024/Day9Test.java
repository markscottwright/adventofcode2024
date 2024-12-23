package adventofcode2024;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

import org.junit.jupiter.api.Test;

public class Day9Test {

	public static class Blocks {

		@Override
		public int hashCode() {
			return Objects.hash(fileNumber);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Blocks other = (Blocks) obj;
			return fileNumber == other.fileNumber;
		}

		private int size;
		private boolean isFree;
		private int fileNumber = -1;

		public Blocks(int size, int fileNumber) {
			this.size = size;
			this.fileNumber = fileNumber;
			this.isFree = false;
		}

		public Blocks(int size) {
			this.size = size;
			this.isFree = true;
		}

		public boolean fitsIn(Blocks other) {
			return other.isFree && size < other.size;
		}

	}

	@Test
	void test() {
		String input = "2333133121414131402";

		assertThat(9).isEqualTo(Day9.numFileBlocks("12345"));

		var checksum = Day9.reorderFileSystem(input);
		assertThat(checksum.checksum).isEqualTo(1928);
	}

//	@Test
//	void testName() throws Exception {
//		String input = "2333133121414131402";
//		var filesystem = new ArrayList<Integer>();
//		int fileNumber = 0;
//		for (int i = 0; i < input.toCharArray().length; i++) {
//			char c = input.toCharArray()[i];
//			if (isEven(i))
//				filesystem.add(new Blocks(c - '0', fileNumber++));
//			else
//				filesystem.add(new Blocks(c - '0'));
//		}
//
//		int pos = filesystem.size()-1;
//		if (filesystem.get(pos).isFree)
//			pos--;
//		while (pos > 0) {
//			for (int potentialNewPos = 0; potentialNewPos < pos; ++potentialNewPos) {
//				if (filesystem.get(pos).fitsIn(filesystem.get(potentialNewPos))) {
//					
//				}
//			}
//		}
//	}

}
