/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        _colName1 = colName1;
        _colName2 = colName2;
        _index1 = input.colNameToIndex(colName1);
        _index2 = input.colNameToIndex(colName2);


    }

    @Override
    protected boolean keep() {
        if (_next.getValue(_index1).equals(_next.getValue(_index2)) ) {
            return true;
        } else {
            return false;
        }
    }

    String _colName1;
    String _colName2;
    int _index1;
    int _index2;
}
