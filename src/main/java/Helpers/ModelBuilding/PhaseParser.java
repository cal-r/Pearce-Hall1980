package Helpers.ModelBuilding;

import Models.SimulatorSettings;
import Models.Stimulus.*;
import Models.GroupPhase;
import Models.Stimulus.Rodriguez.RodriguezStimulus;
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
                                        Map<String, IConditionalStimulus> csMap,
                                        int phaseId,
                                        SimulatorSettings settings,
                                        IConditionalStimulus context,
                                        Integer itiRatio) {

        TrialTypeParser trialTypeParser = new TrialTypeParser(csMap, settings, context, itiRatio);

        GroupPhase groupPhase = new GroupPhase(phaseId, getPhaseReinforcer(trialTypeTokensList));

        for(PhaseStringTokenizer.TrialTypeTokens trialType : trialTypeTokensList){
            groupPhase.addTrials(trialTypeParser.getTrials(trialType));
        }

        return groupPhase;
    }

    public static char getPhaseReinforcer(List<PhaseStringTokenizer.TrialTypeTokens> phaseTokens){
        for(PhaseStringTokenizer.TrialTypeTokens trialType : phaseTokens){
            if(trialType.reinforcer != '-'){
                return trialType.reinforcer;
            }
        }
        return '-';
    }

    private static class TrialTypeParser {

        private Map<String, IConditionalStimulus> csMap;
        private SimulatorSettings settings;
        private IConditionalStimulus context;
        private int itiRatio;

        private TrialTypeParser(Map<String, IConditionalStimulus> csMap, SimulatorSettings settings, IConditionalStimulus context, Integer itiRatio) {
            this.csMap = csMap;
            this.settings = settings;
            this.context = context;
            this.itiRatio = itiRatio;
        }

        private List<Trial> getTrials (PhaseStringTokenizer.TrialTypeTokens trialType){
            List<Trial> trials = new ArrayList<>();
            for (int i = 0; i < trialType.numberOfTrials; i++) {
                boolean isLastTrial = i == (trialType.numberOfTrials -1);
                trials.add(createTrial(trialType.reinforcer, getStims(trialType), isLastTrial, trialType.description));
            }
            return trials;
        }

        private Trial createTrial(char reinforcer, List<IStimulus> cues, boolean isLastTrial, String description){
            List<LearningPeriod> learningPeriods = new ArrayList<>();

            if(settings.ContextSimulation){
                addItiPeriods(learningPeriods);
            }

            List<IStimulus> trialCues = new ArrayList<>();
            for(IStimulus stim : cues){
                trialCues.add(stim);
            }

            learningPeriods.add(new LearningPeriod(getUsPresent(reinforcer), reinforcer, trialCues));

            if(isLastTrial && settings.ContextSimulation){
                addItiPeriods(learningPeriods);
            }

            Trial trial = new Trial(learningPeriods);

            if(ProbeBuilder.containsProbe(description)){
                trial.setProbe(ProbeBuilder.buildProbe(description, cues));
            }

            return trial;
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

        private List<IStimulus> getStims(PhaseStringTokenizer.TrialTypeTokens trialType) {
            List<IStimulus> cuesPresent = new ArrayList<>();
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
                addCompoundStim(cuesPresent, trialType.reinforcer);
            }
            return cuesPresent;
        }

        private void addCompoundStim(List<IStimulus> stims, char reinforcer){
            if(stims.size() < 2){
                return;
            }
            List<IConditionalStimulus> compounded = new ArrayList<>();
            boolean reinforcerSpecificCues = false;
            for(IStimulus stim : stims){
                if(stim instanceof ConditionalStimulus || stim instanceof RodriguezStimulus){
                    compounded.add((IConditionalStimulus) stim);
                }else if(stim instanceof MultipleStimulus){
                    if(reinforcer == '-'){
                        compounded.add((MultipleStimulus) stim);
                    }else{
                        for(IConditionalStimulus positiveStim : ((MultipleStimulus) stim).getStims(reinforcer)){
                            compounded.add(positiveStim);
                        }
                        reinforcerSpecificCues = true;
                    }
                }
            }

            CompoundStimulus compound = new CompoundStimulus(compounded, reinforcerSpecificCues);
            stims.add(compound);
        }
}
}
