package ViewModels;

import Constants.TableStringConstants;
import Models.Parameters.GammaParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 07/11/2015.
 */
public class GlobalPramsTableModel extends BaseParamsTableModel{

    protected String[] getColumnHeaders() {
        String[] columnHeaders = {TableStringConstants.GLOBAL_PARAMETER, TableStringConstants.VALUE};
        return columnHeaders;
    }
}
