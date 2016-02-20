package Helpers.ModelBuilding;

import Models.SimulatorSettings;
import Models.Stimulus.CompoundStimulus;
import Models.Stimulus.ConditionalStimulus;
import Models.GroupPhase;
import Models.Stimulus.ContextStimulus;
import Models.Stimulus.Stimulus;
import Models.Trail.LearningPeriod;
import Models.Trail.Trial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 04/11/2015.
 */
public class PhaseParser {

    public static GroupPhase ParsePhase(List<PhaseStringTokenizer.TrialTypeTokens> trialTypeTokensList,
                                        Map<String, ConditionalStimulus> csMap,
                                        int phaseId,
                                        SimulatorSettings settings,
                                        ContextStimulus context,
                                        Integer itiRatio) {

        TrialTypeParser trialTypeParser = new TrialTypeParser(csMap, settings, context, itiRatio);

        GroupPhase groupPhase = new GroupPhase(phaseId);

        for(PhaseStringTokenizer.TrialTypeTokens trialType : trialTypeTokensList){
            groupPhase.addTrials(trialTypeParser.getTrials(trialType));
        }

        return groupPhase;
    }

    private static class TrialTypeParser {

        private Map<String, ConditionalStimulus> csMap;
        private SimulatorSettings settings;
        private ContextStimulus context;
        private int itiRatio;

        private TrialTypeParser(Map<String, ConditionalStimulus> csMap, SimulatorSettings settings, ContextStimulus context, Integer itiRatio) {
            this.csMap = csMap;
            this.settings = settings;
            this.context = context;
            this.itiRatio = itiRatio;
        }

        private List<Trial> getTrials (PhaseStringTokenizer.TrialTypeTokens trialType){
            List<Trial> trials = new ArrayList<>();
            for (int i = 0; i < trialType.numberOfTrials; i++) {
                trials.add(createTrial(getUsPresent(trialType), getStims(trialType)));
            }
            return trials;
        }

        private Trial createTrial(boolean usPresent, List<Stimulus> cues){
            List<LearningPeriod> learningPeriods = new ArrayList<>();

            if(settings.ContextSimulation){
                for(int i=0;i<itiRatio;i++) {
                    learningPeriods.add(new LearningPeriod(usPresent, getContextStimList()));
                }
            }

            learningPeriods.add(new LearningPeriod(usPresent, cues));

            return new Trial(learningPeriods);
        }

        private boolean getUsPresent(PhaseStringTokenizer.TrialTypeTokens trialType) {
            if (trialType.reinforcer == '+') {
                return true;
            }
            return false;
        }

        private List<Stimulus> getStims(PhaseStringTokenizer.TrialTypeTokens trialType) {
            List<Stimulus> cuesPresent = new ArrayList<>();
            Map<String, Boolean> added = new HashMap<>(); //to prevent same cs being added to trial twice, e.g. in case of AAB+
            for (String cueName : trialType.cueNames) {
                if(!added.containsKey(cueName)) {
                    cuesPresent.add(csMap.get(cueName));
                    added.put(cueName, true);
                }
            }
            Stimulus compound = createCompoundStimulus(trialType.cueNames);
            if(compound!=null){
                cuesPresent.add(compound);
            }

            return cuesPresent;
        }

        private List<Stimulus> getContextStimList(){
            List<Stimulus> contextStimList = new ArrayList<>();
            contextStimList.add(context);
            return contextStimList;
        }

        private CompoundStimulus createCompoundStimulus(String[] compoundedNames){
            if(compoundedNames.length < 2 && !settings.ContextSimulation || compoundedNames.length < 1){
                return null;
            } 

            List<Stimulus> compoundedStims = new ArrayList<>();
            if(settings.ContextSimulation){
                compoundedStims.add(context);
            }
            for(String cueName : compoundedNames){
                compoundedStims.add(csMap.get(cueName));
            }
            return new CompoundStimulus(compoundedStims);
        }
    }
}
