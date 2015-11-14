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
    private int trailNumber;

    public PhaseHistory(Phase phase){
        this.phase = phase;
        initHistoriesMap();
        trailNumber =0;
    }

    public void recordState(){
        trailNumber++;
//        for(ConditionalStimulus cs : phase.getCues()) {
//            CsState csState = new CsState();
//            csState.Ve = cs.getAssociationExcitatory();
//            csState.Vi = cs.getAssociationInhibitory();
//            csState.Vnet = cs.getAssociationNet();
//            csState.TrailNumber = trailNumber;
//            csHistoriesMap.get(cs).add(csState);
//        }
    }

    private void initHistoriesMap(){
        csHistoriesMap = new HashMap<>();
//        for(ConditionalStimulus cs : phase.getCues()){
//            csHistoriesMap.put(cs, new ArrayList<CsState>());
//        }
    }

    public class CsState{
        public double Ve;
        public double Vi;
        public double Vnet;
        public int TrailNumber;
    }
}
