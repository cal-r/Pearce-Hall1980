package Models;

import Constants.GuiStringConstants;
import Helpers.Random.RandomArrayGenerator;
import Helpers.Random.RandomSimulationHelper;
import Models.History.ConditionalStimulusState;
import Models.History.GroupPhaseHistory;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.Stimulus.*;
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
    private Map<String, IStimulus> stimsMap;
    private int phaseId;
    private char phaseReinforcer;
    private String description;

    public GroupPhase(int phaseId, char phaseReinforcer) {
        this.phaseId = phaseId;
        this.phaseReinforcer = phaseReinforcer;
        trials = new ArrayList<>();
        stimsMap = new HashMap<>();
    }

    private GroupPhaseHistory history;

    public GroupPhaseHistory simulateTrials(GlobalParameterPool globalParams, SimulatorSettings simulatorSettings) {
        history = new GroupPhaseHistory();
        if(random) {
            simulateTrialsRandomly(globalParams, simulatorSettings);
        }else{
            simulateTrialsSequentially(globalParams);
        }
        addInfoToHistory(history);
        return history;
    }

    private void simulateTrialsSequentially(GlobalParameterPool globalParams){
        for(int i=0;i<trials.size();i++){
            Trial trial = trials.get(i);
            trial.simulate(history, globalParams, phaseId);
        }
    }

    private void simulateTrialsRandomly(GlobalParameterPool globalParams, SimulatorSettings simulatorSettings) {
        List<IConditionalStimulus> csCopies = getCsCopies(); //preserve initial state of CSs
        List<GroupPhaseHistory> tempHistories = new ArrayList<>(); //stores every simulation
        for(int simNum = 0;simNum< simulatorSettings.NumberOfRandomCombination; simNum++) {
            resetCues(csCopies);
            history = new GroupPhaseHistory();
            int[] randomArray = RandomArrayGenerator.createRandomDistinctArray(trials.size());
            for (int trialNo = 0; trialNo < trials.size(); trialNo++) {
                Trial trial = trials.get(randomArray[trialNo]);
                trial.simulate(history, globalParams, phaseId);
            }
            tempHistories.add(history);
        }
        //get avg history
        GroupPhaseHistory averageHistory = RandomSimulationHelper.getAverageHistory(tempHistories);

        RandomSimulationHelper.setCsPropertiesToAverageValues(getPhaseCues(), averageHistory, phaseReinforcer);

        history = averageHistory;
    }

    private void addInfoToHistory(GroupPhaseHistory history){
        history.setPhaseName(getTitle());
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
        for(IStimulus stim : getPhaseCues()){
            if(stim instanceof ConditionalStimulus){
                ((ConditionalStimulus)stim).reset();
            }

            if (stim instanceof MultipleStimulus) {
                for (ConditionalStimulus cs : ((MultipleStimulus) stim).getAllStims()) {
                    cs.reset();
                }
            }
        }
    }

    private void resetCues(List<IConditionalStimulus> copies){
        for(IConditionalStimulus copy : copies){
            IConditionalStimulus cs = (IConditionalStimulus)stimsMap.get(copy.getName());
            cs.reset(copy);
        }
    }

    private List<IConditionalStimulus> getCsCopies(){
        List<IConditionalStimulus> copies = new ArrayList<>();
        for(IConditionalStimulus cs : getPhaseCues()){
            copies.add(cs.getCopy());
        }
        return copies;
    }

    public List<IConditionalStimulus> getPhaseCues() {
        List<IConditionalStimulus> cues = new ArrayList<>();
        for (IStimulus stim : stimsMap.values()){
            if(stim instanceof IConditionalStimulus)
                cues.add((IConditionalStimulus)stim);
        }
        return cues;
    }

    private void updateStimsMap(Trial trial){
        for(IStimulus stim : trial.getStims()){
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

    public int getPhaseId() {
        return phaseId;
    }

    public String getTitle(){
        return GuiStringConstants.getPhaseTitle(phaseId);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public char getPhaseReinforcer() {
        return phaseReinforcer;
    }
}
