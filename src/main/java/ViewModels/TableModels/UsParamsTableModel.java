package ViewModels.TableModels;

import Constants.GuiStringConstants;
import Models.Parameters.Pools.UsParameterPool;
import Models.Parameters.UnconditionalStimulus.UsParameter;

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
        int firstValueColumn = getPhaseCount() == 0 ? 1 : 2;
        return columnIndex >= firstValueColumn && usParameterPool.getUsParameters().get(rowIndex).isAvailable(colIdToPhaseId(columnIndex));
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

        if(getPhaseCount()==0) {
            //single phase parameters
            columnHeaders.set(0, GuiStringConstants.US_PARAMETER);
            addColumn(GuiStringConstants.VALUE);

            populateSinglePhaseParameters(usParameters);
        }else {
            //multiple us parameters
            columnHeaders.set(0, GuiStringConstants.GROUP_NAME);
            addColumn(GuiStringConstants.US_PARAMETER);

            for (int phase = 0; phase < getPhaseCount(); phase++) {
                addColumn(GuiStringConstants.getPhaseTitle(phase));
            }

            populateMultiPhaseParameters(usParameters);
        }
        fireTableDataChanged();
    }

    private void populateMultiPhaseParameters(List<UsParameter> usParameters){
        for(int row = 0;row<usParameters.size();row++) {
            addRow();
            super.setValueAt(usParameters.get(row).getGroup().Name, row, 0);
            super.setValueAt(usParameters.get(row).getDisplayName(), row, 1);
        }

        for (int row = 0; row < usParameters.size(); row++) {
            for (int col = 2; colIdToPhaseId(col) < getPhaseCount(); col++) {
                UsParameter rowParam = usParameters.get(row);
                if (rowParam.isAvailable(colIdToPhaseId(col))) {
                    super.setValueAt(rowParam.getValue(colIdToPhaseId(col)), row, col);
                } else {
                    super.setValueAt(0.0, row, col);
                }
            }
        }
    }

    private void populateSinglePhaseParameters(List<UsParameter> usParameters){
        for(int row = 0;row<usParameters.size();row++) {
            addRow();
            super.setValueAt(usParameters.get(row).getDisplayName(), row, 0);
            super.setValueAt(usParameters.get(row).getValue(0), 0, 1);
        }
    }

    private static int colIdToPhaseId(int colId){
        return colId-2;
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
                l.setBackground(new Color(240, 240, 240));
            }else{
                l.setBackground(javax.swing.UIManager.getColor("Table.dropCellForeground"));
            }
            l.setHorizontalAlignment(RIGHT);
            return l;

        }
    }
}
