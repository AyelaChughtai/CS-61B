package enigma;

import java.util.HashMap;
import java.util.Map;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ayela Chughtai
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = new HashMap<Character, Character>();

        cycles = cycles.replaceAll(" ", "");

        for (int i = 0; i < cycles.replaceAll("[()]", "").length(); i++) {
            char c = cycles.replaceAll("[()]", "").charAt(i);
            if (!_alphabet.contains(c)) {
                throw new EnigmaException("Letter not in alphabet");
            }
        }

        if (!cycles.equals("")) {
            for (int i = 1; i < cycles.length() - 1; i++) {
                if (cycles.charAt(i) == ')' && cycles.charAt(i + 1) != '(') {
                    throw new EnigmaException("Incorrect cycle format");
                }
            }
            for (int i = 1; i < cycles.replaceAll("[()]", "").length()
                    - 1; i++) {
                for (int j = i + 1; j < cycles.replaceAll("[()]",
                        "").length(); j++) {
                    if (cycles.replaceAll("[()]", "").charAt(i)
                            == cycles.replaceAll("[()]", "").charAt(j)) {
                        throw new EnigmaException("Repeated character");
                    }
                }
            }

            int openingBrackets = 0;
            int closingBrackets = 0;
            StringBuilder oneCycle = new StringBuilder();

            for (int i = 0; i < cycles.length(); i++) {
                char c = cycles.charAt(i);
                if (c == '(') {
                    openingBrackets += 1;
                } else if (c == ')') {
                    closingBrackets += 1;
                    addCycle(oneCycle.toString());
                    oneCycle = new StringBuilder();
                } else {
                    oneCycle.append(c);
                }
            }
            if (openingBrackets != closingBrackets) {
                throw new EnigmaException("Incorrect cycle format");
            }
        }
    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    public void addCycle(String cycle) {
        for (int i = 1; i < cycle.length() - 1; i++) {
            for (int j = i + 1; j < cycle.length(); j++) {
                if (cycle.replaceAll("[()]", "").charAt(i)
                        == cycle.replaceAll("[()]", "").charAt(j)) {
                    throw new EnigmaException("Repeated character in cycle");
                }
            }
        }
        for (int i = 0; i < cycle.length(); i++) {
            char c = cycle.charAt(i);
            if (!_alphabet.contains(cycle.replaceAll("[()]", "").charAt(i))) {
                throw new EnigmaException("Letter not in alphabet");
            } else if (_cycles.containsKey(c)) {
                throw new EnigmaException("Repeated character in cycle");
            } else if (i == cycle.length() - 1) {
                _cycles.put(c, cycle.charAt(0));
            } else {
                _cycles.put(c, cycle.charAt(i + 1));
            }
        }
    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return _alphabet.size();
    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        p = wrap(p);
        char pChar = _alphabet.toChar(p);
        return _alphabet.toInt(permute(pChar));
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        c = wrap(c);
        char cChar = _alphabet.toChar(c);
        return _alphabet.toInt(invert(cChar));
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {
        if (!alphabet().contains(p)) {
            throw new EnigmaException("Letter not in alphabet");
        }
        return _cycles.getOrDefault(p, p);
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    char invert(char c) {
        if (!alphabet().contains(c)) {
            throw new EnigmaException("Letter not in alphabet");
        }
        for (Map.Entry<Character, Character> entry : _cycles.entrySet()) {
            if (entry.getValue() == c) {
                return entry.getKey();
            }
        }
        return c;
    }

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    boolean derangement() {
        boolean result = true;
        if (_cycles.isEmpty()) {
            result = false;
        } else {
            for (Map.Entry<Character, Character> entry : _cycles.entrySet()) {
                if (entry.getValue() == entry.getKey()) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Alphabet of this permutation.
     */
    private Alphabet _alphabet;

    /**
     * A hashmap of string in the form "(cccc) (cc) ..." where the c's are
     * characters in ALPHABET, which is interpreted as a permutation
     * in cycle notation.
     */
    private Map<Character, Character> _cycles;
}
