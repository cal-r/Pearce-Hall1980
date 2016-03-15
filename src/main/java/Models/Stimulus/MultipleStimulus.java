package Models.Stimulus;

import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.SalienceExcitatoryParameter;
import Models.Parameters.ConditionalStimulus.SalienceInhibitoryParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 13/03/2016.
 */
public class MultipleStimulus implements IConditionalStimulus {

    private final String name;
    private final InitialAlphaParameter initialAlphaParameter;
    private final SalienceExcitatoryParameter salienceExcitatoryParameter;
    private final SalienceInhibitoryParameter salienceInhibitoryParameter;
    private Map<Character, ConditionalStimulus> stimsPerUs;

    public MultipleStimulus(String name, InitialAlphaParameter initialAlphaParameter, SalienceExcitatoryParameter salienceExcitatoryParameter, SalienceInhibitoryParameter salienceInhibitoryParameter) {
        this.name = name;
        this.initialAlphaParameter = initialAlphaParameter;
        this.salienceExcitatoryParameter = salienceExcitatoryParameter;
        this.salienceInhibitoryParameter = salienceInhibitoryParameter;
        stimsPerUs = new HashMap<>();
    }

    public void addStimulus(char us){
        //a cs for us '-' exists only when there are no positive reinforcers in the phase
        if(us == '-' && stimsPerUs.isEmpty()){
            stimsPerUs.put(us, createNewCs(us));
        }

        if(!stimsPerUs.containsKey(us) && us != '-'){
            if(stimsPerUs.containsKey('-')){
                stimsPerUs.remove('-');
            }
            stimsPerUs.put(us, createNewCs(us));
        }
    }

    public List<ConditionalStimulus> getStims(char us){
        if(us=='-'){
            return new ArrayList<>(stimsPerUs.values());
        }
        List<ConditionalStimulus> stims = new ArrayList<>();
        stims.add(stimsPerUs.get(us));
        return stims;
    }

    private ConditionalStimulus createNewCs(char us){
        String cueName = String.format("%s%s", name, us);
        return new ConditionalStimulus(cueName, initialAlphaParameter, salienceExcitatoryParameter, salienceInhibitoryParameter);
    }

    @Override
    public IConditionalStimulus getCopy() {
        MultipleStimulus copy = new MultipleStimulus(name, initialAlphaParameter, salienceExcitatoryParameter, salienceInhibitoryParameter);
        for(char key : stimsPerUs.keySet()){
            copy.stimsPerUs.put(key, stimsPerUs.get(key).getCopy());
        }
        return copy;
    }

    @Override
    public void reset(IConditionalStimulus cs) {

    }

    @Override
    public double getAssociationNet() {
        double vNet = 0;
        for(IStimulus stim : stimsPerUs.values()){
            vNet += stim.getAssociationNet();
        }
        return vNet;
    }

    @Override
    public String getName() {
        return name;
    }
}
