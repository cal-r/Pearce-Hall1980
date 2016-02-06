package Models.History;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 21/01/2016.
 */
public class SimulationHistory extends ArrayList<GroupHistory> implements Serializable {
    public List<PhaseHistory> getPhases(){
        List<PhaseHistory> phases = new ArrayList<>();
        for(int i=0;i<numberOfPhases();i++){
            PhaseHistory phaseHist = new PhaseHistory(getPhaseName(i));
            for(GroupHistory groupHistory : this)
            {
                GroupPhaseHistory gpHist = groupHistory.getGroupPhaseHistory(i);
                gpHist.setGroupName(groupHistory.group.Name);
                phaseHist.add(gpHist);
            }
            phases.add(phaseHist);
        }
        return phases;
    }

    private int numberOfPhases(){
        return this.get(0).getNumberOfPhases();
    }

    //param index zero based
    private String getPhaseName(int index){
        return this.get(0).getGroupPhaseHistory(index).getPhaseName();
    }
}
