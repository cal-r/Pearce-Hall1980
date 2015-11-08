package Helpers;

import Models.ConditionalStimulus;
import Models.SimPhase;
import Models.SimTrail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 04/11/2015.
 */
public class PhaseParser {

    public static SimPhase ParsePhase(List<PhaseStringTokenizer.TrailTypeTokens> trailTypeTokensList) {
        SimPhase phase = new SimPhase();

        for(PhaseStringTokenizer.TrailTypeTokens trailType : trailTypeTokensList){
            List<SimTrail> newTrails = getTrails(trailType);
            phase.addTrailType(newTrails);
        }

        return phase;
    }

    private static List<SimTrail> getTrails(PhaseStringTokenizer.TrailTypeTokens trailType) {
        List<SimTrail> trails = new ArrayList<>();

        SimTrail trail = new SimTrail(
                getUsPresent(trailType),
                getCuesPresent(trailType));

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

    private static List<ConditionalStimulus> getCuesPresent(PhaseStringTokenizer.TrailTypeTokens trailType) {
        List<ConditionalStimulus> cuesPresent = new ArrayList<>();
        for(char cueName : trailType.cueNames){
            ConditionalStimulus cue = new ConditionalStimulus(cueName);
            cuesPresent.add(cue);
        }
        return cuesPresent;
    }

}
