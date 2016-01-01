package Models.History;

import Constants.DefaultValuesConstants;
import Constants.GuiStringConstants;
import Constants.TableStringConstants;
import Models.ConditionalStimulus;
import Models.Phase;
import Models.Trail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 09/11/2015.
 */
public class PhaseHistory {

    private HashMap<Character, List<CsState>> csHistoriesMap;
    public Phase phase;
    private int trailNumber;

    public PhaseHistory(Phase phase){
        this.phase = phase;
        initHistoriesMap();
        trailNumber =0;
    }

    public List<Character> getCues(){
        return new ArrayList<>(csHistoriesMap.keySet());
    }

    public CsState getState(Character cue, int trailNumber)
    {
        return csHistoriesMap.get(cue).get(trailNumber-1);
    }

    public void recordState(Trail trail){
        trailNumber++;
        for(ConditionalStimulus cs : phase.getPhaseCues()) {
            CsState csState = new CsState();
            csState.Ve = cs.getAssociationExcitatory();
            csState.Vi = cs.getAssociationInhibitory();
            csState.Vnet = cs.getAssociationNet();
            csState.Alpha = cs.getAlpha();
            csState.TrailNumber = trailNumber;
            csState.TrailDescription = trail.toString();
            csHistoriesMap.get(cs.Name).add(csState);
        }
    }

    private void initHistoriesMap(){
        csHistoriesMap = new HashMap<>();
        for(ConditionalStimulus cs : phase.getPhaseCues()){
            csHistoriesMap.put(cs.Name, new ArrayList<CsState>());
        }
    }

    public class CsState{
        public double Ve;
        public double Vi;
        public double Vnet;
        public double Alpha;
        public int TrailNumber;
        public String TrailDescription;
    }

    public static PhaseHistory getAverageHistory(List<PhaseHistory> list){
        //sum up
        PhaseHistory avgHist = list.get(0);
        for(int i=1;i<list.size();i++){
            for(char csName : avgHist.getCues()){
                List<CsState> statesToAdd = list.get(i).csHistoriesMap.get(csName);
                List<CsState> avgStates = avgHist.csHistoriesMap.get(csName);
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
            List<CsState> avgStates = avgHist.csHistoriesMap.get(csName);
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
