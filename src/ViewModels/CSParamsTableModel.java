package ViewModels;

import Constants.TableStringConstants;
import Models.Parameters.CsParameter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class CSParamsTableModel extends BaseTableModel {

    private List<CsParameter> parameters;

    @Override
    protected String[] getColumnHeaders() {
        String[] columnHeaders = {TableStringConstants.PARAMETER, TableStringConstants.VALUE};
        return columnHeaders;
    }

    @Override
    protected List<List<Object>> getInitialData() {
        List<List<Object>> data = new ArrayList<>();
        List<Object> firsRow = new ArrayList<>();
        firsRow.add("");
        firsRow.add("");
        data.add(firsRow);
        return data;
    }

    public void setUpParameters(List<CsParameter> parameters){
        this.parameters = parameters;
        for(int row = 0;row<parameters.size();row++){
            CsParameter rowParam = parameters.get(row);
            setValueAt(rowParam.getDisplayName(), row, 0);
            setValueAt(rowParam.getValue(), row, 1);
        }
        fireTableDataChanged();
    }
}
