package adventofcode2024;

import org.junit.jupiter.api.Test;

import adventofcode2024.Day11.Stones;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class Day11Test {

    @Test
    void test() {
        Stones stones = Day11.Stones.parse("0 1 10 99 999");
        stones = stones.blink();
        assertThat(stones.count()).isEqualTo(7);
        
        stones = Day11.Stones.parse("125 17");
        stones = stones.blink();
        assertThat(stones.count()).isEqualTo(3);
        stones = stones.blink();
        assertThat(stones.count()).isEqualTo(4);
        stones = stones.blink();
        assertThat(stones.count()).isEqualTo(5);
        stones = stones.blink();
        assertThat(stones.count()).isEqualTo(9);

        stones = Day11.Stones.parse("125 17");
        for (int i=0; i < 25; ++i)
            stones = stones.blink();
        assertThat(stones.count()).isEqualTo(55312);
        
    }

}
