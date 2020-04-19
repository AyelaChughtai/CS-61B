package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Ayela Chughtai
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        assert chars != null;
        _chars = chars;
        if (_chars.replace(" ", "").equals("")) {
            throw new EnigmaException("Empty alphabet "
                    + "or no letters in alphabet");
        }
        charsList = new char[_chars.length()];
        for (int i = 0; i < chars.length(); i++) {
            if (_chars.charAt(i) == '('
                    || _chars.charAt(i) == ')'
                    || _chars.charAt(i) == '*') {
                throw new EnigmaException("Unwanted chars in alphabet");
            } else {
                charsList[i] = _chars.charAt(i);
            }
        }
        for (int i = 0; i < charsList.length - 1; i++) {
            for (int j = i + 1; j < charsList.length; j++) {
                if (charsList[i] == charsList[j]) {
                    throw new EnigmaException("Repeated "
                            + "character in alphabet");
                }
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return charsList.length;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        boolean output = false;
        for (int i = 0; i < size(); i++) {
            if (charsList[i] == ch) {
                output = true;
                break;
            }
        }
        return output;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw new EnigmaException("Character index out of bounds");
        }
        return charsList[index];
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int result = 0;
        if (!contains(ch)) {
            throw new EnigmaException("Character not in alphabet");
        } else {
            for (int i = 0; i < size(); i++) {
                if (charsList[i] == ch) {
                    result = i;
                    break;
                }
            }
        }
        return result;
    }
    /** String of characters constituting alphabet. */
    private String _chars;

    /**List of characters constituting alphabet. */
    private char[] charsList;
}
