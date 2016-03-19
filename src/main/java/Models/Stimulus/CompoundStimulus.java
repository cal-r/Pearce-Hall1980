package Models.Stimulus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rokas on 14/02/2016.
 */
public class CompoundStimulus implements IStimulus, Serializable {

    private List<IConditionalStimulus> compounded;

    public CompoundStimulus(List<IConditionalStimulus> compounded){
        this.compounded = compounded;
    }

    @Override
    public double getAssociationNet() {
        double vnet = 0;
        for(IStimulus stim : compounded){
            vnet += stim.getAssociationNet();
        }
        return vnet;
    }

    @Override
    public String getName() {
        String name = "";
        for(IStimulus stim : compounded){
            name += stim.getName();
        }
        return name;
    }
}
