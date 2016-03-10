package ViewModels.TableModels;

import Constants.GuiStringConstants;
import Models.Parameters.Parameter;
import Models.Parameters.Pools.UsParameterPool;
import Models.Parameters.UsParameter;

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
        return columnHeaders;
    }

    @Override
    protected List<List<Object>> getInitialData() {
        return new ArrayList<>();
    }

    private int getPhaseCount(){
        return usParameterPool.getUsParameters().get(0).getPhaseCount();
    }

    public void clearTable(){
        super.clearTable();
        while (getColumnCount()>1){
            removeRighmostColumn();
        }
    }

    public void setUpParameters(UsParameterPool usParameterPool){
        this.usParameterPool = usParameterPool;
        clearTable();

        List<UsParameter> usParameters =usParameterPool.getUsParameters();
        for(int row = 0;row<usParameters.size();row++) {
            super.addRow();
            super.setValueAt(usParameters.get(row).getDisplayName(), row, 0);
        }

        for(int phase =0;phase<getPhaseCount();phase++){
            addColumn(GuiStringConstants.getPhaseTitle(phase));
        }

        for(int row = 0;row<usParameters.size();row++){
            for(int col =1;col<=getPhaseCount();col++) {
                UsParameter rowParam = usParameters.get(row);
                super.setValueAt(rowParam.getValue(col-1), row, col);
            }
        }
        fireTableDataChanged();
    }
}
