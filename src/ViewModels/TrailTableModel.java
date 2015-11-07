package ViewModels;

import Constants.GuiStringConstants;
import Constants.TableStringConstants;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class TrailTableModel extends BaseTableModel {

    @Override
    protected String[] getColumnHeaders() {
        String[] columnHeaders = {TableStringConstants.GROUP_NAME, TableStringConstants.PHASE};
        return columnHeaders;
    }

    @Override
    protected List<List<Object>> getInitialData() {
        List<List<Object>> data = new ArrayList<>();
        List<Object> firsRow = new ArrayList<>();
        firsRow.add(TableStringConstants.GetDefaultGroupName());
        firsRow.add(TableStringConstants.DEFAULT_PHASE);
        data.add(firsRow);
        return data;
    }

    public String GetPhaseDescription() {
        return (String) getValueAt(0,1);
    }
}
