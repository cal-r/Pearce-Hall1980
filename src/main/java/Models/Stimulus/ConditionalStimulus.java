package Models.Stimulus;

import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.SalienceExcitatoryParameter;
import Models.Parameters.ConditionalStimulus.SalienceInhibitoryParameter;
import Models.Parameters.Parameter;
import Models.Parameters.Pools.GlobalParameterPool;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Rokas on 03/11/2015.
 */
public class ConditionalStimulus implements Serializable, IConditionalStimulus {

    public InitialAlphaParameter InitialAlphaParameter;
    public SalienceExcitatoryParameter SalienceExcitatoryParameter;
    public SalienceInhibitoryParameter SalienceInhibitoryParameter;

    private String name;

    private double associationInhibitory;
    private double associationExcitatory;

    private boolean alphaSet;
    private double alpha;

    public ConditionalStimulus(String name, InitialAlphaParameter initialAlphaParameter, SalienceExcitatoryParameter salienceExcitatoryParameter, SalienceInhibitoryParameter salienceInhibitoryParameter){
        this.name = name;
        setInitialValues();
        InitialAlphaParameter = initialAlphaParameter;
        SalienceExcitatoryParameter = salienceExcitatoryParameter;
        SalienceInhibitoryParameter = salienceInhibitoryParameter;
    }

    public ConditionalStimulus(String name, InitialAlphaParameter initialAlphaParameter,
                               SalienceExcitatoryParameter salienceExcitatoryParameter,
                               SalienceInhibitoryParameter salienceInhibitoryParameter,
                               double associationExcitatory,
                               double associationInhibitory,
                               double alpha) {
        this(name, initialAlphaParameter, salienceExcitatoryParameter, salienceInhibitoryParameter);
        this.associationExcitatory = associationExcitatory;
        this.associationInhibitory = associationInhibitory;
        setAlpha(alpha);
    }

    public double getAlpha(){
        return alphaSet ? alpha : InitialAlphaParameter.getValue();
    }

    public void setAlpha(double value){
        alpha = value;
        alphaSet = true;
    }

    public double getAssociationExcitatory(){
        return associationExcitatory;
    }

    public double getAssociationInhibitory(){
        return associationInhibitory;
    }

    public void updateAssociationExcitatory(double change){
        associationExcitatory+=change;
    }

    public void updateAssociationInhibitory(double change){
        associationInhibitory+=change;
    }

    public void setAssociationExcitatory(double newVal){
        associationExcitatory=newVal;
    }

    public void setAssociationInhibitory(double newVal){
        associationInhibitory=newVal;
    }

    public ConditionalStimulus getCopy(){
        return new ConditionalStimulus(name, InitialAlphaParameter, SalienceExcitatoryParameter, SalienceInhibitoryParameter, associationExcitatory, associationInhibitory, getAlpha());
    }

    public void reset(IConditionalStimulus stim){
        ConditionalStimulus cs = (ConditionalStimulus) stim;
        associationExcitatory = cs.getAssociationExcitatory();
        associationInhibitory = cs.getAssociationInhibitory();
        setAlpha(cs.getAlpha());
    }

    @Override
    public void stimulate(GlobalParameterPool globalParams, Map<Character, Double> phaseLambdaValues, double vNet, char reinforcer) {
        boolean usPresent = reinforcer != '-';
        double lambda = phaseLambdaValues.get(reinforcer);
        double capitalLambda = lambda - vNet;
        double newAlpha = calcNewAlpha(globalParams.getGamma(), lambda, vNet, getAlpha());
        if (usPresent && capitalLambda > 0) {
            double newDeltaVe = calcNewDeltaVe(lambda);
            updateAssociationExcitatory(newDeltaVe);
        } else if (!usPresent && capitalLambda < 0) {
            double newDeltaVi = calcNewDeltaVi(capitalLambda);
            updateAssociationInhibitory(newDeltaVi);
        }
        setAlpha(newAlpha);
    }

    private double calcNewDeltaVe(double lambda){
        return SalienceExcitatoryParameter.getValue() * getAlpha() * lambda;
    }

    private double calcNewDeltaVi(double capitalLambda){
        return SalienceExcitatoryParameter.getValue() * getAlpha() * Math.abs(capitalLambda);
    }

    private double calcNewAlpha(Parameter gamma, double lambda, double vNet, double oldAlpha) {
        return gamma.getValue() * Math.abs(lambda - vNet) + (1 - gamma.getValue()) * oldAlpha;
    }

    public void reset(){
        setInitialValues();
    }

    private void setInitialValues(){
        associationExcitatory = 0;
        associationInhibitory = 0;
        alphaSet = false;
    }

    @Override
    public double getAssociationNet() {
        return associationExcitatory - associationInhibitory;
    }

    @Override
    public String getName() {
        return String.valueOf(name);
    }
}
