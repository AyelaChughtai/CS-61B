package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** Testing methods in class Lists
 *  @author Ayela
 */

public class ListsTest {
    /** Tests naturalRuns method*/
    @Test
    public void TestNaturalRuns() {
        int[][] test = {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        IntListList testList = IntListList.list(test);
        assertEquals(testList, Lists.naturalRuns(IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11)));
    }

        // It might initially seem daunting to try to set up
        // IntListList expected.
        //
        // There is an easy way to get the IntListList that you want in just
        // few lines of code! Make note of the IntListList.list method that
        // takes as input a 2D array.

        public static void main (String[] args){
            System.exit(ucb.junit.textui.runClasses(ListsTest.class));
        }
    }