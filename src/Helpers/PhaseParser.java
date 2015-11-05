package Helpers;

import Models.ConditionalStimulus;
import Models.SimPhase;
import Models.SimTrail;

import java.util.ArrayList;

/**
 * Created by Rokas on 04/11/2015.
 */
public class PhaseParser {

    public static SimPhase ParsePhase(ArrayList<PhaseStringTokenizer.TrailTypeTokens> trailTypeTokensList) {
        SimPhase phase = new SimPhase();

        for(PhaseStringTokenizer.TrailTypeTokens trailType : trailTypeTokensList){
            ArrayList<SimTrail> newTrails = GetTrails(trailType);
            phase.addTrailType(newTrails);
        }

        return phase;
    }

    private static ArrayList<SimTrail> GetTrails(PhaseStringTokenizer.TrailTypeTokens trailType) {
        ArrayList<SimTrail> trails = new ArrayList<>();

        SimTrail trail = new SimTrail(
                GetUsPresent(trailType),
                GetCuesPresent(trailType));

        for (int i = 0; i < trailType.numberOfTrails; i++){
            trails.add(trail);
        }

        return trails;
    }

    private static boolean GetUsPresent(PhaseStringTokenizer.TrailTypeTokens trailType) {
        if(trailType.reinforcer == '+'){
            return true;
        }
        return false;
    }

    private static ArrayList<ConditionalStimulus> GetCuesPresent(PhaseStringTokenizer.TrailTypeTokens trailType) {
        ArrayList<ConditionalStimulus> cuesPresent = new ArrayList<>();
        for(char cueName : trailType.cueNames){
            ConditionalStimulus cue = new ConditionalStimulus(cueName);
            cuesPresent.add(cue);
        }
        return cuesPresent;
    }

}
