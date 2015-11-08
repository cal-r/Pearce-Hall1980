package Models;

import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class SimTrail {
    public boolean usPresent;
    public List<ConditionalStimulus> cuesPresent;

    public SimTrail(boolean usPresent, List<ConditionalStimulus> cuesPresent){
        this.usPresent = usPresent;
        this.cuesPresent = cuesPresent;
    }

    public void simulate(double vNet){
        double lambda = usPresent ? 1 : 0;
        double capitalLambda = lambda - vNet;
        double
    }

}
