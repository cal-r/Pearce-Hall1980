package ViewModels;

import Constants.TableStringConstants;

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
            columnHeaders.add(TableStringConstants.getPhaseTitle(phaseCount));
        }
        return columnHeaders;
    }

    public int getPhaseCount(){
        if(columnHeaders==null){
            return 1;
        }
        return columnHeaders.size() - 1;
    }

    public int getGroupCount(){
        return data.size();
    }

    @Override
    protected List<List<Object>> getInitialData() {
        List<List<Object>> data = new ArrayList<>();
        List<Object> firsRow = new ArrayList<>();
        firsRow.add(TableStringConstants.getDefaultGroupName(0));
        firsRow.add(TableStringConstants.DEFAULT_PHASE);
        data.add(firsRow);
        return data;
    }

    public void addGroup(){
        addRow();
        int groupId = getGroupCount()-1;
        setValueAt(TableStringConstants.getDefaultGroupName(groupId), groupId, 0);
        for(int phaseId = 0;phaseId < getPhaseCount();phaseId++){
            setValueAt(TableStringConstants.DEFAULT_PHASE, groupId, phaseId+1);
        }
    }

    public String getGroupName(int groupId){
        return (String) getValueAt(groupId, 0);
    }

    public List<String> getPhaseDescriptions(int groupId) {
        List<String> descriptions = new ArrayList<>();
        for(int p=1;p<=getPhaseCount();p++){
            descriptions.add((String) getValueAt(groupId, p));
        }
        return descriptions;
    }

    public void addPhase(){
        addColumn(TableStringConstants.getPhaseTitle(getPhaseCount()), TableStringConstants.DEFAULT_PHASE);
    }

    public void removePhase(){
        for(List<Object> row : data) {
            row.remove(row.size()-1);
        }
        columnHeaders.remove(getPhaseCount()-1);
        fireTableStructureChanged();
    }
}
