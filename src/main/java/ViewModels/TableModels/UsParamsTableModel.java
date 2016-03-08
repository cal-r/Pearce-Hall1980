package ViewModels.TableModels;

import Constants.GuiStringConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rokasg on 08/03/2016.
 */
public class UsParamsTableModel extends BaseParamsTableModel implements Serializable {

    protected List<String> getColumnHeaders() {
        columnHeaders = new ArrayList<>();
        columnHeaders.add(GuiStringConstants.US_PARAMETER);
        columnHeaders.add(GuiStringConstants.VALUE);
        return columnHeaders;
    }
}
