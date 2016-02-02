package Helpers.ModelBuilding;

import Helpers.ModelBuilding.PhaseParser;
import Helpers.ModelBuilding.PhaseStringTokenizer;
import Models.ConditionalStimulus;
import Models.Group;
import Models.GroupPhase;
import Models.Parameters.CsParameterPool;
import Models.Simulator;
import ViewModels.TrialTableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 13/11/2015.
 */
public class SimulatorBuilder {

    public static void initSimulator(TrialTableModel tableModel, Simulator simulator){
        CsParameterPool csParameterPool = new CsParameterPool();
        GroupBuilder groupBuilder = new GroupBuilder(csParameterPool);

        List<Group> groups = new ArrayList<>();
        for(int gi=0;gi<tableModel.getGroupCount();gi++){
            Group group = groupBuilder.buildGroup(tableModel.getGroupName(gi), tableModel.getPhaseDescriptions(gi), tableModel.getRandomSelections(gi));
            groups.add(group);
        }

        simulator.setCsParameterPool(csParameterPool);
        simulator.setGroups(groups);
    }

    private static class GroupBuilder{
        private CsParameterPool csParameterPool;

        private Map<Character, ConditionalStimulus> csMap;
        private List<GroupPhase> groupPhases;

        private GroupBuilder(CsParameterPool csParameterPool){
            this.csParameterPool = csParameterPool;
        }

        private Group buildGroup(String groupName, List<String> phaseDescriptions, List<Boolean> randomSelections){
            csMap = new HashMap<>();
            groupPhases = new ArrayList<>();

            for(int i=0;i<phaseDescriptions.size();i++){
                List<PhaseStringTokenizer.TrialTypeTokens> phaseTokens = PhaseStringTokenizer.getPhaseTokens(phaseDescriptions.get(i));
                updateCsMaps(phaseTokens);
                GroupPhase groupPhase = PhaseParser.ParsePhase(phaseTokens, csMap, i);
                groupPhase.setRandom(randomSelections.get(i));
                groupPhases.add(groupPhase);
            }

            return new Group(groupName, csMap, groupPhases);
        }

        private void updateCsMaps(List<PhaseStringTokenizer.TrialTypeTokens> phaseTokens){
            for(PhaseStringTokenizer.TrialTypeTokens trialTypeTokens : phaseTokens){
                for(char cueName : trialTypeTokens.cueNames){
                    if(!csParameterPool.contains(cueName)){
                        csParameterPool.createParameters(cueName);
                    }
                    if(!csMap.containsKey(cueName)){
                        csMap.put(cueName, createCs(cueName));
                    }
                }
            }
        }

        private ConditionalStimulus createCs(char cueName){
            return new ConditionalStimulus(
                    cueName,
                    csParameterPool.getInitialAlpha(cueName),
                    csParameterPool.getSeParameter(cueName),
                    csParameterPool.getSiParamter(cueName));
        }
    }
}
