package ViewModels.TableModels;

import Constants.GuiStringConstants;
import Models.Parameters.Parameter;
import ViewModels.TableModels.BaseParamsTableModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 07/11/2015.
 */
public class GlobalPramsTableModel extends BaseParamsTableModel implements Serializable {

    protected List<String> getColumnHeaders() {
        columnHeaders = new ArrayList<>();
        columnHeaders.add(GuiStringConstants.GLOBAL_PARAMETER);
        columnHeaders.add(GuiStringConstants.VALUE);
        return columnHeaders;
    }

    public void overrideParameters(List<Parameter> parameters){
        this.parameters = null;
        setUpParameters(parameters);
    }
}
