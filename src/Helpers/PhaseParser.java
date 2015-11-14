package Helpers;

import Models.ConditionalStimulus;
import Models.Phase;
import Models.Trail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 04/11/2015.
 */
public class PhaseParser {

    public static Phase ParsePhase(List<PhaseStringTokenizer.TrailTypeTokens> trailTypeTokensList, Map<Character, ConditionalStimulus> csMap) {

        TrailTypeParser trailTypeParser = new TrailTypeParser(csMap);

        Phase phase = new Phase();

        for(PhaseStringTokenizer.TrailTypeTokens trailType : trailTypeTokensList){
            List<Trail> newTrails =trailTypeParser.getTrails(trailType);
            phase.addTrailType(newTrails);
        }

        return phase;
    }

    private static class TrailTypeParser {

        private Map<Character, ConditionalStimulus> csMap;

        private TrailTypeParser(Map < Character, ConditionalStimulus > csMap) {
            this.csMap = csMap;
        }

        private List<Trail> getTrails (PhaseStringTokenizer.TrailTypeTokens trailType){
            List<Trail> trails = new ArrayList<>();

            Trail trail = new Trail(
                    getUsPresent(trailType),
                    getCuesPresent(trailType));

            for (int i = 0; i < trailType.numberOfTrails; i++) {
                trails.add(trail);
            }

            return trails;
        }

        private boolean getUsPresent(PhaseStringTokenizer.TrailTypeTokens trailType) {
            if (trailType.reinforcer == '+') {
                return true;
            }
            return false;
        }

        private List<ConditionalStimulus> getCuesPresent(PhaseStringTokenizer.TrailTypeTokens trailType) {
            List<ConditionalStimulus> cuesPresent = new ArrayList<>();
            for (char cueName : trailType.cueNames) {
                cuesPresent.add(csMap.get(cueName));
            }
            return cuesPresent;
        }
    }
}
