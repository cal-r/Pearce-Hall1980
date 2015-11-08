package Models;

import Models.Parameters.GammaParameter;
import Models.Parameters.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class SimPhase {
    public GammaParameter gamma;
    public ArrayList<SimTrail> trails;
    private HashMap<Character, ConditionalStimulus> cues;

    public SimPhase() {
        trails = new ArrayList<>();
        cues = new HashMap<>();
        gamma = new GammaParameter();
    }

    public void SimulateTrail(int trailNum){

    }

    public void addTrailType(List<SimTrail> trailsToAdd) { //all trails in the param are the same (e.g. 'AB+')
        trails.addAll(trailsToAdd);
        SimTrail firstOfTheType = trailsToAdd.get(0);
        for(ConditionalStimulus cue : firstOfTheType.cuesPresent) {
            addCue(cue);
        }
    }

    private void addCue(ConditionalStimulus cue) {
        if(!cues.containsKey(cue.Name)) {
            cues.put(cue.Name, cue);
        }
    }

    public List<ConditionalStimulus> getCues() {
        return new ArrayList<>(cues.values());
    }

    public List<Parameter> getAllCsParameters(){
        List<Parameter> parameters = new ArrayList<>();
        for(ConditionalStimulus cs : getCues()) {
            parameters.addAll(cs.getAllParameters());
        }
        return parameters;
    }

    public List<Parameter> getAllGlobalParameters(){
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(gamma);
        return parameters;
    }
}
