package ViewModels;

import StringConstants.GuiConstants;
import StringConstants.TableConstants;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Created by Rokas on 03/11/2015.
 */
public class TrailTableModel extends BaseTableModel {

    @Override
    protected String[] getColumnHeaders() {
        String[] columnHeaders = {TableConstants.GROUP_NAME, TableConstants.PHASE};
        return columnHeaders;
    }

    @Override
    protected Object[][] getInitialData() {
        Object[][] data = {
                { TableConstants.GetDefaultGroupName(), TableConstants.DEFAULT_PHASE }
        };
        return data;
    }

    public String GetPhaseDescription() {
        return (String) getValueAt(0,1);
    }
}
