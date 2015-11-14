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
    protected List<String> getColumnHeaders() {
        columnHeaders = new ArrayList<>();
        columnHeaders.add(TableStringConstants.CS_PARAMETER);
        columnHeaders.add(TableStringConstants.VALUE);
        return columnHeaders;
    }
}
