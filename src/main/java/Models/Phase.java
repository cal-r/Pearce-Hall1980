package Models;

import Constants.TableStringConstants;
import Models.History.PhaseHistory;
import Models.Parameters.GammaParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 03/11/2015.
 */
public class Phase {
    public ArrayList<Trail> trails;
    private Map<Character, ConditionalStimulus> phaseCsMap;
    private int phaseId;

    public Phase(int phaseId) {
        this.phaseId = phaseId;
        trails = new ArrayList<>();
    }

    public PhaseHistory simulateTrails(GammaParameter gamma)
    {
        PhaseHistory history = new PhaseHistory(this);
        for(Trail trail : trails) {
            trail.simulate(calcVNet(), gamma.getValue());
            history.recordState(trail);
        }
        return history;
    }

    public void addTrailType(List<Trail> trailsToAdd) { //all trails in the param are the same (e.g. 'AB+')
        trails.addAll(trailsToAdd);
    }

    private double calcVNet(){
        double vNet = 0;
        for(ConditionalStimulus cs : getPhaseCues()){
            vNet += cs.getAssociationNet();
        }
        return vNet;
    }

    public List<ConditionalStimulus> getPhaseCues() {
        if (phaseCsMap == null)
            initCsMap();

        return new ArrayList<>(phaseCsMap.values());
    }

    private void initCsMap(){
        phaseCsMap = new HashMap<>();
        for(Trail t : trails){
            for(ConditionalStimulus cs : t.cuesPresent){
                if(!phaseCsMap.containsKey(cs.Name)){
                    phaseCsMap.put(cs.Name, cs);
                }
            }
        }
    }

    public String toString(){
        return TableStringConstants.getPhaseTitle(phaseId);
    }
}
