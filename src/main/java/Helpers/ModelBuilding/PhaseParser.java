package Helpers.ModelBuilding;

import Helpers.ListCaster;
import Models.SimulatorSettings;
import Models.Stimulus.CompoundStimulus;
import Models.Stimulus.ConditionalStimulus;
import Models.GroupPhase;
import Models.Stimulus.ContextStimulus;
import Models.Stimulus.Stimulus;
import Models.Trail.ItiPeriod;
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
                boolean isLastTrial = i == (trialType.numberOfTrials -1);
                trials.add(createTrial(trialType.reinforcer, getStims(trialType), isLastTrial));
            }
            return trials;
        }

        private Trial createTrial(char reinforcer, List<Stimulus> cues, boolean isLastTrial){
            List<LearningPeriod> learningPeriods = new ArrayList<>();

            if(settings.ContextSimulation){
                addItiPeriods(learningPeriods);
            }

            learningPeriods.add(new LearningPeriod(getUsPresent(reinforcer), reinforcer, cues));

            if(isLastTrial && settings.ContextSimulation){
                addItiPeriods(learningPeriods);
            }

            return new Trial(learningPeriods);
        }

        private void addItiPeriods(List<LearningPeriod> learningPeriods){
            for(int i=0;i<itiRatio;i++) {
                learningPeriods.add(new ItiPeriod(context));
            }
        }

        private boolean getUsPresent(char reinforcer) {
            if (reinforcer != '-') {
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

            if(settings.ContextSimulation){
                cuesPresent.add(context);
            }
            if(settings.CompoundResults) {
                addCompoundStim(cuesPresent);
            }
            return cuesPresent;
        }

        private void addCompoundStim(List<Stimulus> stims){
            if(stims.size() < 2){
                return;
            }
            //copy and cast
            List<Stimulus> compounded = ListCaster.cast(new ArrayList<>(stims));
            CompoundStimulus compound = new CompoundStimulus(compounded);
            stims.add(compound);
        }
    }
}
