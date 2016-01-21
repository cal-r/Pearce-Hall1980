package Models.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 21/01/2016.
 */
public class SimulationHistory extends ArrayList<GroupHistory> {
    public List<PhaseHistory> getPhases(){
        List<PhaseHistory> phases = new ArrayList<>();
        for(int i=0;i<numberOfPhases();i++){
            PhaseHistory phaseHist = new PhaseHistory(getPhaseName(i));
            GroupHistory groupHistory = this.get(i);
            for (GroupPhaseHistory gpHist : groupHistory.phaseHistories){
                gpHist.setGroupName(groupHistory.group.Name);
                phaseHist.add(gpHist);
            }
            phases.add(phaseHist);
        }
        return phases;
    }

    private int numberOfPhases(){
        return this.get(0).phaseHistories.size();
    }

    //param index zero based
    private String getPhaseName(int index){
        return this.get(0).phaseHistories.get(index).getPhaseName();
    }
}
