package enigma;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents a complete enigma machine.
 *
 * @author Ayela Chughtai
 */
class Machine {

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        assert numRotors > 1;
        assert pawls >= 0;
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors.toArray(new Rotor[0]);
        _rotorSlots = new Rotor[_numRotors];
    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _pawls;
    }

    /**
     * Return all rotors.
     */
    Rotor[] allrotors() {
        return _allRotors;
    }

    /**
     * Return _rotorSlots.
     */
    Rotor[] rotorSlots() {
        return _rotorSlots;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        if (rotors == null) {
            throw new EnigmaException("Rotors is empty.");
        }
        if (_rotorSlots.length != rotors.length) {
            throw new EnigmaException("Not enough or too "
                    + "many rotor names for rotor slots");
        }
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor allRotor : _allRotors) {
                if ((rotors[i].toUpperCase()).equals(
                        (allRotor.name().toUpperCase()))) {
                    _rotorSlots[i] = allRotor;
                }
            }
        }
        for (int i = 0; i < numRotors(); i++) {
            if (rotorSlots()[i] == null) {
                throw new EnigmaException("Empty rotor slots not possible");
            }
        }
    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 characters in my alphabet. The first letter refers
     * to the leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("Not enough settings "
                    + "for rotors in machine");
        } else {
            for (int i = 0; i < setting.length(); i++) {
                if (!_alphabet.contains(setting.charAt(i))) {
                    throw new EnigmaException("Character for setting"
                            + " not in alphabet");
                } else {
                    _rotorSlots[i + 1].set(setting.charAt(i));
                }
            }
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /**
     * @param ringSetting [EC].
     */
    void setRings(String ringSetting) {
        for (int i = 1; i < _rotorSlots.length; i++) {
            _rotorSlots[i].setRing(ringSetting.charAt(i - 1));
        }
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * the machine.
     */
    int convert(int c) {
        advanceRotors();
        int cOutput = _plugboard.permute(c);
        for (int i = _numRotors - 1; i >= 0; i--) {
            cOutput = _rotorSlots[i].convertForward(cOutput);
        }
        for (int i = 1; i < _rotorSlots.length; i++) {
            cOutput = _rotorSlots[i].convertBackward(cOutput);
        }
        cOutput = _plugboard.permute(cOutput);

        return cOutput;
    }

    /**
     * Helps take care of the double stepping in convert.
     */
    void advanceRotors() {
        Map<Integer, Boolean> willRotate = new HashMap<Integer, Boolean>();
        int willRotateIndex = 0;
        for (int i = numRotors() - 1; i
                > (numRotors() - 1 - _pawls); i--) {
            if (i == rotorSlots().length - 1) {
                willRotate.put(willRotateIndex, true);
            } else if (rotorSlots()[i].rotates()
                    && rotorSlots()[i + 1].atNotch()) {
                willRotate.put(willRotateIndex, true);
                willRotate.put(willRotateIndex - 1, true);
            }
            willRotateIndex += 1;
        }
        int willRotateIndex2 = numRotors() - 1;
        for (int i = 0; i < willRotate.size(); i++) {
            if (willRotate.get(i)) {
                rotorSlots()[willRotateIndex2].advance();
            }
            willRotateIndex2 -= 1;
        }
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {
        String convertedMsg = "";
        for (int i = 0; i < msg.length(); i++) {
            if (_alphabet.contains(msg.charAt(i))) {
                char letter = msg.charAt(i);
                int letterIndex = _alphabet.toInt(letter);
                int convertedLetterIndex = convert(letterIndex);
                char convertedLetter = _alphabet.toChar(
                        convertedLetterIndex);
                convertedMsg += convertedLetter;
            }
        }
        return convertedMsg;
    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;

    /**
     * Total number of rotors.
     */
    private int _numRotors;

    /**
     * Total number of pawls.
     */
    private int _pawls;

    /**
     * All available rotors.
     */
    private Rotor[] _allRotors;

    /**
     * Plugboard.
     */
    private Permutation _plugboard;

    /**
     * All rotors in machine.
     */
    private Rotor[] _rotorSlots;
}
