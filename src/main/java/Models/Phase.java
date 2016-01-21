package Models;

import Constants.DefaultValuesConstants;
import Constants.TableStringConstants;
import Helpers.RandomArrayGenerator;
import Helpers.RandomSimulationHelper;
import Models.History.GroupPhaseHistory;
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

    private boolean random;
    private Map<Character, ConditionalStimulus> phaseCsMap;
    private int phaseId;

    public Phase(int phaseId) {
        this.phaseId = phaseId;
        trails = new ArrayList<>();
    }

    public GroupPhaseHistory simulateTrails(GammaParameter gamma)
    {
        if(random){
            return simulateTrailsRandomly(gamma);
        }
        return simulateTrailsSequentially(gamma);
    }

    private GroupPhaseHistory simulateTrailsSequentially(GammaParameter gamma){
        GroupPhaseHistory history = new GroupPhaseHistory(this);
        for(Trail trail : trails) {
            trail.simulate(calcVNet(), gamma.getValue());
            history.recordState(trail);
        }
        return history;
    }

    private GroupPhaseHistory simulateTrailsRandomly(GammaParameter gamma) {
        List<ConditionalStimulus> csCopies = getCsCopies(); //preserve initial state of CSs
        List<GroupPhaseHistory> tempHistories = new ArrayList<>(); //stores every simulation
        //sim phase 1000 times
        for(int simNum = 0;simNum< DefaultValuesConstants.NUMBER_OF_RANDOM_COMBINATIONS; simNum++) {
            resetCues(csCopies);
            GroupPhaseHistory history = new GroupPhaseHistory(this);
            int[] randomArray = RandomArrayGenerator.createRandomDistinctArray(trails.size());
            for (int trailNo = 0; trailNo < trails.size(); trailNo++) {
                Trail trail = trails.get(randomArray[trailNo]);
                trail.simulate(calcVNet(), gamma.getValue());
                history.recordState(trail);
            }
            tempHistories.add(history);
        }
        //get avg history
        GroupPhaseHistory averageHistory = RandomSimulationHelper.getAverageHistory(tempHistories);

        //set cs properties to average values
        for(ConditionalStimulus cs : getPhaseCues()) {
            GroupPhaseHistory.CsState csState = averageHistory.getState(cs.Name, trails.size()); //get the state of cs after the last trail
            cs.setAlpha(csState.Alpha);
            cs.setAssociationExcitatory(csState.Ve);
            cs.setAssociationInhibitory(csState.Vi);
        }
        return averageHistory;
    }

    public void addTrailType(List<Trail> trailsToAdd) { //all trails in the param are the same (e.g. 'AB+')
        trails.addAll(trailsToAdd);
    }

    public void reset(){
        for(ConditionalStimulus cs : getPhaseCues()){
            cs.reset();
        }
    }

    private void resetCues(List<ConditionalStimulus> copies){
        for(ConditionalStimulus copy : copies){
            phaseCsMap.get(copy.Name).reset(copy);
        }
    }

    private List<ConditionalStimulus> getCsCopies(){
        List<ConditionalStimulus> copies = new ArrayList<>();
        for(ConditionalStimulus cs : getPhaseCues()){
            copies.add(cs.getCopy());
        }
        return copies;
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

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean isRandom) {
        this.random = isRandom;
    }

    public int getNumberOfTrails(){ return trails.size(); }

    public String toString(){
        return TableStringConstants.getPhaseTitle(phaseId);
    }
}
