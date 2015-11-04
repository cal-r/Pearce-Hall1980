package Models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rokas on 03/11/2015.
 */
public class SimPhase {
    public double gamma;
    public ArrayList<SimTrail> trails;
    private HashMap<Character, ConditionalStimulus> cues;

    public SimPhase() {
        trails = new ArrayList<>();
        cues = new HashMap<>();
    }

    public void AddCue(ConditionalStimulus cue) {
        cues.put(cue.Name, cue);
    }

    public ArrayList<ConditionalStimulus> GetCues() {
        return new ArrayList<>(cues.values());
    }

    public void addTrail(SimTrail trail) {
        trails.add(trail);
    }
}
