package Helpers.Random;

import Models.History.GroupPhaseHistory;
import Models.History.StimulusState;

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
}
