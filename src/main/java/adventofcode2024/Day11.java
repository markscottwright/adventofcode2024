package adventofcode2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.lang3.tuple.Pair;

public class Day11 {
    public static class Stones {

        private List<Long> stones;

        @Override
        public String toString() {
            return "Stones [stones=" + stones + "]";
        }

        public Stones(List<Long> stones) {
            this.stones = stones;
        }

        public static Stones parse(String string) {
            List<Long> stones = Arrays.stream(string.split("\\s+")).map(Long::parseLong).toList();
            return new Stones(stones);
        }

        public Stones blink() {
            ArrayList<Long> nextStones = new ArrayList<Long>();
            for (Long stone : stones) {
                if (stone == 0)
                    nextStones.add(1L);
                else if (numDigits(stone) % 2 == 0) {
                    long divisor = (long) Math.pow(10, numDigits(stone) / 2);
                    long leftDigits = stone / divisor;
                    long rightDigits = stone % divisor;
                    nextStones.add(leftDigits);
                    nextStones.add(rightDigits);
                } else {
                    nextStones.add(stone * 2024);
                }
            }
            return new Stones(nextStones);
        }

        public long count(long number, int times, HashMap<Pair<Long, Integer>, Long> cache) {
            if (times == 0)
                return 1L;
            else if (cache.containsKey(Pair.of(number, times)))
                return cache.get(Pair.of(number, times));
            else if (number == 0) {
                long value = count(1L, times - 1, cache);
                cache.put(Pair.of(number, times), value);
                return value;
            } else if (numDigits(number) % 2 == 0) {
                long divisor = (long) Math.pow(10, numDigits(number) / 2);
                long leftDigits = number / divisor;
                long rightDigits = number % divisor;
                long value = count(leftDigits, times - 1, cache) + count(rightDigits, times - 1, cache);
                cache.put(Pair.of(number, times), value);
                return value;
            } else {
                long value = count(number * 2024, times - 1, cache);
                cache.put(Pair.of(number, times), value);
                return value;
            }
        }

        public long sumAfterBlinks(int times) {
            var cache = new HashMap<Pair<Long, Integer>, Long>();
            long sum = stones.stream().mapToLong(s -> count(s, times, cache)).sum();
            return sum;
        }

        public int count() {
            return stones.size();
        }

    }

    public static void main(String[] args) {
        Stones stones = Stones.parse(new DataProtection().decryptDay(11));
        var originalStones =stones;
        for (int i = 0; i < 25; i++) {
            stones = stones.blink();
        }
        System.out.println("Day 11 part 1: " + stones.count());
        // System.out.println("Day 11 part 1: " + originalStones.sumAfterBlinks(25));
        System.out.println("Day 11 part 2: " + originalStones.sumAfterBlinks(75));
    }

    static public int numDigits(long num) {
        return ((int) Math.log10(num)) + 1;
    }
}
