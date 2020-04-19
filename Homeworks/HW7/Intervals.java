import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/** HW #7, Sorting ranges.
 *  @author Ayela Chughtai
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        int out = 0;
        if (intervals.size() == 0) {
            return out;
        } else {
            intervals.sort(Comparator.comparing(arr -> arr[0]));
            int first = intervals.get(0)[0];
            int second = intervals.get(0)[1];
            for (int i = 0; i < intervals.size(); i++) {
                if (intervals.get(i)[0] > second) {
                    out += (second - first);
                    first = intervals.get(i)[0];
                    second = intervals.get(i)[1];
                } else if (intervals.get(i)[1] > second) {
                    second = intervals.get(i)[1];
                }
            }
            out += (second - first);
        }
        return out;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
//I google searched how to sort a list like intervals in a specific way (I wanted to do it according to the first digit of each array)
// and it took me to sort oracle documentation about .sort, which I have used here