package ViewModels;

import Constants.DefaultValuesConstants;
import Constants.GuiStringConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class TrailTableModel extends BaseTableModel {

    public TrailTableModel(){
        addPhase();
    }

    @Override
    protected List<String> getColumnHeaders() {
        if(columnHeaders==null) {
            columnHeaders = new ArrayList<>();
            columnHeaders.add(GuiStringConstants.GROUP_NAME);
        }
        return columnHeaders;
    }

    public int getPhaseCount(){
        if(columnHeaders==null){
            return 0;
        }
        return (columnHeaders.size() - 1) / 2;
    }

    public int getGroupCount(){
        return data.size();
    }

    @Override
    protected List<List<Object>> getInitialData() {
        List<List<Object>> data = new ArrayList<>();
        List<Object> firsRow = new ArrayList<>();
        firsRow.add(GuiStringConstants.getDefaultGroupName(0));
        data.add(firsRow);
        return data;
    }

    public void addGroup(){
        addRow();
        int groupId = getGroupCount()-1;
        setValueAt(GuiStringConstants.getDefaultGroupName(groupId), groupId, 0);
        for(int phaseId = 1;phaseId <= getPhaseCount();phaseId++){
            setValueAt(GuiStringConstants.DEFAULT_PHASE, groupId, phaseId*2 - 1);
            setValueAt(DefaultValuesConstants.RANDOM_SELECTION, groupId, phaseId*2);
        }
    }

    public void removeGroup(){
        if(getGroupCount()>1){
            removeBottomRow();
        }
        fireTableStructureChanged();
    }

    public String getGroupName(int groupId){
        return (String) getValueAt(groupId, 0);
    }

    public List<String> getPhaseDescriptions(int groupId) {
        List<String> descriptions = new ArrayList<>();
        for(int p=1;p<=getPhaseCount();p++){
            descriptions.add((String) getValueAt(groupId, p*2-1));
        }
        return descriptions;
    }

    public List<Boolean> getRandomSelections(int groupId) {
        List<Boolean> selections = new ArrayList<>();
        for(int p=1;p<=getPhaseCount();p++){
            selections.add((Boolean) getValueAt(groupId, p * 2));
        }
        return selections;
    }

    public void addPhase(){
        addColumn(GuiStringConstants.getPhaseTitle(getPhaseCount()), GuiStringConstants.DEFAULT_PHASE);
        addColumn(GuiStringConstants.RANDOM, DefaultValuesConstants.RANDOM_SELECTION);
    }

    public void removePhase(){
        if(getPhaseCount()>1) {
            removeRighmostColumn();
            removeRighmostColumn();
        }
    }
}
