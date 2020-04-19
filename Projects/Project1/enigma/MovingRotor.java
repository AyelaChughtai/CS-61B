package enigma;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Ayela Chughtai
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return true;
    }

    @Override
    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        boolean output = false;
        for (int i = 0; i < _notches.length(); i++) {
            if (permutation().wrap(setting())
                == alphabet().toInt(_notches.charAt(i))) {
                output = true;
            }
        }
        return output;
    }

    @Override
    void advance() {
        set(setting() + 1);
    }

    /** Notches of the rotor. */
    private String _notches;
}
