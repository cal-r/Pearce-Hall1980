package Helpers.ModelBuilding;

import Models.Parameters.Pools.CsPools.ElaboratorCsParameterPool;
import Models.Parameters.Pools.CsPools.ICsParameterPool;
import Models.Parameters.Pools.CsPools.RodriguezCsParameterPool;
import Models.SimulatorSettings;
import Models.Stimulus.*;
import Models.Group;
import Models.GroupPhase;
import Models.Parameters.Pools.CsPools.CsParameterPool;
import Models.Simulator;
import Models.Stimulus.Rodriguez.RodriguezStimulus;
import Models.Stimulus.Rodriguez.VeConditionalStimulus;
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

    public static void initSimulator(TrialTableModel tableModel, Simulator simulator) {
        SimulatorSettings settings = simulator.getSettings();
        ICsParameterPool csParameterPool = getCsParameterPool(settings);
        GroupBuilder groupBuilder = new GroupBuilder(csParameterPool, settings, tableModel);
        List<Group> groups = new ArrayList<>();
        for (int gi = 0; gi < tableModel.getGroupCount(); gi++) {
            Group group = groupBuilder.buildGroup(gi);
            groups.add(group);
        }

        simulator.setCsParameterPool(csParameterPool);
        simulator.setGroups(groups);
    }

    private static class GroupBuilder{
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

            String groupName = tableModel.getGroupName(gi);
            List<String> phaseDescriptions = tableModel.getPhaseDescriptions(gi);
            List<Boolean> randomSelections = tableModel.getRandomSelections(gi);

            if(settings.isContextSimulation()) {
                contextConfigs = tableModel.getContextConfigs(gi);
                itiRatios = tableModel.getItiRatios(gi);
            }

            csMap = new HashMap<>();
            groupPhases = new ArrayList<>();

            for(int i=0;i<phaseDescriptions.size();i++){
                String phaseDescription = phaseDescriptions.get(i);
                List<PhaseStringTokenizer.TrialTypeTokens> phaseTokens = PhaseStringTokenizer.getPhaseTokens(settings, phaseDescription);
                updateCsMapsForCues(phaseTokens);

                GroupPhase groupPhase;
                if(settings.isContextSimulation()) {
                    ContextConfig contextConfig = contextConfigs.get(i);
                    String contextName = contextConfig.getSymbol();
                    ContextParameterHelper.setContextConfigInParamsPool(contextConfig, (CsParameterPool)csParameterPool);
                    updateCsMaps(contextName, PhaseParser.getPhaseReinforcer(phaseTokens));
                    groupPhase = PhaseParser.ParsePhase(phaseTokens, csMap, i, settings, csMap.get(contextName), itiRatios.get(i));
                    phaseDescription = String.format("%s: %s", contextName, phaseDescription);
                }else{
                    groupPhase = PhaseParser.ParsePhase(phaseTokens, csMap, i, settings, null, 0);
                }

                groupPhase.setRandom(randomSelections.get(i));
                groupPhase.setDescription(phaseDescription);
                groupPhases.add(groupPhase);
            }

            Group group = new Group(groupName, groupPhases);
            return group;
        }

        private void updateCsMapsForCues(List<PhaseStringTokenizer.TrialTypeTokens> phaseTokens){
            for(PhaseStringTokenizer.TrialTypeTokens trialTypeTokens : phaseTokens) {
                for(String cueName : trialTypeTokens.cueNames) {
                    updateCsMaps(cueName, PhaseParser.getPhaseReinforcer(phaseTokens));
                }
            }
        }

        private void updateCsMaps(String cueName, char reinforcer){
            if (!csMap.containsKey(cueName)) {
                if (!csParameterPool.contains(cueName)) {
                    csParameterPool.createParameters(cueName);
                }
                if (settings.isUseDifferentUs()) {
                    csMap.put(cueName, createMultipleStimulus(cueName));
                } else if (settings.isRodriguezMode()) {
                    csMap.put(cueName, createRodriguezParameter(cueName));
                }
                else {
                    csMap.put(cueName, createCs(cueName));
                }
            }
            if (settings.isUseDifferentUs())
                ((MultipleStimulus) csMap.get(cueName)).addStimulus(reinforcer);
        }

        private ConditionalStimulus createCs(String cueName){
            if(!settings.isUseInitialVe()) {
                return new ConditionalStimulus(
                        cueName,
                        ((CsParameterPool) csParameterPool).getInitialAlpha(cueName),
                        ((CsParameterPool) csParameterPool).getSeParameter(cueName),
                        ((CsParameterPool) csParameterPool).getSiParamter(cueName));
            }else{
                return new VeConditionalStimulus(
                        cueName,
                        ((CsParameterPool) csParameterPool).getInitialAlpha(cueName),
                        ((CsParameterPool) csParameterPool).getSeParameter(cueName),
                        ((CsParameterPool) csParameterPool).getSiParamter(cueName),
                        ((ElaboratorCsParameterPool) csParameterPool).getVeParameter(cueName));
            }
        }

        private MultipleStimulus createMultipleStimulus(String cueName){
            if(!settings.isUseInitialVe()) {
                return new MultipleStimulus(
                        cueName,
                        ((CsParameterPool) csParameterPool).getInitialAlpha(cueName),
                        ((CsParameterPool) csParameterPool).getSeParameter(cueName),
                        ((CsParameterPool) csParameterPool).getSiParamter(cueName),
                        null);
            }
            return new MultipleStimulus(
                    cueName,
                    ((CsParameterPool) csParameterPool).getInitialAlpha(cueName),
                    ((CsParameterPool) csParameterPool).getSeParameter(cueName),
                    ((CsParameterPool) csParameterPool).getSiParamter(cueName),
                    ((ElaboratorCsParameterPool) csParameterPool).getVeParameter(cueName));
        }

        private RodriguezStimulus createRodriguezParameter(String cueName) {
            return new RodriguezStimulus(
                    ((RodriguezCsParameterPool)csParameterPool).getInitialAlphaParameter(cueName),
                    ((RodriguezCsParameterPool)csParameterPool).getInitialAssociationParameter(cueName),
                    ((RodriguezCsParameterPool)csParameterPool).getSalienceParameter(cueName),
                    cueName);
        }
    }

    private static ICsParameterPool getCsParameterPool(SimulatorSettings settings){
        return settings.isRodriguezMode() ? new RodriguezCsParameterPool() :
                settings.isUseInitialVe() ? new ElaboratorCsParameterPool() : new CsParameterPool();
    }
}
