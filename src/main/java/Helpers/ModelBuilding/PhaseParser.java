package Helpers.ModelBuilding;

import Models.SimulatorSettings;
import Models.Stimulus.CompoundStimulus;
import Models.Stimulus.ConditionalStimulus;
import Models.GroupPhase;
import Models.Stimulus.ContextStimulus;
import Models.Stimulus.Stimulus;
import Models.Trial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 04/11/2015.
 */
public class PhaseParser {

    public static GroupPhase ParsePhase(List<PhaseStringTokenizer.TrialTypeTokens> trialTypeTokensList,
                                        Map<Character, ConditionalStimulus> csMap,
                                        int phaseId,
                                        SimulatorSettings settings,
                                        ContextStimulus context,
                                        Integer itiRatio) {

        TrialTypeParser trialTypeParser = new TrialTypeParser(csMap, settings, context, itiRatio);

        GroupPhase groupPhase = new GroupPhase(phaseId);

        for(PhaseStringTokenizer.TrialTypeTokens trialType : trialTypeTokensList){
            List<Trial> newTrials =trialTypeParser.getTrials(trialType);
            if (settings.ContextSimulation) {
                trialTypeParser.insertContextTrials(newTrials, trialTypeParser.getUsPresent(trialType));
            }
            groupPhase.addTrials(newTrials);
        }

        return groupPhase;
    }

    private static class TrialTypeParser {

        private Map<Character, ConditionalStimulus> csMap;
        private SimulatorSettings settings;
        private ContextStimulus context;
        private int itiRatio;

        private TrialTypeParser(Map<Character, ConditionalStimulus> csMap, SimulatorSettings settings, ContextStimulus context, Integer itiRatio) {
            this.csMap = csMap;
            this.settings = settings;
            this.context = context;
            this.itiRatio = itiRatio;
        }

        private List<Trial> getTrials (PhaseStringTokenizer.TrialTypeTokens trialType){
            List<Trial> trials = new ArrayList<>();
            for (int i = 0; i < trialType.numberOfTrials; i++) {
                trials.add(new Trial(
                        getUsPresent(trialType),
                        getCuesPresent(trialType)));
            }
            return trials;
        }

        private void insertContextTrials(List<Trial> trials, boolean usPresent){
            int numberOfActiveTrials = trials.size();
            int contextTrialsAdded = 0;
            int i = 1;
            while(contextTrialsAdded < numberOfActiveTrials*itiRatio){
                for(int j=0;j<itiRatio;j++){
                    trials.add(i, new Trial(usPresent, getContextStimList()));
                    contextTrialsAdded++;
                }
                i = i + itiRatio + 1;
            }

        }

        private boolean getUsPresent(PhaseStringTokenizer.TrialTypeTokens trialType) {
            if (trialType.reinforcer == '+') {
                return true;
            }
            return false;
        }

        private List<Stimulus> getCuesPresent(PhaseStringTokenizer.TrialTypeTokens trialType) {
            List<Stimulus> cuesPresent = new ArrayList<>();
            Map<Character, Boolean> added = new HashMap<>(); //to prevent same cs being added to trial twice, e.g. in case of AAB+
            for (char cueName : trialType.cueNames) {
                if(!added.containsKey(cueName)) {
                    cuesPresent.add(csMap.get(cueName));
                    added.put(cueName, true);
                }
            }
            if(settings.CompoundResults && trialType.cueNames.length > 1){
                cuesPresent.add(createCompoundStimulus(trialType.cueNames));
            }

            return cuesPresent;
        }

        private List<Stimulus> getContextStimList(){
            List<Stimulus> contextStimList = new ArrayList<>();
            contextStimList.add(context);
            return contextStimList;
        }

        private CompoundStimulus createCompoundStimulus(char[] compoundedNames){
            List<Stimulus> compoundedStims = new ArrayList<>();
            for(char cueName : compoundedNames){
                compoundedStims.add(csMap.get(cueName));
            }
            return new CompoundStimulus(compoundedStims);
        }
    }
}
