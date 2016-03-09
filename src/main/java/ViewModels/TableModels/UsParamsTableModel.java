package ViewModels.TableModels;

import Constants.GuiStringConstants;
import Models.Parameters.Pools.UsParameterPool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rokasg on 08/03/2016.
 */
public class UsParamsTableModel extends BaseTableModel implements Serializable {

    private UsParameterPool usParameterPool;

    protected List<String> getColumnHeaders() {
        columnHeaders = new ArrayList<>();
        columnHeaders.add(GuiStringConstants.US_PARAMETER);
        columnHeaders.add(GuiStringConstants.VALUE);
        return columnHeaders;
    }

    @Override
    protected List<List<Object>> getInitialData() {
        return new ArrayList<>();
    }

    public void setUpParameters(UsParameterPool usParameterPool){
        this.usParameterPool = usParameterPool;

    }
}
