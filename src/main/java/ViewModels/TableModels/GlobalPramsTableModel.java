package ViewModels.TableModels;

import Constants.GuiStringConstants;
import ViewModels.TableModels.BaseParamsTableModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 07/11/2015.
 */
public class GlobalPramsTableModel extends BaseParamsTableModel {

    protected List<String> getColumnHeaders() {
        columnHeaders = new ArrayList<>();
        columnHeaders.add(GuiStringConstants.GLOBAL_PARAMETER);
        columnHeaders.add(GuiStringConstants.VALUE);
        return columnHeaders;
    }
}
