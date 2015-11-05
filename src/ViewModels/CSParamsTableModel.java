package ViewModels;

import Constants.TableStringConstants;

/**
 * Created by Rokas on 03/11/2015.
 */
public class CSParamsTableModel extends BaseTableModel {

    @Override
    protected String[] getColumnHeaders() {
        String[] columnHeaders = {TableStringConstants.PARAMETER, TableStringConstants.VALUE};
        return columnHeaders;
    }

    @Override
    protected Object[][] getInitialData() {
        Object[][] data = {
                { "", ""}
        };
        return data;
    }
}
