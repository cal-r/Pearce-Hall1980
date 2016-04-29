package Models.Stimulus;

import Helpers.MultipleUsLabelingHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 14/02/2016.
 */
public class CompoundStimulus implements IStimulus, Serializable {

    private List<IConditionalStimulus> compounded;
    private boolean reinforcerSpecificCues;

    public CompoundStimulus(List<IConditionalStimulus> compounded, boolean reinforcerSpecificCues){
        this.compounded = compounded;
        this.reinforcerSpecificCues = reinforcerSpecificCues;
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
        if (reinforcerSpecificCues) {
            return MultipleUsLabelingHelper.getCompoundName(getCompoundedNames());
        }

        String name = "";
        for (IStimulus stim : compounded) {
            name += stim.getName();
        }
        return name;
    }

    private List<String> getCompoundedNames(){
        List<String> names = new ArrayList<>();
        for(IStimulus stim : compounded){
            names.add(stim.getName());
        }
        return names;
    }
}
