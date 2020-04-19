package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Ayela Chughtai
 */
public class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     *
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    Permutation getNewPermutation(String cycles, Alphabet alphabet) {
        Permutation permutation = new Permutation(cycles, alphabet);
        return permutation;
    }

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     *
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    Alphabet getNewAlphabet(String chars) {
        Alphabet alphabet = new Alphabet(chars);
        return alphabet;
    }

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     *
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    Alphabet getNewAlphabet() {
        return new Alphabet();
    }

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /**
     * Check that PERM has an ALPHABET whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }
    @Test
    public void testAlphabet() {
        Alphabet alpha = getNewAlphabet();
        Permutation p = getNewPermutation("(BACD)      (Z)",
                getNewAlphabet("ABCDEZ"));
        assertTrue(alpha.contains('A'));
        assertFalse(getNewAlphabet("ABCDEZk").contains('K'));
        assertFalse(getNewAlphabet("BCDEZ").contains('F'));
    }
    @Test
    public void testInvertChar() {
        Permutation p = getNewPermutation("(BACD)      (Z)",
                getNewAlphabet("ABCDEZ"));
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));
        assertEquals('C', p.invert('D'));
        assertEquals('A', p.invert('C'));
        assertEquals('Z', p.invert('Z'));
        assertEquals('E', p.invert('E'));
    }

    @Test
    public void testSize() {
        Permutation p = getNewPermutation("(BACD)",
                getNewAlphabet("ABCD"));
        Permutation q = getNewPermutation("(BACD)",
                getNewAlphabet("ABCDEFGH"));
        Permutation r = getNewPermutation("(BA  CD)     (YLZ) (F)",
                getNewAlphabet());
        assertEquals(4, p.size());
        assertEquals(8, q.size());
        assertEquals(26, r.size());
        assertEquals(26, getNewAlphabet().size());
    }

    @Test
    public void testPermuteInt() {
        Permutation p = getNewPermutation("(BACD) (E)",
                getNewAlphabet("ABCDEZ"));
        assertEquals(2, p.permute(0));
        assertEquals(0, p.permute(1));
        assertEquals(3, p.permute(2));
        assertEquals(1, p.permute(3));
        assertEquals(-1 + p.size(), p.permute(-1));
        assertEquals(4, p.permute(4));
        assertEquals(5, p.permute(5));
        assertEquals(2, p.permute(30));
    }

    @Test
    public void testInvertInt() {
        Permutation p = getNewPermutation("(BACD)   (E    )",
                getNewAlphabet("ABCDEZ"));
        assertEquals(1, p.invert(0));
        assertEquals(3, p.invert(1));
        assertEquals(2, p.invert(3));
        assertEquals(0, p.invert(2));
        assertEquals(4, p.invert(4));
        assertEquals(-1 + p.size(), p.invert(-1));
        assertEquals(1, p.invert(30));
    }
    @Test
    public void testPermuteChar() {
        Permutation p = getNewPermutation("(BACD)  (E)",
                getNewAlphabet("ABCDEZ"));
        assertEquals('C', p.permute('A'));
        assertEquals('A', p.permute('B'));
        assertEquals('D', p.permute('C'));
        assertEquals('B', p.permute('D'));
        assertEquals('E', p.permute('E'));
        assertEquals('Z', p.permute('Z'));

    }
    @Test
    public void testDerangement() {
        Permutation p = getNewPermutation("(BACD) (E)",
                getNewAlphabet("ABCDEZ"));
        Permutation q = getNewPermutation("",
                getNewAlphabet("ABCDEZ"));
        Permutation r = getNewPermutation("(BACD   E)",
                getNewAlphabet("ABCDE"));
        assertFalse(p.derangement());
        assertFalse(q.derangement());
        assertTrue(r.derangement());
    }
    @Test (expected = EnigmaException.class)
    public void testNotInAlphabetPlusOtherStuff() {
        Permutation p = getNewPermutation("(BACD) (E)",
                getNewAlphabet("ABCDEZ"));
        p.invert('F');
        p.permute('F');
        getNewAlphabet("ABCDEZ").contains('F');
        getNewPermutation("(BACD) (F)", getNewAlphabet("ABCDEZ"));
        getNewPermutation("(BACD) (ED)", getNewAlphabet("ABCDEZ"));
        getNewPermutation("BACD) (E)", getNewAlphabet("ABCDEZ"));
        getNewPermutation("(BACD) E)", getNewAlphabet("ABCDEZ"));
        getNewAlphabet("ABCDE.");
        getNewAlphabet("ABCDEZD");
        getNewPermutation("(AB)C)", getNewAlphabet("ABCDEZ"));
    }
}
