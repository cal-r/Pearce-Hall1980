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

    public HashMap<ConditionalStimulus, List<CsState>> csHistoriesMap;
    public Phase phase;
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
