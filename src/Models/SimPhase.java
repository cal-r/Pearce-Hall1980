package Models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rokas on 03/11/2015.
 */
public class SimPhase {
    private ArrayList<SimTrail> trails;
    private HashMap<Character, ConditionalStimulus> cues;
    public SimPhase() {
        trails = new ArrayList<SimTrail>();
    }

    public void addTrail(SimTrail trail) {
        trails.add(trail);
    }

    public int getTrailCount() {
        return trails.size();
    }
}
