package ViewModels;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */

public abstract class BaseTableModel extends AbstractTableModel {

    JTable table;

    protected String[] columnHeaders;
    protected List<List<Object>> data;

    protected BaseTableModel(){
        columnHeaders = getColumnHeaders();
        data = getInitialData();
    }

    protected abstract String[] getColumnHeaders();
    protected abstract List<List<Object>> getInitialData();

    @Override
    public int getRowCount() {
        return data.size();
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
        return data.get(rowIndex).get(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(rowIndex==getRowCount()){
            addRow();
        }
        data.get(rowIndex).set(columnIndex, aValue);
    }

    private void addRow(){
        List newRow = new ArrayList<>();
        for(int col=0;col<getColumnCount();col++){
            newRow.add(new Object());
        }
        data.add(newRow);
    }
}
