package Helpers.Random;

import Models.History.ConditionalStimulusState;
import Models.History.GroupPhaseHistory;
import Models.History.StimulusState;
import Models.Stimulus.ConditionalStimulus;
import Models.Stimulus.IConditionalStimulus;
import Models.Stimulus.IStimulus;
import Models.Stimulus.MultipleStimulus;

import java.util.List;

/**
 * Created by Rokas on 21/01/2016.
 */
public class RandomSimulationHelper {
    public static GroupPhaseHistory getAverageHistory(List<GroupPhaseHistory> list){
        int numOfRandomCombs = list.size();
        //sum up
        GroupPhaseHistory avgHist = list.get(0);
        for(int i=1;i<list.size();i++){
            for(String stimName : avgHist.getStimsNames()){
                List<StimulusState> statesToAdd = list.get(i).getStimHistory(stimName);
                List<StimulusState> avgStates = avgHist.getStimHistory(stimName);
                for(int stateId=0;stateId<avgStates.size();stateId++){
                    avgStates.get(stateId).addValues(statesToAdd.get(stateId));
                }
            }
        }

        //divide by 1000
        for(String stimName : avgHist.getStimsNames()){
            List<StimulusState> avgStates = avgHist.getStimHistory(stimName);
            for(int stateId=0;stateId<avgStates.size();stateId++){
                avgStates.get(stateId).divideValues(numOfRandomCombs);
            }
        }

        return avgHist;
    }

    public static void setCsPropertiesToAverageValues(List<IConditionalStimulus> activeStims, GroupPhaseHistory averageGroupPhaseHistory, char phaseReinforcer){
        //set cs properties to average values
        for(IStimulus stim : activeStims) {
            //get the last state of cs
            if(stim instanceof ConditionalStimulus) {
                ConditionalStimulus cs = (ConditionalStimulus) stim;
                ConditionalStimulusState conditionalStimulusState = (ConditionalStimulusState) averageGroupPhaseHistory.getLastState(cs.getName());
                cs.setAlpha(conditionalStimulusState.Alpha);
                cs.setAssociationExcitatory(conditionalStimulusState.Ve);
                cs.setAssociationInhibitory(conditionalStimulusState.Vi);
            }
            if (stim instanceof MultipleStimulus) {
                for (ConditionalStimulus cs : ((MultipleStimulus) stim).getStims(phaseReinforcer)) {
                    ConditionalStimulusState conditionalStimulusState = (ConditionalStimulusState) averageGroupPhaseHistory.getLastState(cs.getName());
                    cs.setAlpha(conditionalStimulusState.Alpha);
                    cs.setAssociationExcitatory(conditionalStimulusState.Ve);
                    cs.setAssociationInhibitory(conditionalStimulusState.Vi);
                }
            }
        }
    }
}
