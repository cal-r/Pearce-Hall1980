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
public class GroupPhaseHistory {
    private Phase phase;
    private HashMap<Character, List<CsState>> csHistoriesMap;
    private String groupName;
    private int trailCounrer;

    public GroupPhaseHistory(Phase phase){
        this.phase = phase;
        initHistoriesMap();
        trailCounrer =0;
    }

    public List<Character> getCues(){
        return new ArrayList<>(csHistoriesMap.keySet());
    }

    public CsState getState(Character cue, int trailNumber) {
        return csHistoriesMap.get(cue).get(trailNumber-1);
    }

    public List<CsState> getCsHistory(Character cue){
        return csHistoriesMap.get(cue);
    }

    public void recordState(Trail trail){
        trailCounrer++;
        for(ConditionalStimulus cs : phase.getPhaseCues()) {
            CsState csState = new CsState();
            csState.Ve = cs.getAssociationExcitatory();
            csState.Vi = cs.getAssociationInhibitory();
            csState.Vnet = cs.getAssociationNet();
            csState.Alpha = cs.getAlpha();
            csState.TrailNumber = trailCounrer;
            csState.TrailDescription = trail.toString();
            csHistoriesMap.get(cs.Name).add(csState);
        }
    }

    private void initHistoriesMap(){
        csHistoriesMap = new HashMap<>();
        for(ConditionalStimulus cs : phase.getPhaseCues()){
            csHistoriesMap.put(cs.Name, new ArrayList<CsState>());
        }
    }

    public class CsState{
        public double Ve;
        public double Vi;
        public double Vnet;
        public double Alpha;
        public int TrailNumber;
        public String TrailDescription;
    }

    public Phase getPhase(){
        return phase;
    }

    public String getPhaseName(){
        return phase.toString();
    }

    public int getNumberOfTrails(){
        return phase.getNumberOfTrails();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
