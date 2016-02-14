package Models.Stimulus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rokas on 14/02/2016.
 */
public class CompoundStimulus implements Stimulus, Serializable {

    private List<Stimulus> compounded;

    public CompoundStimulus(List<Stimulus> compounded){
        this.compounded = compounded;
    }

    @Override
    public double getAssociationNet() {
        double vnet = 0;
        for(Stimulus stim : compounded){
            vnet += stim.getAssociationNet();
        }
        return vnet;
    }

    @Override
    public String getName() {
        String name = "";
        for(Stimulus stim : compounded){
            name += stim.getName();
        }
        return name;
    }
}
