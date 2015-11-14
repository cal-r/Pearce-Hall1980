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
    protected List<String> getColumnHeaders() {
        int phaseCount = getPhaseCount();
        columnHeaders = new ArrayList<>();
        columnHeaders.add(TableStringConstants.GROUP_NAME);
        for(int p=1;p<=phaseCount;p++) {
            columnHeaders.add(TableStringConstants.GetPhaseTitle(phaseCount));
        }
        return columnHeaders;
    }

    private int getPhaseCount(){
        if(columnHeaders==null){
            return 1;
        }
        return columnHeaders.size() - 1;
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

    public List<String> GetPhaseDescriptions(int groupNumber) {
        List<String> descriptions = new ArrayList<>();
        for(int p=0;p<getPhaseCount();p++){
            descriptions.add((String) getValueAt(groupNumber-1, p));
        }
        return descriptions;
    }

    //move column adding/removing logic to the BaseTableModel
    public void addPhase(){
        for(List<Object> row : data){
            row.add(TableStringConstants.DEFAULT_PHASE);
        }
        columnHeaders.add(TableStringConstants.GetPhaseTitle(1 + 1));
        fireTableStructureChanged();
    }

    public void removePhase(){
        for(List<Object> row : data) {
            row.remove(row.size()-1);
        }
        columnHeaders.remove(columnHeaders.size()-1);
        fireTableStructureChanged();
    }
}
