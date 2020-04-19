package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Ayela Chughtai
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _ringSetting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Return ring setting (extra credit). */
    int ringSetting() {
        return _ringSetting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = _permutation.wrap(posn);
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        set(_permutation.wrap(alphabet().toInt(cposn)));
    }

    /** Set ring setting() to POSN [EC].*/
    void setRing(int posn) {
        _ringSetting = _permutation.wrap(posn);
    }

    /** Set ring setting() to character CPOSN [EC]. */
    void setRing(char cposn) {
        setRing(_permutation.wrap(alphabet().toInt(cposn)));
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. Includes [EC]. */
    int convertForward(int p) {
        int ring = _permutation.wrap(_setting - _ringSetting);
        int output = _permutation.permute(_permutation.wrap(p + ring));
        return _permutation.wrap(output - ring);
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. Includes [EC]. */
    int convertBackward(int e) {
        int ring = _permutation.wrap(_setting - _ringSetting);
        int output = _permutation.invert(_permutation.wrap(e + ring));
        return _permutation.wrap(output - ring);
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** The setting of the rotor (an int from 0-25). */
    private int _setting;

    /** The ring setting of the rotor [EC]. */
    private int _ringSetting;

}
