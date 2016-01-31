package Models;

import Constants.DefaultValuesConstants;
import Constants.GuiStringConstants;
import Helpers.RandomArrayGenerator;
import Helpers.RandomSimulationHelper;
import Models.History.CsState;
import Models.History.GroupPhaseHistory;
import Models.Parameters.GammaParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 03/11/2015.
 */
public class GroupPhase {
    public ArrayList<Trial> trials;

    private boolean random;
    private Map<Character, ConditionalStimulus> phaseCsMap;
    private int phaseId;

    public GroupPhase(int phaseId) {
        this.phaseId = phaseId;
        trials = new ArrayList<>();
    }

    public GroupPhaseHistory simulateTrials(GammaParameter gamma) {
        if(random){
            return simulateTrialsRandomly(gamma);
        }
        return simulateTrialsSequentially(gamma);
    }

    private GroupPhaseHistory simulateTrialsSequentially(GammaParameter gamma){
        GroupPhaseHistory history = new GroupPhaseHistory(this);
        for(int i=0;i<trials.size();i++){
            Trial trial = trials.get(i);
            //The algorithm runs AFTER the trial finishes and gives the predictive value for the following trial.
            history.recordState(trial);
            trial.simulate(calcVNet(), gamma.getValue());
        }
        return history;
    }

    private GroupPhaseHistory simulateTrialsRandomly(GammaParameter gamma) {
        List<ConditionalStimulus> csCopies = getCsCopies(); //preserve initial state of CSs
        List<GroupPhaseHistory> tempHistories = new ArrayList<>(); //stores every simulation
        //sim phase 1000 times
        for(int simNum = 0;simNum< DefaultValuesConstants.NUMBER_OF_RANDOM_COMBINATIONS; simNum++) {
            resetCues(csCopies);
            GroupPhaseHistory history = new GroupPhaseHistory(this);
            int[] randomArray = RandomArrayGenerator.createRandomDistinctArray(trials.size());
            for (int trialNo = 0; trialNo < trials.size(); trialNo++) {
                Trial trial = trials.get(randomArray[trialNo]);
                history.recordState(trial);
                trial.simulate(calcVNet(), gamma.getValue());
            }
            tempHistories.add(history);
        }
        //get avg history
        GroupPhaseHistory averageHistory = RandomSimulationHelper.getAverageHistory(tempHistories);

        //set cs properties to average values
        for(ConditionalStimulus cs : getPhaseCues()) {
            CsState csState = averageHistory.getState(cs, trials.size()); //get the state of cs after the last trial
            cs.setAlpha(csState.Alpha);
            cs.setAssociationExcitatory(csState.Ve);
            cs.setAssociationInhibitory(csState.Vi);
        }
        return averageHistory;
    }

    public void addTrialType(List<Trial> trialsToAdd) { //all trials in the param are the same (e.g. 'AB+')
        trials.addAll(trialsToAdd);
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
        for(Trial t : trials){
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

    public int getNumberOfTrials(){ return trials.size(); }

    public String toString(){
        return GuiStringConstants.getPhaseTitle(phaseId);
    }
}
