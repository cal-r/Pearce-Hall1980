package Helpers.ModelBuilding;

import Models.SimulatorSettings;
import Models.Stimulus.ConditionalStimulus;
import Models.Group;
import Models.GroupPhase;
import Models.Parameters.CsParameterPool;
import Models.Simulator;
import Models.Stimulus.ContextStimulus;
import ViewModels.TableModels.TrialTableModel;
import _from_RW_simulator.ContextConfig;

import javax.naming.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 13/11/2015.
 */
public class SimulatorBuilder {

    public static void initSimulator(TrialTableModel tableModel, Simulator simulator){
        SimulatorSettings settings = simulator.getSettings();
        CsParameterPool csParameterPool = new CsParameterPool();
        GroupBuilder groupBuilder = new GroupBuilder(csParameterPool, settings, tableModel);
        List<Group> groups = new ArrayList<>();
        for(int gi=0;gi<tableModel.getGroupCount();gi++){
            Group group = groupBuilder.buildGroup(gi);
            groups.add(group);
        }

        simulator.setCsParameterPool(csParameterPool);
        simulator.setGroups(groups);
    }

    private static class GroupBuilder{
        private CsParameterPool csParameterPool;

        private SimulatorSettings settings;
        private Map<String, ConditionalStimulus> csMap;
        private List<GroupPhase> groupPhases;
        private TrialTableModel tableModel;

        private GroupBuilder(CsParameterPool csParameterPool, SimulatorSettings settings, TrialTableModel tableModel){
            this.csParameterPool = csParameterPool;
            this.settings = settings;
            this.tableModel = tableModel;
        }

        private Group buildGroup(int gi){
            List<ContextConfig> contextConfigs = new ArrayList<>();
            List<Integer> itiRatios = new ArrayList<>();

            String groupName = tableModel.getGroupName(gi);
            List<String> phaseDescriptions = tableModel.getPhaseDescriptions(gi);
            List<Boolean> randomSelections = tableModel.getRandomSelections(gi);

            if(settings.ContextSimulation) {
                contextConfigs = tableModel.getContextConfigs(gi);
                itiRatios = tableModel.getItiRatios(gi);
            }

            csMap = new HashMap<>();
            groupPhases = new ArrayList<>();

            for(int i=0;i<phaseDescriptions.size();i++){
                String phaseDescription = phaseDescriptions.get(i);
                List<PhaseStringTokenizer.TrialTypeTokens> phaseTokens = PhaseStringTokenizer.getPhaseTokens(phaseDescription);
                updateCsMaps(phaseTokens);
                GroupPhase groupPhase;

                if(settings.ContextSimulation) {
                    ContextConfig contextConfig = contextConfigs.get(i);
                    updateCsMap(contextConfig);
                    ContextStimulus context = (ContextStimulus)csMap.get(contextConfig.getSymbol());
                    groupPhase = PhaseParser.ParsePhase(phaseTokens, csMap, i, settings, context, itiRatios.get(i));
                }else{
                    groupPhase = PhaseParser.ParsePhase(phaseTokens, csMap, i, settings, null, 0);
                }

                groupPhase.setRandom(randomSelections.get(i));
                groupPhase.setDescription(phaseDescription);
                groupPhases.add(groupPhase);
            }

            return new Group(groupName, groupPhases);
        }

        private void updateCsMaps(List<PhaseStringTokenizer.TrialTypeTokens> phaseTokens){
            for(PhaseStringTokenizer.TrialTypeTokens trialTypeTokens : phaseTokens){
                for(String cueName : trialTypeTokens.cueNames){
                    if(!csParameterPool.contains(cueName)){
                        csParameterPool.createParameters(cueName);
                    }
                    if(!csMap.containsKey(cueName)){
                        csMap.put(cueName, createCs(cueName));
                    }
                }
            }
        }

        private void updateCsMap(ContextConfig contextConfig){
            if(!csMap.containsKey(contextConfig.getSymbol())){
                csMap.put(contextConfig.getSymbol(), ContextBuilder.buildContext(contextConfig));
            }
        }

        private ConditionalStimulus createCs(String cueName){
            return new ConditionalStimulus(
                    cueName,
                    csParameterPool.getInitialAlpha(cueName),
                    csParameterPool.getSeParameter(cueName),
                    csParameterPool.getSiParamter(cueName));
        }
    }
}
