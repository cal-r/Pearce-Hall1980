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
    private Map<Character, ConditionalStimulus> stimsMap;
    private Map<Character, Boolean> usedStims;
    
    public MultipleStimulus(String name, InitialAlphaParameter initialAlphaParameter, SalienceExcitatoryParameter salienceExcitatoryParameter, SalienceInhibitoryParameter salienceInhibitoryParameter) {
        this.name = name;
        this.initialAlphaParameter = initialAlphaParameter;
        this.salienceExcitatoryParameter = salienceExcitatoryParameter;
        this.salienceInhibitoryParameter = salienceInhibitoryParameter;
        stimsMap = new HashMap<>();
        usedStims = new HashMap<>();
    }

    public void addStimulus(char us){
        //a cs for us '-' exists only when there are no positive reinforcers in the phase
        if(us == '-' && stimsMap.isEmpty()) {
            stimsMap.put(us, createNewCs(us));
        }

        if(!stimsMap.containsKey(us) && us != '-'){
            if(stimsMap.containsKey('-')){
                stimsMap.remove('-');
            }
            stimsMap.put(us, createNewCs(us));
        }
    }

    public List<ConditionalStimulus> getAllStims(){
        return getStims('-');
    }

    public List<ConditionalStimulus> getStims(char us){
        if(us=='-'){
            return new ArrayList<>(stimsMap.values());
        }
        List<ConditionalStimulus> stims = new ArrayList<>();
        stims.add(stimsMap.get(us));
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
        for(char key : stimsMap.keySet()){
            copy.stimsMap.put(key, stimsMap.get(key).getCopy());
        }
        return copy;
    }

    @Override
    public void reset(IConditionalStimulus stim) {
        MultipleStimulus otherMs = (MultipleStimulus) stim;
        for(IConditionalStimulus otherCs : otherMs.getAllStims()){
            for(IConditionalStimulus myCs : getAllStims()){
                if(myCs.getName().equals(otherCs.getName())){
                    myCs.reset(otherCs);
                }
            }
        }
    }

    @Override
    public void stimulate(GlobalParameterPool globalParams, Map<Character, Double> phaseLambdaValues, double vNet, char reinforcer) {
        if(reinforcer == '-'){
            if(!usedStims.isEmpty()) {
                for (char usedUs : usedStims.keySet()) {
                    stimsMap.get(usedUs).stimulate(globalParams, phaseLambdaValues, vNet, reinforcer);
                }
            }
        }else{
            stimsMap.get(reinforcer).stimulate(globalParams, phaseLambdaValues, vNet, reinforcer);
            if(!usedStims.containsKey(reinforcer)){
                usedStims.put(reinforcer, true);
            }
            for(char us : usedStims.keySet()){
                if(us != reinforcer){
                    stimsMap.get(us).stimulate(globalParams, phaseLambdaValues, stimsMap.get(us).getAssociationNet(), '-');
                }
            }
        }
    }

    public double getAssociationNet(char reinforcer){
        if(reinforcer == '-'){
            return getAssociationNet();
        }
        return stimsMap.get(reinforcer).getAssociationNet();
    }

    @Override
    public double getAssociationNet() {
        //gives a mean value
        double sum = 0.0;
        if(!usedStims.isEmpty()) {
            for(char usedUs : usedStims.keySet()){
                sum += stimsMap.get(usedUs).getAssociationNet();
            }
            sum /= usedStims.size();
        }
        return sum;
    }

    @Override
    public String getName() {
        return name;
    }

    public Map<Character, ConditionalStimulus> getStimsMap() {
        return stimsMap;
    }

    public Map<Character, Boolean> getUsedStimsMap() {
        return usedStims;
    }
}
