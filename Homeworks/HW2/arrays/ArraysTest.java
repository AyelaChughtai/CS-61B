package arrays;

import lists.IntList;
import lists.IntListList;
import org.junit.Test;
import static org.junit.Assert.*;

/** Testing methods in array class.
 *  @author Ayela
 */

public class ArraysTest {

    @Test
    public void catenateTest() {
        int[] testA = {1, 2, 3};
        int[] testB = {4, 5, 6};
        int[] testAB = {1, 2, 3, 4, 5, 6};
        assertArrayEquals (testAB , Arrays.catenate(testA , testB));
    }

    @Test
    public void testRemove() {
        int[] testA = {1, 2, 3, 4, 5};
        int[] resultA = {1, 2, 5};
        assertArrayEquals(resultA , Arrays.remove(testA, 2, 2));
    }

    @Test
    public void testNaturalRuns() {
        int[][] testResult = {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        int[] test = {1, 3, 7, 5, 4, 6, 9, 10, 10, 11};
        assertArrayEquals(testResult, Arrays.naturalRuns(test));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
