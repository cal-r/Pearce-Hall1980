package Helpers.ModelBuilding;

import Models.Parameters.Pools.CsPools.ICsParameterPool;
import Models.Parameters.Pools.CsPools.RodriguezCsParameterPool;
import Models.SimulatorSettings;
import Models.Stimulus.*;
import Models.Group;
import Models.GroupPhase;
import Models.Parameters.Pools.CsPools.CsParameterPool;
import Models.Simulator;
import Models.Stimulus.Rodriguez.RodriguezStimulus;
import ViewModels.TableModels.TrialTableModel;
import _from_RW_simulator.ContextConfig;

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
        ICsParameterPool csParameterPool = settings.RodriguezMode ? new RodriguezCsParameterPool() : new CsParameterPool();
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
        private CsParameterPool contextParameters;
        private ICsParameterPool csParameterPool;

        private SimulatorSettings settings;
        private Map<String, IConditionalStimulus> csMap;
        private List<GroupPhase> groupPhases;
        private TrialTableModel tableModel;

        private GroupBuilder(ICsParameterPool csParameterPool, SimulatorSettings settings, TrialTableModel tableModel){
            this.csParameterPool = csParameterPool;
            this.settings = settings;
            this.tableModel = tableModel;
        }

        private Group buildGroup(int gi){
            List<ContextConfig> contextConfigs = new ArrayList<>();
            List<Integer> itiRatios = new ArrayList<>();
            contextParameters = new CsParameterPool();

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
                List<PhaseStringTokenizer.TrialTypeTokens> phaseTokens = PhaseStringTokenizer.getPhaseTokens(settings, phaseDescription);
                updateCsMaps(phaseTokens, PhaseParser.getPhaseReinforcer(phaseTokens));
                GroupPhase groupPhase;

                if(settings.ContextSimulation) {
                    ContextConfig contextConfig = contextConfigs.get(i);
                    updateCsMap(contextConfig);
                    groupPhase = PhaseParser.ParsePhase(phaseTokens, csMap, i, settings, csMap.get(contextConfig.getSymbol()), itiRatios.get(i));
                }else{
                    groupPhase = PhaseParser.ParsePhase(phaseTokens, csMap, i, settings, null, 0);
                }

                groupPhase.setRandom(randomSelections.get(i));
                groupPhase.setDescription(phaseDescription);
                groupPhases.add(groupPhase);
            }

            Group group = new Group(groupName, groupPhases);
            if(settings.ContextSimulation){
                group.setContextParameterPool(contextParameters);
            }
            return group;
        }

        private void updateCsMaps(List<PhaseStringTokenizer.TrialTypeTokens> phaseTokens, char reinforcer){
            for(PhaseStringTokenizer.TrialTypeTokens trialTypeTokens : phaseTokens){
                for(String cueName : trialTypeTokens.cueNames){
                    if (!csMap.containsKey(cueName)) {
                        if (!csParameterPool.contains(cueName)) {
                            csParameterPool.createParameters(cueName);
                        }
                        if (settings.UseDifferentUs) {
                            csMap.put(cueName, createMultipleStimulus(cueName));
                        } else if (settings.RodriguezMode) {
                            csMap.put(cueName, createRodriguezParameter(cueName));
                        } else {
                            csMap.put(cueName, createCs(cueName));
                        }
                    }
                    if (settings.UseDifferentUs)
                        ((MultipleStimulus) csMap.get(cueName)).addStimulus(reinforcer);
                }
            }
        }

        private void updateCsMap(ContextConfig contextConfig){
            if(!csMap.containsKey(contextConfig.getSymbol())){
                csMap.put(contextConfig.getSymbol(), ContextBuilder.buildContext(contextConfig, contextParameters));
            }
        }

        private ConditionalStimulus createCs(String cueName){
            return new ConditionalStimulus(
                    cueName,
                    ((CsParameterPool)csParameterPool).getInitialAlpha(cueName),
                    ((CsParameterPool)csParameterPool).getSeParameter(cueName),
                    ((CsParameterPool)csParameterPool).getSiParamter(cueName));
        }

        private MultipleStimulus createMultipleStimulus(String cueName){
            return new MultipleStimulus(
                    cueName,
                    ((CsParameterPool)csParameterPool).getInitialAlpha(cueName),
                    ((CsParameterPool)csParameterPool).getSeParameter(cueName),
                    ((CsParameterPool)csParameterPool).getSiParamter(cueName));
        }

        private RodriguezStimulus createRodriguezParameter(String cueName) {
            return new RodriguezStimulus(
                    ((RodriguezCsParameterPool)csParameterPool).getInitialAlphaParameter(cueName),
                    ((RodriguezCsParameterPool)csParameterPool).getInitialAssociationParameter(cueName),
                    ((RodriguezCsParameterPool)csParameterPool).getSalienceParameter(cueName),
                    cueName);
        }
    }
}
