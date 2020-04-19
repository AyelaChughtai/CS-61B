import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i++) {
                for (int j = i; j > 0 ; j--) {
                    if (array[j - 1] > array[j]) {
                        swap(array, j - 1, j);
                    } else {
                        break;
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            k = Math.min(k, array.length);
            int minAt = 0;
            for (int i = 0; i < k; i++) {
                minAt = i;
                for (int j = i + 1; j < k; j++) {
                    if (array[minAt] > array[j]) {
                        minAt = j;
                    }
                }
                swap(array, i, minAt);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (k < 2) {
                return;
            } else {
                int mid = k / 2;
                int[] left = new int[mid];
                int[] right = new int[k - mid];
                for (int i = 0; i < mid; i++) {
                    left[i] = array[i];
                }
                for (int i = mid; i < k; i++) {
                    right[i - mid] = array[i];
                }
                sort(left, mid);
                sort(right, k - mid);
                merge(array, left, right, mid, k - mid);
            }
        }
        public static void merge(int[] array, int[] l, int[] r, int left, int right) {
            int i = 0;
            int j = 0;
            int k = 0;
            while (i < left && j < right) {
                if (l[i] < r[j]) {
                    array[k] = l[i];
                    k++;
                    i++;
                } else {
                    array[k] = r[j];
                    k++;
                    j++;
                }
            }
            while (i < left) {
                array[k] = l[i];
                k++;
                i++;
            }
            while (j < right) {
                array[k] = r[j];
                k++;
                j++;
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int[] arr = new int[k];
            System.arraycopy(a, 0, arr, 0, k);
            Queue[] buckets = new Queue[10];
            for (int i = 0; i < 10; i++) {
                buckets[i] = new LinkedList<>();
            }
            boolean sorted = false;
            int exp = 1;
            while (!sorted) {
                sorted = true;
                for (int item: arr) {
                    int bucket = (item / exp) % 10;
                    if (bucket > 0) {
                        sorted = false;
                    }
                    buckets[bucket].add(item);
                }
                exp *= 10;
                int index = 0;
                for (Queue bucket: buckets) {
                    while (!bucket.isEmpty()) {
                        arr[index] = (int) bucket.remove();
                        index++;
                    }
                }
            }
            System.arraycopy(arr, 0, a, 0, k);
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
//I used youtube videos to help me understand and be able to write mergesort and lsdsort.
// In specific, they helped me figure out how to use Queues and linked lists instead of arrays
// (my initial code using ArrayList and newArrayList but it was failing and I'm still not sure why
// but the video I watched explained a similar process using different
// data structures). For mergeSort, I used it to help figure out the conditions in my helper function.