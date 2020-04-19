import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Ayela Chughtai
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */

    /** The reader string.*/
    private Reader _str;

    /** The string we must translate from.*/
    private String _from;

    /** The result of translating the string.*/
    private String _to;

    public TrReader(Reader str, String from, String to) {
        assert from.length() == to.length();
        _str = str;
        _from = from;
        _to = to;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int x = this._str.read(cbuf, off, len);
        for (int index = off; index < off + len; index++) {
            int chInd = this._from.indexOf(cbuf[index]);
            if (chInd >= 0) {
                cbuf[index] = this._to.charAt(chInd);
            }
        }
        len = Math.min(len, x);
        return len;
    }

    @Override
    public void close() throws IOException {
        this._str.close();
    }

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */
}
