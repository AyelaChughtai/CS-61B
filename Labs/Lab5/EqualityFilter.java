/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _colName = colName;
        _match = match;
        _index = input.colNameToIndex(colName);

    }

    @Override
    protected boolean keep() {
        if (_next.getValue(_index).equals(_match) ) {
            return true;
        } else {
            return false;
        }
    }
    String _colName;
    String _match;
    int _index;
}
