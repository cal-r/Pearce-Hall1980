package Models.Stimulus;

import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.SalienceExcitatoryParameter;
import Models.Parameters.ConditionalStimulus.SalienceInhibitoryParameter;
import Models.Parameters.Pools.GlobalParameterPool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 13/03/2016.
 */
public class MultipleStimulus implements IConditionalStimulus, Serializable {

    private final String name;
    private final InitialAlphaParameter initialAlphaParameter;
    private final SalienceExcitatoryParameter salienceExcitatoryParameter;
    private final SalienceInhibitoryParameter salienceInhibitoryParameter;
    private Map<Character, ConditionalStimulus> stimsPerUs;
    private Map<Character, Boolean> usedStims;

    public MultipleStimulus(String name, InitialAlphaParameter initialAlphaParameter, SalienceExcitatoryParameter salienceExcitatoryParameter, SalienceInhibitoryParameter salienceInhibitoryParameter) {
        this.name = name;
        this.initialAlphaParameter = initialAlphaParameter;
        this.salienceExcitatoryParameter = salienceExcitatoryParameter;
        this.salienceInhibitoryParameter = salienceInhibitoryParameter;
        stimsPerUs = new HashMap<>();
        usedStims = new HashMap<>();
    }

    public void addStimulus(char us){
        //a cs for us '-' exists only when there are no positive reinforcers in the phase
        if(us == '-' && stimsPerUs.isEmpty()) {
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
            if(stimsPerUs.size()==1 && stimsPerUs.containsKey('-')){
                return new ArrayList<>();
            }
            return new ArrayList<>(stimsPerUs.values());
        }
        List<ConditionalStimulus> stims = new ArrayList<>();
        stims.add(stimsPerUs.get(us));
        return stims;
    }

    private ConditionalStimulus createNewCs(char us){
        return new ConditionalStimulus(createCueName(us), initialAlphaParameter, salienceExcitatoryParameter, salienceInhibitoryParameter);
    }

    private String createCueName(char us){
        return String.format("%s%s", name, us);
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
    public void reset(IConditionalStimulus stim) {
        MultipleStimulus otherMs = (MultipleStimulus) stim;
        for(IConditionalStimulus otherCs : otherMs.getStims('-')){
            for(IConditionalStimulus myCs : getStims('-')){
                if(myCs.getName().equals(otherCs.getName())){
                    myCs.reset(otherCs);
                }
            }
        }
    }

    @Override
    public void stimulate(GlobalParameterPool globalParams, Map<Character, Double> phaseLambdaValues, double vNet, char reinforcer) {
        if(reinforcer == '-'){
            for(IConditionalStimulus cs : stimsPerUs.values()){
                cs.stimulate(globalParams, phaseLambdaValues, vNet, reinforcer);
            }
        }else{
            stimsPerUs.get(reinforcer).stimulate(globalParams, phaseLambdaValues, vNet, reinforcer);
            if(!usedStims.containsKey(reinforcer)){
                usedStims.put(reinforcer, true);
            }
            for(char us : usedStims.keySet()){
                if(us != reinforcer){
                    stimsPerUs.get(us).stimulate(globalParams, phaseLambdaValues, vNet, '-');
                }
            }
        }
    }

    @Override
    public double getAssociationNet() {
        double sum = 0.0;
        for(IConditionalStimulus stim : stimsPerUs.values()){
            sum += stim.getAssociationNet();
        }
        return sum / stimsPerUs.size();
    }

    @Override
    public String getName() {
        return name;
    }
}
