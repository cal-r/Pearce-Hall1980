package Models.Stimulus;

import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.Rodriguez.InitialAssociationParameter;
import Models.Parameters.ConditionalStimulus.SalienceExcitatoryParameter;
import Models.Parameters.ConditionalStimulus.SalienceInhibitoryParameter;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.Stimulus.Rodriguez.VeConditionalStimulus;

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
    private InitialAssociationParameter initialAssociationParameter;
    private Map<Character, ConditionalStimulus> stimsMap;
    private Map<Character, Boolean> usedStims;
    private double alpha;
    
    public MultipleStimulus(String name,
                            InitialAlphaParameter initialAlphaParameter,
                            SalienceExcitatoryParameter salienceExcitatoryParameter,
                            SalienceInhibitoryParameter salienceInhibitoryParameter,
                            InitialAssociationParameter initialAssociationParameter) {
        this.name = name;
        this.initialAlphaParameter = initialAlphaParameter;
        this.salienceExcitatoryParameter = salienceExcitatoryParameter;
        this.salienceInhibitoryParameter = salienceInhibitoryParameter;
        this.initialAssociationParameter = initialAssociationParameter;
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
        if(initialAssociationParameter == null) {
            return new ConditionalStimulus(
                    createCueName(us),
                    initialAlphaParameter,
                    salienceExcitatoryParameter,
                    salienceInhibitoryParameter);
        }
        return new VeConditionalStimulus(
                createCueName(us),
                initialAlphaParameter,
                salienceExcitatoryParameter,
                salienceInhibitoryParameter,
                initialAssociationParameter);
    }

    private String createCueName(char us){
        return String.format("%s%s", name, us);
    }

    @Override
    public IConditionalStimulus getCopy() {
        MultipleStimulus copy = new MultipleStimulus(
                name,
                initialAlphaParameter,
                salienceExcitatoryParameter,
                salienceInhibitoryParameter,
                initialAssociationParameter);

        for(char key : stimsMap.keySet()){
            copy.stimsMap.put(key, stimsMap.get(key).getCopy());
        }
        copy.alpha = alpha;
        copy.usedStims = usedStims;
        return copy;
    }

    @Override
    public void reset(IConditionalStimulus stim) {
        MultipleStimulus otherMs = (MultipleStimulus) stim;
        alpha = otherMs.alpha;
        usedStims = otherMs.usedStims;
        for(IConditionalStimulus otherCs : otherMs.getAllStims()){
            for(IConditionalStimulus myCs : getAllStims()){
                if(myCs.getName().equals(otherCs.getName())){
                    myCs.reset(otherCs);
                }
            }
        }
    }

    @Override
    public void stimulate(GlobalParameterPool globalParams, double lambda, double vNet, char reinforcer) {
        alpha = getAnyAlpha();
        if(reinforcer == '-'){
            if(!usedStims.isEmpty()) {
                for (char usedUs : usedStims.keySet()) {
                    simulateStim(stimsMap.get(usedUs), globalParams, lambda, vNet, reinforcer);
                }
            }
        }else{
            simulateStim(stimsMap.get(reinforcer), globalParams, lambda, vNet, reinforcer);
            if(!usedStims.containsKey(reinforcer)){
                usedStims.put(reinforcer, true);
            }
            for(char us : usedStims.keySet()) {
                if (us != reinforcer) {
                    simulateStim(stimsMap.get(us), globalParams, lambda, stimsMap.get(us).getAssociationNet(), '-');
                }
            }
        }
        setAlphas();
    }

    private double getAnyAlpha(){
        return ((ConditionalStimulus)stimsMap.values().toArray()[0]).getAlpha();
    }

    private void setAlphas(){
        for(ConditionalStimulus cs : stimsMap.values()){
            cs.setAlpha(alpha);
        }
    }

    private void simulateStim(ConditionalStimulus cs, GlobalParameterPool globalParams, double lambda, double vNet, char reinforcer){
        cs.stimulate(globalParams, lambda, vNet, reinforcer);
        alpha = cs.getAlpha();
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

    public double getAlpha() {
        return alpha;
    }
}
