package ViewModels;

import Constants.TableStringConstants;
import Models.Parameters.GammaParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 07/11/2015.
 */
public class GlobalPramsTableModel extends BaseParamsTableModel{

    protected List<String> getColumnHeaders() {
        columnHeaders = new ArrayList<>();
        columnHeaders.add(TableStringConstants.GLOBAL_PARAMETER);
        columnHeaders.add(TableStringConstants.VALUE);
        return columnHeaders;
    }
}
