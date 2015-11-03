package ViewModels;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Created by Rokas on 03/11/2015.
 */

public abstract class BaseTableModel implements TableModel {

    protected String[] columnHeaders;
    protected Object[][] data;

    protected BaseTableModel(){
        this.columnHeaders = getColumnHeaders();
        this.data = getInitialData();
    }

    protected abstract String[] getColumnHeaders();
    protected abstract Object[][] getInitialData();

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return columnHeaders.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnHeaders[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = aValue;
    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
