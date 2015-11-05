package ViewModels;

import Constants.TableStringConstants;

/**
 * Created by Rokas on 03/11/2015.
 */
public class TrailTableModel extends BaseTableModel {

    @Override
    protected String[] getColumnHeaders() {
        String[] columnHeaders = {TableStringConstants.GROUP_NAME, TableStringConstants.PHASE};
        return columnHeaders;
    }

    @Override
    protected Object[][] getInitialData() {
        Object[][] data = {
                { TableStringConstants.GetDefaultGroupName(), TableStringConstants.DEFAULT_PHASE }
        };
        return data;
    }

    public String GetPhaseDescription() {
        return (String) getValueAt(0,1);
    }
}
