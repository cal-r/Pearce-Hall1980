package Helpers;

import Models.ConditionalStimulus;
import Models.Phase;
import Models.Trail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 04/11/2015.
 */
public class PhaseParser {

    public static Phase ParsePhase(List<PhaseStringTokenizer.TrailTypeTokens> trailTypeTokensList) {
        Phase phase = new Phase();

        HashMap<Character, ConditionalStimulus> csMap = new HashMap<>();

        for(PhaseStringTokenizer.TrailTypeTokens trailType : trailTypeTokensList){
            List<Trail> newTrails = getTrails(trailType, csMap);
            phase.addTrailType(newTrails);
        }

        return phase;
    }

    private static List<Trail> getTrails(PhaseStringTokenizer.TrailTypeTokens trailType, HashMap<Character, ConditionalStimulus> csMap) {
        List<Trail> trails = new ArrayList<>();

        Trail trail = new Trail(
                getUsPresent(trailType),
                getCuesPresent(trailType, csMap));

        for (int i = 0; i < trailType.numberOfTrails; i++){
            trails.add(trail);
        }

        return trails;
    }

    private static boolean getUsPresent(PhaseStringTokenizer.TrailTypeTokens trailType) {
        if(trailType.reinforcer == '+'){
            return true;
        }
        return false;
    }

    private static List<ConditionalStimulus> getCuesPresent(PhaseStringTokenizer.TrailTypeTokens trailType, HashMap<Character, ConditionalStimulus> csMap) {
        List<ConditionalStimulus> cuesPresent = new ArrayList<>();
        for(char cueName : trailType.cueNames){
            if(!csMap.containsKey(cueName)) {
                csMap.put(cueName, new ConditionalStimulus(cueName));
            }
            cuesPresent.add(csMap.get(cueName));
        }
        return cuesPresent;
    }

}
