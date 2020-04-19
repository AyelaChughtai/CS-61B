import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Ayela Chughtai
 */
class ECHashStringSet implements StringSet {

    public ECHashStringSet() {
        _keys = 0;
        _bucketCount = 5;

        _hashCode = new LinkedList[_bucketCount];;
        for (int i = 0; i < _bucketCount; i++) {
            _hashCode[i] = new LinkedList<>();
        }

    }

    @Override
    public void put(String s) {
        if (s == null){
            return;
        }

        int index = s.hashCode() & 0x7fffffff % _bucketCount;
        _hashCode[index].add(s);
        _keys += 1;

        if (_keys/ _bucketCount > 5) {
            List<String> lst = asList();
            _bucketCount *= 2;

            _hashCode = new LinkedList[_bucketCount];
            for (int i = 0; i < _hashCode.length; i++) {
                _hashCode[i] = new LinkedList<>();
            }

            for (String value : lst) {
                int index2 = value.hashCode() & 0x7fffffff % _bucketCount;
                _hashCode[index2].add(value);
            }
        }
    }

    @Override
    public boolean contains(String s) {
        if (s == null){
            return false;
        } else {
            return _hashCode[s.hashCode() & 0x7fffffff % _bucketCount].contains(s);
        }
    }

    @Override
    public List<String> asList() {
        LinkedList<String> output = new LinkedList<>();
        for (int i = 0; i < _bucketCount; i++) {
            output.addAll(_hashCode[i]);
        }
        return output;
    }

    /** Keys. */
    private int _keys;

    /** bucketCount. */
    private int _bucketCount;

    /** hashCode. */
    private LinkedList<String>[] _hashCode;
}
