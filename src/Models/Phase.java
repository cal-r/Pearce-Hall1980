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
    public ArrayList<Trail> trails;

    public Phase() {
        trails = new ArrayList<>();
    }

    public PhaseHistory simulateTrails(GammaParameter gamma)
    {
        PhaseHistory history = new PhaseHistory(this);
        for(Trail trail : trails) {
            trail.simulate(calcVNet(), gamma.getValue());
            history.recordState();
        }
        return history;
    }

    public void addTrailType(List<Trail> trailsToAdd) { //all trails in the param are the same (e.g. 'AB+')
        trails.addAll(trailsToAdd);
    }

    private double calcVNet(){
        return - 69;
    }
}
