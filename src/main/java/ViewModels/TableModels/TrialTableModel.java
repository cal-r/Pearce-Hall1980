package ViewModels.TableModels;

import Constants.DefaultValuesConstants;
import Constants.GuiStringConstants;
import ViewModels.TableModels.BaseTableModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class TrialTableModel extends BaseTableModel implements Serializable {

	private simulateCompounds;	
		
    public TrialTableModel(){
        addPhase();
		simulateCompounds = false;
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
        return (columnHeaders.size() - 1) / getGroupPhaseCellsCount();
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

    public void setSimulateCompounds(boolean simulateCompounds){
		//copy data
		int phaseCount = getPhaseCount();
		List<List<String>> descriptions = new ArrayList<>();
		List<List<Boolean>> randomSelections = new ArrayList<>();
		for(int gid=0;gid<getGroupCount();gid++){
			descriptions.add(getPhaseDescriptions(gid));
			randomSelections.add(getRandomSelections(gid));
		}
		//remove columns (except the first one)
		while(columnHeaders.size()>1){
			removeRighmostColumn();
		}
		
		//add phases
		this.simulateCompounds = simulateCompounds;
		for(int pid=1;pid<=phaseCount;pid++){
			addPhase();
		}
		
	}
	
	public int getGroupPhaseCellsCount(){
		return simulateCompounds ? 4 : 2;
	}
	
	public void addGroup(){
        addRow();
        int groupId = getGroupCount()-1;
        setValueAt(GuiStringConstants.getDefaultGroupName(groupId), groupId, 0);
        for(int phaseId = 1;phaseId <= getPhaseCount();phaseId++){
            setValueAt(GuiStringConstants.DEFAULT_PHASE, groupId, getColId(phaseId, 1));
            setValueAt(DefaultValuesConstants.RANDOM_SELECTION, groupId, getColId(phaseId, 2));
			if(simulateCompounds){
				setValueAt(69, groupId, getColId(phaseId, 3));
				setValueAt(69, groupId, getColId(phaseId, 4));
			}
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
            descriptions.add((String) getValueAt(groupId, getColId(p, 1)));
        }
        return descriptions;
    }
	
	private int getColId(int phaseId, int groupPhaseColId){ //both 1-based
		return phaseId*getGroupPhaseCellsCount() - (getGroupPhaseCellsCount() - (getGroupPhaseCellsCount() - groupPhaseColId);
	}

    public List<Boolean> getRandomSelections(int groupId) {
        List<Boolean> selections = new ArrayList<>();
        for(int p=1;p<=getPhaseCount();p++){
            selections.add((Boolean) getValueAt(groupId, getColId(p, 2)));
        }
        return selections;
    }

    public void addPhase(){
        addColumn(GuiStringConstants.getPhaseTitle(getPhaseCount()), GuiStringConstants.DEFAULT_PHASE);
        addColumn(GuiStringConstants.RANDOM, DefaultValuesConstants.RANDOM_SELECTION);
		if(simulateCompounds){
			addColumn("some val", 69);
			addColumn("some ratio", 69);
		}
    }
	
    public void removePhase(){
        if(getPhaseCount()>1) {
            for(int i=0;i<getGroupPhaseCellsCount();i++){
				removeRighmostColumn();
			}
        }
    }
}
