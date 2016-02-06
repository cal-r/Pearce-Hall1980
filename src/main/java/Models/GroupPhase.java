package Models;

import Constants.DefaultValuesConstants;
import Constants.GuiStringConstants;
import Helpers.Random.RandomArrayGenerator;
import Helpers.Random.RandomSimulationHelper;
import Models.History.CsState;
import Models.History.GroupPhaseHistory;
import Models.Parameters.GammaParameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 03/11/2015.
 */
public class GroupPhase implements Serializable {
    public ArrayList<Trial> trials;
    private boolean random;
    private Map<Character, ConditionalStimulus> csMap;
    private Map<ConditionalStimulus, Integer> csOrderingMap;
    private int phaseId;
    private int orderCounter;

    public GroupPhase(int phaseId) {
        this.phaseId = phaseId;
        trials = new ArrayList<>();
        csMap = new HashMap<>();
        csOrderingMap = new HashMap<>();
        orderCounter = 0;
    }

    public GroupPhaseHistory simulateTrials(GammaParameter gamma, SimulatorSettings simulatorSettings) {
        if(random){
            return simulateTrialsRandomly(gamma, simulatorSettings);
        }
        return simulateTrialsSequentially(gamma, simulatorSettings);
    }

    private GroupPhaseHistory simulateTrialsSequentially(GammaParameter gamma, SimulatorSettings simulatorSettings){
        GroupPhaseHistory history = new GroupPhaseHistory(this);
        for(int i=0;i<trials.size();i++){
            Trial trial = trials.get(i);
            //The algorithm runs AFTER the trial finishes and gives the predictive value for the following trial.
            history.recordState(trial);
            trial.simulate(calcVNet(), gamma.getValue());
        }
        return history;
    }

    private GroupPhaseHistory simulateTrialsRandomly(GammaParameter gamma, SimulatorSettings simulatorSettings) {
        List<ConditionalStimulus> csCopies = getCsCopies(); //preserve initial state of CSs
        List<GroupPhaseHistory> tempHistories = new ArrayList<>(); //stores every simulation
        //sim phase 1000 times
        for(int simNum = 0;simNum< simulatorSettings.NumberOfRandomCombination; simNum++) {
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

    public void addTrialType(List<Trial> trailType) { //all trials in the param are the same (e.g. 'AB+')
        trials.addAll(trailType);
        Trial firstOfTheType = trailType.get(0);
        updateCsMap(firstOfTheType);
        updateOrderingMap(firstOfTheType);

    }

    public void reset(){
        for(ConditionalStimulus cs : getPhaseCues()){
            cs.reset();
        }
    }

    private void resetCues(List<ConditionalStimulus> copies){
        for(ConditionalStimulus copy : copies){
            csMap.get(copy.Name).reset(copy);
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
        return new ArrayList<>(csMap.values());
    }

    private void updateCsMap(Trial trial){
        for(ConditionalStimulus cs : trial.cuesPresent){
            if(!csMap.containsKey(cs.Name)){
                csMap.put(cs.Name, cs);
            }
        }
    }

    private void updateOrderingMap(Trial trial){
        for (ConditionalStimulus cs : trial.cuesPresent) {
            if(!csOrderingMap.containsKey(cs)) {
                csOrderingMap.put(cs, orderCounter++);
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

    public int getCsOrder(ConditionalStimulus cs){
        return csOrderingMap.get(cs);
    }
}
