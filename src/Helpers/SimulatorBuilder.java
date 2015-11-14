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
        for(int i=0;i<tableModel.getRowCount();i++){
            Group group = groupBuilder.buildGroup(tableModel.getRow(i));
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

        private Group buildGroup(List<String> phaseRow){
            csMap = new HashMap<>();
            phases = new ArrayList<>();
            String name = phaseRow.get(0);

            for(int i=1;i<phaseRow.size();i++){
                List<PhaseStringTokenizer.TrailTypeTokens> phaseTokens = PhaseStringTokenizer.getPhaseTokens(phaseRow.get(i));
                updateCsMaps(phaseTokens);
                phases.add(PhaseParser.ParsePhase(phaseTokens, csMap));
            }

            return new Group(name, csMap, phases);
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
