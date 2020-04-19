import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] list = {{1,3,4}, {1}, {5,6,7,8}, {7,9}};
        assertEquals(MultiArr.maxValue(list), 9);
    }

    @Test
    public void testAllRowSums() {
        int[][] list = {{1,3,4}, {1}, {5,6,7,8}, {7,9}};
        int[] result = {8, 1, 26, 16};
        assertArrayEquals(MultiArr.allRowSums(list), result);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
