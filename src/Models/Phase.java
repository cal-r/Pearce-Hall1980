package Models;

import Models.Parameters.GammaParameter;
import Models.Parameters.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class Phase {
    public GammaParameter gamma;
    public ArrayList<Trail> trails;
    private HashMap<Character, ConditionalStimulus> cues;

    public Phase() {
        trails = new ArrayList<>();
        cues = new HashMap<>();
        gamma = new GammaParameter();
    }

    public void simulateTrail(int trailNum){
        trails.get(trailNum).simulate(calcVNet(), gamma.getValue());
    }

    public void addTrailType(List<Trail> trailsToAdd) { //all trails in the param are the same (e.g. 'AB+')
        trails.addAll(trailsToAdd);
        Trail firstOfTheType = trailsToAdd.get(0);
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

    private double calcVNet(){
        double vNet = 0;
        for(ConditionalStimulus cs : getCues()){
            vNet += cs.getAssociationNet();
        }
        return vNet;
    }
}
