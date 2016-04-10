package Models.Stimulus;

import java.io.Serializable;
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
        if(reinforcerSpecificCues){
            return getNameWithReinforcerAtTheEnd();
        }
        String name = "";
        for(IStimulus stim : compounded){
            name += stim.getName();
        }
        return name;
    }

    private String getNameWithReinforcerAtTheEnd(){
        String name = "";
        Character reinforcer = null;
        for(IStimulus stim : compounded){
            if(stim instanceof ContextStimulus){
                name += stim.getName();
                continue;
            }
            String stimNameWithReinforcer = stim.getName();
            String stimNameWithoutReinforcer = stimNameWithReinforcer.substring(0, stimNameWithReinforcer.length()-1);
            reinforcer = stimNameWithReinforcer.charAt(stimNameWithReinforcer.length()-1);
            name += stimNameWithoutReinforcer;
        }
        return name + reinforcer;
    }
}
