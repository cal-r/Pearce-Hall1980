package Models.History;


import Models.Stimulus.ConditionalStimulus;
import Models.Stimulus.Stimulus;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Rokas on 09/11/2015.
 */
public class GroupPhaseHistory implements Serializable {
    private Map<String, List<StimulusState>> stimsHistoriesMap;
    private String groupName;
    private String phaseName;
    private int numbetOfTrails;
    private String description;
    private boolean isRandom;

    public GroupPhaseHistory(){
        stimsHistoriesMap = new HashMap<>();
    }

    public StimulusState getState(String cueName, int trialNumber) {
        return stimsHistoriesMap.get(cueName).get(trialNumber - 1);
    }

    public List<StimulusState> getStimHistory(String stimName){
        return stimsHistoriesMap.get(stimName);
    }

    public void recordState(Collection<Stimulus> stims){
        for(Stimulus stim : stims) {
            StimulusState state = createStimState(stim);
            addToMap(stim.getName(), state);
        }
    }

    private void addToMap(String stimName, StimulusState state){
        if(!stimsHistoriesMap.containsKey(stimName)){
            stimsHistoriesMap.put(stimName, new ArrayList<StimulusState>());
        }
        stimsHistoriesMap.get(stimName).add(state);
    }

    private StimulusState createStimState(Stimulus stim){
        if(stim instanceof ConditionalStimulus){
            return new ConditionalStimulusState((ConditionalStimulus)stim);
        }
        return new StimulusState(stim);
    }

    public String getPhaseName(){
        return phaseName;
    }

    public int getNumberOfTrials(){
        return numbetOfTrails;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setNumbetOfTrails(int numbetOfTrails) {
        this.numbetOfTrails = numbetOfTrails;
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
