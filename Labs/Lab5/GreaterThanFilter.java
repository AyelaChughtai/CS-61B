/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _colName = colName;
        _ref = ref;
        _index = input.colNameToIndex(colName);
    }

    @Override
    protected boolean keep() {
        if (_next.getValue(_index).compareTo(_ref) > 0) {
            return true;
        } else {
            return false;
        }
    }
    String _colName;
    String _ref;
    int _index;
}
