package Models.History;


import Models.ConditionalStimulus;
import Models.GroupPhase;
import Models.Trial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 09/11/2015.
 */
public class GroupPhaseHistory implements Serializable {
    private GroupPhase groupPhase;
    private HashMap<ConditionalStimulus, List<CsState>> csHistoriesMap;
    private String groupName;
    private int trialCounrer;

    public GroupPhaseHistory(GroupPhase groupPhase){
        this.groupPhase = groupPhase;
        initHistoriesMap();
        trialCounrer = 0;
    }

    public List<ConditionalStimulus> getOrderedCues(){
        List<ConditionalStimulus> orderedCues = new ArrayList<>();
        for(int i=0;i<csHistoriesMap.size();i++){
            for(ConditionalStimulus cs : csHistoriesMap.keySet()){
                if(groupPhase.getCsOrder(cs) == i){
                    orderedCues.add(cs);
                }
            }
        }
        return orderedCues;
    }

    public CsState getState(ConditionalStimulus cue, int trialNumber) {
        return csHistoriesMap.get(cue).get(trialNumber - 1);
    }

    public List<CsState> getCsHistory(ConditionalStimulus cue){
        return csHistoriesMap.get(cue);
    }

    public void recordState(Trial trial){
        trialCounrer++;
        for(ConditionalStimulus cs : groupPhase.getPhaseCues()) {
            CsState csState = new CsState();
            csState.Ve = cs.getAssociationExcitatory();
            csState.Vi = cs.getAssociationInhibitory();
            csState.Vnet = cs.getAssociationNet();
            csState.Alpha = cs.getAlpha();
            csState.TrialNumber = trialCounrer;
            csState.TrialDescription = trial.toString();
            csHistoriesMap.get(cs).add(csState);
        }
    }

    private void initHistoriesMap(){
        csHistoriesMap = new HashMap<>();
        for(ConditionalStimulus cs : groupPhase.getPhaseCues()){
            csHistoriesMap.put(cs, new ArrayList<CsState>());
        }
    }

    public GroupPhase getGroupPhase(){
        return groupPhase;
    }

    public String getPhaseName(){
        return groupPhase.toString();
    }

    public int getNumberOfTrials(){
        return groupPhase.getNumberOfTrials();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
