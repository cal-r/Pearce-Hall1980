package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 09/11/2015.
 */
public class PhaseHistory {

    public HashMap<ConditionalStimulus, List<CsState>> csHistoriesMap;
    private Phase phase;

    public PhaseHistory(Phase phase){
        this.phase = phase;
        initHistoriesMap();
    }

    public void recordState(int trailIndex){
        for(ConditionalStimulus cs : phase.getCues()) {
            CsState csState = new CsState();
            csState.Ve = cs.AssociationExcitatory;
            csState.Vi = cs.AssociationInhibitory;
            csState.Vnet = cs.getAssociationNet();
            csState.TrailNumber = trailIndex+1;
            csHistoriesMap.get(cs).add(csState);
        }
    }

    private void initHistoriesMap(){
        csHistoriesMap = new HashMap<>();
        for(ConditionalStimulus cs : phase.getCues()){
            csHistoriesMap.put(cs, new ArrayList<CsState>());
        }
    }

    public class CsState{
        public double Ve;
        public double Vi;
        public double Vnet;
        public int TrailNumber;
    }
}
