/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        _colName = colName;
        _subStr = subStr;
        _index = input.colNameToIndex(colName);
    }

    @Override
    protected boolean keep() {
        if (_next.getValue(_index).contains(_subStr)) {
            return true;
        } else {
            return false;
        }
    }
    String _colName;
    String _subStr;
    int _index;
}
