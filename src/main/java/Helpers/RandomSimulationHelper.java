package Helpers;

import Constants.DefaultValuesConstants;
import Constants.TableStringConstants;
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
            for(char csName : avgHist.getCues()){
                List<GroupPhaseHistory.CsState> statesToAdd = list.get(i).getCsHistory(csName);
                List<GroupPhaseHistory.CsState> avgStates = avgHist.getCsHistory(csName);
                for(int stateId=0;stateId<avgStates.size();stateId++){
                    avgStates.get(stateId).Ve += statesToAdd.get(stateId).Ve;
                    avgStates.get(stateId).Vi += statesToAdd.get(stateId).Vi;
                    avgStates.get(stateId).Vnet += statesToAdd.get(stateId).Vnet;
                    avgStates.get(stateId).Alpha += statesToAdd.get(stateId).Alpha;
                    avgStates.get(stateId).TrailDescription = TableStringConstants.RANDOM;
                }
            }
        }
        //divide by 1000
        for(char csName : avgHist.getCues()){
            List<GroupPhaseHistory.CsState> avgStates = avgHist.getCsHistory(csName);
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
