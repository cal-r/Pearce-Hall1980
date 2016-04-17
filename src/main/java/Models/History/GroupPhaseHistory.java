package Models.History;


import Models.Stimulus.*;
import Models.Stimulus.Rodriguez.RodriguezStimulus;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Rokas on 09/11/2015.
 */
public class GroupPhaseHistory implements Serializable {
    private Map<String, List<StimulusState>> stimsHistoriesMap;
    private String groupName;
    private String phaseName;
    private int numberOfPeriods;
    private String description;
    private boolean isRandom;

    public GroupPhaseHistory(){
        stimsHistoriesMap = new HashMap<>();
        numberOfPeriods = 0;
    }

    public StimulusState getState(String cueName, int trialNumber) {
        if(stimsHistoriesMap.get(cueName).size() < trialNumber){
            return null;
        }
        return stimsHistoriesMap.get(cueName).get(trialNumber - 1);
    }

    public StimulusState getLastState(String cueName){
        List<StimulusState> cueHistory = stimsHistoriesMap.get(cueName);
        return cueHistory.get(cueHistory.size() - 1);
    }

    public List<StimulusState> getStimHistory(String stimName){
        return stimsHistoriesMap.get(stimName);
    }

    public void recordState(Collection<IStimulus> stims, char trialReinforcer, char phaseReinforcer){
        for(IStimulus stim : stims) {
            if(stim instanceof MultipleStimulus){
                recordMultiStimState((MultipleStimulus)stim, trialReinforcer, phaseReinforcer);
            }else {
                recordStimState(stim.getName(), stim);
            }
        }
        numberOfPeriods++;
    }

    public void recordProbeState(Probe probe) {
        recordStimState(probe.getLabel(), probe.getStimulus());
    }

    private void recordMultiStimState(MultipleStimulus multipleStimulus, char trialReinforcer, char phaseReinforcer){
        if(phaseReinforcer == '-'){
            recordStimState(multipleStimulus.getName(), multipleStimulus);
        }else{
            IConditionalStimulus stim = trialReinforcer != '-'
                    ? multipleStimulus.getStimsMap().get(trialReinforcer)
                    : multipleStimulus.getStimsMap().get(phaseReinforcer);
            recordStimState(stim.getName(), stim);
            setAlphaLink(stim.getName(), multipleStimulus.getName());
            for(char usedReinforcer : multipleStimulus.getUsedStimsMap().keySet()){
                if(usedReinforcer!=phaseReinforcer){
                    ConditionalStimulus decliningStim = multipleStimulus.getStimsMap().get(usedReinforcer);
                    recordStimStateWithNegativeLabel(decliningStim);
                    setAlphaLink(getNegativeLabel(decliningStim), multipleStimulus.getName());
                }
            }
        }
    }

    private void setAlphaLink(String cueNameWithReinforcer, String cueName){
        List<StimulusState> list = stimsHistoriesMap.get(cueNameWithReinforcer);
        StimulusState stimulusState = list.get(list.size() - 1);
        ((ConditionalStimulusState)stimulusState).AlphaLink = cueName;
    }
    
    private void recordStimStateWithNegativeLabel(IStimulus stim){
        String label = getNegativeLabel(stim);
        recordStimState(label, stim);
    }

    private String getNegativeLabel(IStimulus stim){
        return String.format("(%s)-", stim.getName());
    }
       
    private void recordStimState(String name, IStimulus stim){
        StimulusState state = createStimState(stim);
        addToMap(name, state);
    }

    private void addToMap(String stimName, StimulusState state){
        if(!stimsHistoriesMap.containsKey(stimName)){
            stimsHistoriesMap.put(stimName, new ArrayList<StimulusState>());
        }
        stimsHistoriesMap.get(stimName).add(state);
    }

    private StimulusState createStimState(IStimulus stim){
        if(stim instanceof IPHConditionalStimulus){
            return new ConditionalStimulusState((IPHConditionalStimulus)stim);
        }
        if(stim instanceof RodriguezStimulus){
            return new ConditionalStimulusState((RodriguezStimulus)stim);
        }
        return new StimulusState(stim);
    }

    public String getPhaseName(){
        return phaseName;
    }

    public int getNumberOfPeriods(){
        return numberOfPeriods;
    }

    public String getGroupName() {
        return groupName;
    }

    public Collection<String> getStimsNames(){
        return stimsHistoriesMap.keySet();
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public void setIsRandom(boolean isRandom) {
        this.isRandom = isRandom;
    }
}
