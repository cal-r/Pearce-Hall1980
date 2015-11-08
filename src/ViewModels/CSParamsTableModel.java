package ViewModels;

import Constants.TableStringConstants;
import Models.Parameters.CsParameter;
import Models.Parameters.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class CSParamsTableModel extends BaseParamsTableModel {

    @Override
    protected String[] getColumnHeaders() {
        String[] columnHeaders = {TableStringConstants.CS_PARAMETER, TableStringConstants.VALUE};
        return columnHeaders;
    }
}
