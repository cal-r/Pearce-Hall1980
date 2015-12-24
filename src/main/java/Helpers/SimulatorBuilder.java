package Helpers;

import Models.ConditionalStimulus;
import Models.Group;
import Models.Parameters.CsParameterPool;
import Models.Phase;
import Models.Simulator;
import ViewModels.TrailTableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 13/11/2015.
 */
public class SimulatorBuilder {

    public static Simulator build(TrailTableModel tableModel){
        CsParameterPool csParameterPool = new CsParameterPool();
        GroupBuilder groupBuilder = new GroupBuilder(csParameterPool);

        List<Group> groups = new ArrayList<>();
        for(int gi=0;gi<tableModel.getGroupCount();gi++){
            Group group = groupBuilder.buildGroup(tableModel.getGroupName(gi), tableModel.getPhaseDescriptions(gi), tableModel.getRandomSelections(gi));
            groups.add(group);
        }

        return new Simulator(csParameterPool, groups);
    }

    private static class GroupBuilder{
        private CsParameterPool csParameterPool;

        private Map<Character, ConditionalStimulus> csMap;
        private List<Phase> phases;

        private GroupBuilder(CsParameterPool csParameterPool){
            this.csParameterPool = csParameterPool;
        }

        private Group buildGroup(String groupName, List<String> phaseDescriptions, List<Boolean> randomSelections){
            csMap = new HashMap<>();
            phases = new ArrayList<>();

            for(int i=0;i<phaseDescriptions.size();i++){
                List<PhaseStringTokenizer.TrailTypeTokens> phaseTokens = PhaseStringTokenizer.getPhaseTokens(phaseDescriptions.get(i));
                updateCsMaps(phaseTokens);
                Phase phase = PhaseParser.ParsePhase(phaseTokens, csMap, i);
                phase.setRandom(randomSelections.get(i));
                phases.add(phase);
            }

            return new Group(groupName, csMap, phases);
        }

        private void updateCsMaps(List<PhaseStringTokenizer.TrailTypeTokens> phaseTokens){
            for(PhaseStringTokenizer.TrailTypeTokens trailTypeTokens : phaseTokens){
                for(char cueName : trailTypeTokens.cueNames){
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
