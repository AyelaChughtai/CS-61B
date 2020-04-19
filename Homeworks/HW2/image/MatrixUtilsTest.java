package image;

import org.junit.Test;
import static org.junit.Assert.*;

/** Testing for MatrixUtils class
 *  @author Ayela
 */

public class MatrixUtilsTest {
    @Test
    public void testaccumulateVertical () {
        double[][] testInput =
        {{1000000,   1000000,   1000000,   1000000},
        {1000000,     75990,     30003,   1000000},
        {1000000,     30002,    103046,   1000000},
        {1000000,     29515,     38273,   1000000},
        {1000000,     73403,     35399,   1000000},
        {1000000,   1000000,   1000000,   1000000}};

        double[][] testOutput =
        {{1000000,   1000000,   1000000,   1000000},
        {2000000,   1075990,   1030003,   2000000},
        {2075990,   1060005,   1133049,   2030003},
        {2060005,   1089520,   1098278,   2133049},
        {2089520,   1162923,   1124919,   2098278},
        {2162923,   2124919,   2124919,   2124919}};

        assertArrayEquals(testOutput, MatrixUtils.accumulateVertical(testInput));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
