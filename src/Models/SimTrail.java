package Models;

import java.util.ArrayList;

/**
 * Created by Rokas on 03/11/2015.
 */
public class SimTrail {
    public boolean usPresent;
    public ArrayList<ConditionalStimulus> cuesPresent;

    public SimTrail(boolean usPresent, ArrayList<ConditionalStimulus> cuesPresent){
        this.usPresent = usPresent;
        this.cuesPresent = cuesPresent;
    }


}
