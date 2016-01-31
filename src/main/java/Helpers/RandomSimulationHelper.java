package Helpers;

import Constants.DefaultValuesConstants;
import Constants.GuiStringConstants;
import Models.ConditionalStimulus;
import Models.History.CsState;
import Models.History.GroupPhaseHistory;

import java.util.List;

/**
 * Created by Rokas on 21/01/2016.
 */
public class RandomSimulationHelper {
    public static GroupPhaseHistory getAverageHistory(List<GroupPhaseHistory> list){
        //sum up
        GroupPhaseHistory avgHist = list.get(0);
        for(int i=1;i<list.size();i++){
            for(ConditionalStimulus cs : avgHist.getCues()){
                List<CsState> statesToAdd = list.get(i).getCsHistory(cs);
                List<CsState> avgStates = avgHist.getCsHistory(cs);
                for(int stateId=0;stateId<avgStates.size();stateId++){
                    avgStates.get(stateId).Ve += statesToAdd.get(stateId).Ve;
                    avgStates.get(stateId).Vi += statesToAdd.get(stateId).Vi;
                    avgStates.get(stateId).Vnet += statesToAdd.get(stateId).Vnet;
                    avgStates.get(stateId).Alpha += statesToAdd.get(stateId).Alpha;
                    avgStates.get(stateId).TrialDescription = GuiStringConstants.RANDOM;
                }
            }
        }
        //divide by 1000
        for(ConditionalStimulus cs : avgHist.getCues()){
            List<CsState> avgStates = avgHist.getCsHistory(cs);
            for(int stateId=0;stateId<avgStates.size();stateId++){
                avgStates.get(stateId).Ve /= DefaultValuesConstants.NUMBER_OF_RANDOM_COMBINATIONS;
                avgStates.get(stateId).Vi /= DefaultValuesConstants.NUMBER_OF_RANDOM_COMBINATIONS;
                avgStates.get(stateId).Vnet /= DefaultValuesConstants.NUMBER_OF_RANDOM_COMBINATIONS;
                avgStates.get(stateId).Alpha /= DefaultValuesConstants.NUMBER_OF_RANDOM_COMBINATIONS;
            }
        }

        return avgHist;
    }
}
