package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

import lists.IntList;
import lists.IntListList;

/** Array utilities.
 *  @author Ayela
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int totalLength = A.length + B.length;
        int[] newList = new int[totalLength];
        for (int i = 0; i < A.length ; i++) {
            newList[i] = A[i];
            for (int j = 0; j < B.length; j++) {
                newList[j + A.length] = B[j];
            }
        }
        return newList;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        int[] newList= new int[A.length - len];
        for (int i= 0; i < (A.length - len); i++){
            if (i < start) {
                newList[i]= A[i];
            } else {
                newList[i]= A[i+len];;
            }
        }
        return newList;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {

        int[] newA = new int[A.length];
        int index = 0;
        for (int i = 0; i < A.length-1; i++) {
            if (A[i+1] <= A[i]) {
                newA[index] = i;
                index += 1;
            }
        }

        int[][] result = new int[A.length + 3][];
        int[] newATemp = new int[index];
        System.arraycopy(newA, 0, newATemp, 0, index);
        newA = newATemp;
        int index2 = 0;
        for (int j = 0; j < newA.length; j++) {
            if (j == 0) {
                int[] temp = new int[newA[0] + 1];
                System.arraycopy(A, 0, temp, 0, newA[0] + 1);
                result[0] = temp;
                index2 += 1;
            } else {
                int[] temp = new int[newA[j] - newA[j-1]];
                System.arraycopy(A, newA[j-1] + 1, temp, 0, (newA[j] - newA[j-1]));
                result[index2] = temp;
                index2 += 1;
            }

        }
        int[] temp = new int[A.length - newA[newA.length - 1] - 1];
        System.arraycopy(A, newA[newA.length-1] + 1, temp, 0, (A.length - newA[newA.length - 1] - 1));
        result[index2] = temp;
        index2 += 1;
        int[][] resultTemp = new int[index2][];
        System.arraycopy(result, 0, resultTemp, 0, index2);
        result = resultTemp;

        return result;
    }
}