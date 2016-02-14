package Helpers.ModelBuilding;

import Models.Stimulus.ConditionalStimulus;
import Models.GroupPhase;
import Models.Trial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 04/11/2015.
 */
public class PhaseParser {

    public static GroupPhase ParsePhase(List<PhaseStringTokenizer.TrialTypeTokens> trialTypeTokensList, Map<Character, ConditionalStimulus> csMap, int phaseId) {

        TrialTypeParser trialTypeParser = new TrialTypeParser(csMap);

        GroupPhase groupPhase = new GroupPhase(phaseId);

        for(PhaseStringTokenizer.TrialTypeTokens trialType : trialTypeTokensList){
            List<Trial> newTrials =trialTypeParser.getTrials(trialType);
            groupPhase.addTrialType(newTrials);
        }

        return groupPhase;
    }

    private static class TrialTypeParser {

        private Map<Character, ConditionalStimulus> csMap;

        private TrialTypeParser(Map < Character, ConditionalStimulus > csMap) {
            this.csMap = csMap;
        }

        private List<Trial> getTrials (PhaseStringTokenizer.TrialTypeTokens trialType){
            List<Trial> trials = new ArrayList<>();

            Trial trial = new Trial(
                    getUsPresent(trialType),
                    getCuesPresent(trialType));

            for (int i = 0; i < trialType.numberOfTrials; i++) {
                trials.add(trial);
            }

            return trials;
        }

        private boolean getUsPresent(PhaseStringTokenizer.TrialTypeTokens trialType) {
            if (trialType.reinforcer == '+') {
                return true;
            }
            return false;
        }

        private List<ConditionalStimulus> getCuesPresent(PhaseStringTokenizer.TrialTypeTokens trialType) {
            List<ConditionalStimulus> cuesPresent = new ArrayList<>();
            Map<Character, Boolean> added = new HashMap<>(); //to prevent same cs being added to trial twice, e.g. in case of AAB+
            for (char cueName : trialType.cueNames) {
                if(!added.containsKey(cueName)) {
                    cuesPresent.add(csMap.get(cueName));
                    added.put(cueName, true);
                }
            }
            return cuesPresent;
        }
    }
}
