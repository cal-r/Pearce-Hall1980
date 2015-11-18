package Models.History;

import Models.ConditionalStimulus;
import Models.Phase;
import Models.Trail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 09/11/2015.
 */
public class PhaseHistory {

    private HashMap<ConditionalStimulus, List<CsState>> csHistoriesMap;
    private Phase phase;
    private int trailNumber;

    public PhaseHistory(Phase phase){
        this.phase = phase;
        initHistoriesMap();
        trailNumber =0;
    }

    public void recordState(Trail trail){
        trailNumber++;
        for(ConditionalStimulus cs : phase.getPhaseCues()) {
            CsState csState = new CsState();
            csState.Ve = cs.getAssociationExcitatory();
            csState.Vi = cs.getAssociationInhibitory();
            csState.Vnet = cs.getAssociationNet();
            csState.TrailNumber = trailNumber;
            csState.TrailDescription = trail.toString();
            csHistoriesMap.get(cs).add(csState);
        }
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s \n", phase.toString()));
        for(ConditionalStimulus cs : csHistoriesMap.keySet()) {
            builder.append(String.format("Cs: %s \n", cs.Name));
            for (PhaseHistory.CsState state : csHistoriesMap.get(cs)) {
                builder.append(String.format("#trial: %1$d \t", state.TrailNumber));
                builder.append(String.format("%s \t", state.TrailDescription));
                builder.append(String.format("Ve: %1$.5f \t", state.Ve));
                builder.append(String.format("Vi: %1$.5f \t", state.Vi));
                builder.append(String.format("Vnet: %1$.5f \t", state.Vnet));
                builder.append("\n");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private void initHistoriesMap(){
        csHistoriesMap = new HashMap<>();
        for(ConditionalStimulus cs : phase.getPhaseCues()){
            csHistoriesMap.put(cs, new ArrayList<CsState>());
        }
    }

    public class CsState{
        public double Ve;
        public double Vi;
        public double Vnet;
        public int TrailNumber;
        public String TrailDescription;
    }
}
