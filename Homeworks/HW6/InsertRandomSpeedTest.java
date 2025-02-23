import java.util.TreeSet;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import java.util.Scanner;

/** Performs a timing test on three different set implementations.
 *  @author Josh Hug
 */
public class InsertRandomSpeedTest {
    /** Returns time needed to put N random strings of length L into the
     * StringSet SS. */
    public static double insertRandomStrings(StringSet ss, int N, int L) {
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            String s = StringUtils.randomString(L);
            ss.put(s);
        }
        return sw.elapsedTime();
    }

    /** Returns time needed to put N random strings of length L into the
     * Set TS. */
    public static double insertRandomStrings(Set<String> ss, int N, int L) {
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; i++) {
            String s = StringUtils.randomString(L);
            ss.add(s);
        }
        return sw.elapsedTime();
    }

    /** Prints the result of a random timing test on a StringSet. */
    public static void printRandomTimingTest(StringSet s, int N) {
        System.out.printf("Inserting %d random length 10 Strings into a"
                        + " StringSet of type %s\n",
                N, s.getClass().getName());
        double runTime = insertRandomStrings(s, N, 10);
        System.out.printf("Took: %.2f sec.\n\n", runTime);
    }

    /** Prints the result of a random timing test on a StringSet. */
    public static void printRandomTimingTest(Set<String> s, int N) {
        System.out.printf("Inserting %d random length 10 Strings into a"
                        + " StringSet of type %s\n",
                N, s.getClass().getName());
        double runTime = insertRandomStrings(s, N, 10);
        System.out.printf("Took: %.2f sec.\n\n", runTime);
    }


    /** Requests user input and performs tests of three different set
     implementations. ARGS is unused. */
    public static void main(String[] args) throws IOException {
        int N;
        if (args.length > 0) {
            N = Integer.parseInt(args[0]);
            System.out.printf("Testing %d random 10-character strings.%n",
                    N);
        } else {
            Scanner input = new Scanner(System.in);
            System.out.println("This program inserts random length 10 strings"
                    + " into various string-set implementations.");
            System.out.print("\nEnter # strings to insert: ");
            N = input.nextInt();
        }
        printRandomTimingTest(new BSTStringSet(), N);
        printRandomTimingTest(new ECHashStringSet(), N);
        printRandomTimingTest(new TreeSet<String>(), N);
        printRandomTimingTest(new HashSet<String>(), N);

    }
}