package adventofcode2024;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

public class Day5 {

	ArrayList<List<Integer>> pageUpdates = new ArrayList<>();
	HashSetValuedHashMap<Integer, Integer> pageToAfters = new HashSetValuedHashMap<Integer, Integer>();

	public static Day5 parse(String input) {
		Day5 day5 = new Day5();

		boolean inOrderingRules = true;
		for (String line : input.split("\n")) {
			if (line.isBlank())
				inOrderingRules = false;
			else if (inOrderingRules) {
				String[] beforeAndAfter = line.split("\\|");
				int before = Integer.parseInt(beforeAndAfter[0]);
				int after = Integer.parseInt(beforeAndAfter[1]);
				day5.pageToAfters.put(before, after);
			} else {
				day5.pageUpdates.add(Arrays.asList(line.split(",")).stream().map(Integer::parseInt).toList());
			}
		}

		return day5;
	}

	public boolean updateIsGood(List<Integer> update) {
		for (int i = 0; i < update.size(); i++) {
			Integer page = update.get(i);
			Set<Integer> afters = pageToAfters.get(page);
			
			// all pages after this one appear in this page's after set
			for (int j = i + 1; j < update.size(); ++j) {
				Integer shouldBeAfter = update.get(j);
				if (!afters.contains(shouldBeAfter)) {
					return false;
				}
			}
		}
		return true;
	}

	public static Integer middlePage(List<Integer> update) {
		assert update.size() % 2 == 1;
		return update.get(update.size() / 2);
	}

	public int sumOfGoodMiddlePages() {
		return pageUpdates.stream().filter(u -> updateIsGood(u)).mapToInt(Day5::middlePage).sum();
	}

	public int sumOfBadMiddlePages() {
		return pageUpdates.stream().filter(u -> !updateIsGood(u)).map(this::reorder).mapToInt(Day5::middlePage).sum();
	}

	private List<Integer> reorder(List<Integer> list) {
		ArrayList<Integer> out = new ArrayList<Integer>(list);
		// we can use our list of after pages as a Comparator::compare and sort the list
		out.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1.equals(o2))
					return 0;

				else if (pageToAfters.get(o1).contains(o2))
					return -1;
				else
					return 1;
			}
		});

		return out;
	}

	public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		DataProtection dataProtection = new DataProtection();
		Day5 day5 = Day5.parse(dataProtection.decryptDay(5));
		System.out.println("Day 5 part 1: " + day5.sumOfGoodMiddlePages());
		System.out.println("Day 5 part 2: " + day5.sumOfBadMiddlePages());
	}

}
