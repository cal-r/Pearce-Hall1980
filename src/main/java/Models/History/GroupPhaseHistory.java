package Models.History;


import Models.Stimulus.*;

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

    public void recordState(Collection<IStimulus> stims, char phaseReinforcer){
        for(IStimulus stim : stims) {
            if(stim instanceof MultipleStimulus){
                recordMultiStimState((MultipleStimulus)stim, phaseReinforcer);
            }else {
                recordStimState(stim.getName(), stim);
            }
        }
        numberOfPeriods++;
    }

    public void recordProbeState(Probe probe) {
        recordStimState(probe.getTrialTypeDescription(), probe.getStimulus());
    }

    private void recordMultiStimState(MultipleStimulus multipleStimulus, char phaseReinforcer){
        if(phaseReinforcer == '-'){
            for(IConditionalStimulus cs : multipleStimulus.getAllStims()){
                recordStimStateWithNegativeLabel(cs);
            }
            if(multipleStimulus.getAllStims().size()>1) {
                recordStimStateWithNegativeLabel(multipleStimulus);
            }
        }else{
            IConditionalStimulus stim = multipleStimulus.getStimsMap().get(phaseReinforcer);
            recordStimState(stim.getName(), stim);
            for(char usedReinforcer : multipleStimulus.getUsedStimsMap().keySet()){
                if(usedReinforcer!=phaseReinforcer){
                    recordStimStateWithNegativeLabel(multipleStimulus.getStimsMap().get(usedReinforcer));
                }
            }
        }
    }
    
    private void recordStimStateWithNegativeLabel(IStimulus stim){
        String label = String.format("(%s)-", stim.getName());
        recordStimState(label, stim);
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
        if(stim instanceof ConditionalStimulus){
            return new ConditionalStimulusState((ConditionalStimulus)stim);
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
