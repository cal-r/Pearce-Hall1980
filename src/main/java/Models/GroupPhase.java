package Models;

import Constants.GuiStringConstants;
import Helpers.Random.RandomArrayGenerator;
import Helpers.Random.RandomSimulationHelper;
import Models.History.ConditionalStimulusState;
import Models.History.GroupPhaseHistory;
import Models.Parameters.GammaParameter;
import Models.Stimulus.ConditionalStimulus;
import Models.Stimulus.Stimulus;
import Models.Trail.Trial;

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
    private Map<String, Stimulus> stimsMap;
    private int phaseId;
    private String description;

    public GroupPhase(int phaseId) {
        this.phaseId = phaseId;
        trials = new ArrayList<>();
        stimsMap = new HashMap<>();
    }

    public GroupPhaseHistory simulateTrials(GammaParameter gamma, SimulatorSettings simulatorSettings) {
        GroupPhaseHistory history;
        if(random) {
            history = simulateTrialsRandomly(gamma, simulatorSettings);
        }else{
            history = simulateTrialsSequentially(gamma, simulatorSettings);
        }
        addInfoToHistory(history);
        return history;
    }

    private GroupPhaseHistory simulateTrialsSequentially(GammaParameter gamma, SimulatorSettings simulatorSettings){
        GroupPhaseHistory history = new GroupPhaseHistory();
        for(int i=0;i<trials.size();i++){
            Trial trial = trials.get(i);
            //The algorithm runs AFTER the trial finishes and gives the predictive value for the following trial.
            history.recordState(stimsMap.values());
            trial.simulate(this, gamma.getValue());
        }
        return history;
    }

    private GroupPhaseHistory simulateTrialsRandomly(GammaParameter gamma, SimulatorSettings simulatorSettings) {
        List<ConditionalStimulus> csCopies = getCsCopies(); //preserve initial state of CSs
        List<GroupPhaseHistory> tempHistories = new ArrayList<>(); //stores every simulation
        //sim phase 1000 times
        for(int simNum = 0;simNum< simulatorSettings.NumberOfRandomCombination; simNum++) {
            resetCues(csCopies);
            GroupPhaseHistory history = new GroupPhaseHistory();
            int[] randomArray = RandomArrayGenerator.createRandomDistinctArray(trials.size());
            for (int trialNo = 0; trialNo < trials.size(); trialNo++) {
                Trial trial = trials.get(randomArray[trialNo]);
                history.recordState(stimsMap.values());
                trial.simulate(this, gamma.getValue());
            }
            tempHistories.add(history);
        }
        //get avg history
        GroupPhaseHistory averageHistory = RandomSimulationHelper.getAverageHistory(tempHistories);

        //set cs properties to average values
        for(ConditionalStimulus cs : getPhaseCues()) {
            //get the last state of cs
            ConditionalStimulusState conditionalStimulusState = (ConditionalStimulusState) averageHistory.getState(cs.getName(), trials.size());
            cs.setAlpha(conditionalStimulusState.Alpha);
            cs.setAssociationExcitatory(conditionalStimulusState.Ve);
            cs.setAssociationInhibitory(conditionalStimulusState.Vi);
        }
        return averageHistory;
    }

    private void addInfoToHistory(GroupPhaseHistory history){
        history.setPhaseName(getTitle());
        history.setNumbetOfTrails(getNumberOfTrials());
        history.setDescription(description);
        history.setIsRandom(isRandom());
    }

    public void addTrials(List<Trial> trials) {
        this.trials.addAll(trials);
        for(Trial trial : trials) {
            updateStimsMap(trial);
        }
    }

    public void reset(){
        for(ConditionalStimulus cs : getPhaseCues()){
            cs.reset();
        }
    }

    private void resetCues(List<ConditionalStimulus> copies){
        for(ConditionalStimulus copy : copies){
            ConditionalStimulus cs = (ConditionalStimulus)stimsMap.get(copy.getName());
            cs.reset(copy);
        }
    }

    private List<ConditionalStimulus> getCsCopies(){
        List<ConditionalStimulus> copies = new ArrayList<>();
        for(ConditionalStimulus cs : getPhaseCues()){
            copies.add(cs.getCopy());
        }
        return copies;
    }

    public double calcVNetValue(){
        double vNet = 0;
        for(ConditionalStimulus cs : getPhaseCues()){
            vNet += cs.getAssociationNet();
        }
        return vNet;
    }

    public List<ConditionalStimulus> getPhaseCues() {
        List<ConditionalStimulus> cues = new ArrayList<>();
        for(Stimulus stim : stimsMap.values()){
            if(stim instanceof ConditionalStimulus)
                cues.add((ConditionalStimulus) stim);
        }
        return cues;
    }

    private void updateStimsMap(Trial trial){
        for(Stimulus stim : trial.getStims()){
            if(!stimsMap.containsKey(stim.getName())){
                stimsMap.put(stim.getName(), stim);
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

    public String getTitle(){
        return GuiStringConstants.getPhaseTitle(phaseId);
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
