package ViewModels.TableModels;

import Constants.GuiStringConstants;
import Models.Parameters.Pools.UsParameterPool;
import Models.Parameters.UsParameter;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rokasg on 08/03/2016.
 */
public class UsParamsTableModel extends BaseTableModel implements Serializable {

    public UsParamsTableModel(){
        super();
    }

    private UsParameterPool usParameterPool;

    protected List<String> getColumnHeaders() {
        columnHeaders = new ArrayList<>();
        columnHeaders.add(GuiStringConstants.US_PARAMETER);
        return columnHeaders;
    }

    @Override
    protected List<List<Object>> getInitialData() {
        return new ArrayList<>();
    }

    private int getPhaseCount(){
        return usParameterPool.getUsParameters().get(0).getPhaseCount();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex > 0 && usParameterPool.getUsParameters().get(rowIndex).isAvailable(colIdToPhaseId(columnIndex));
    }

    public void clearTable(){
        super.clearTable();
        while (getColumnCount()>1){
            removeRighmostColumn();
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);
        usParameterPool.getUsParameters().get(rowIndex).setValue(colIdToPhaseId(columnIndex), (Double) aValue);
    }

    public void setUpParameters(UsParameterPool usParameterPool){
        this.usParameterPool = usParameterPool;
        clearTable();

        List<UsParameter> usParameters =usParameterPool.getUsParameters();
        for(int row = 0;row<usParameters.size();row++) {
            super.addRow();
            super.setValueAt(usParameters.get(row).getDisplayName(), row, 0);
        }

        for(int phase =0;phase<getPhaseCount();phase++){
            addColumn(GuiStringConstants.getPhaseTitle(phase));
        }

        for(int row = 0;row<usParameters.size();row++){
            for(int col =1;col<=getPhaseCount();col++) {
                UsParameter rowParam = usParameters.get(row);
                if(rowParam.isAvailable(colIdToPhaseId(col))) {
                    super.setValueAt(rowParam.getValue(col - 1), row, col);
                }else{
                    super.setValueAt(0.0, row, col);
                }
            }
        }
        fireTableDataChanged();
    }

    private static int colIdToPhaseId(int colId){
        return colId-1;
    }

    public TableCellRenderer getRenderer() {
        return new TableCellEditor();
    }

    public class TableCellEditor extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            if (!table.getModel().isCellEditable(row, col)) {
                l.setText("");
            }
            return l;

        }
    }
}
