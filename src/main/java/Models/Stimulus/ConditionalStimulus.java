package Models.Stimulus;

import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.SalienceExcitatoryParameter;
import Models.Parameters.ConditionalStimulus.SalienceInhibitoryParameter;
import Models.Parameters.Parameter;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.Parameters.Pools.UsParameterPool;

import java.io.Serializable;

/**
 * Created by Rokas on 03/11/2015.
 */
public class ConditionalStimulus implements Serializable, IPHConditionalStimulus {

    public InitialAlphaParameter InitialAlphaParameter;
    public SalienceExcitatoryParameter SalienceExcitatoryParameter;
    public SalienceInhibitoryParameter SalienceInhibitoryParameter;

    private String name;

    private double associationInhibitory;
    protected double associationExcitatory;

    private double alpha;

    public ConditionalStimulus(String name, InitialAlphaParameter initialAlphaParameter, SalienceExcitatoryParameter salienceExcitatoryParameter, SalienceInhibitoryParameter salienceInhibitoryParameter){
        this.name = name;
        setInitialValues(initialAlphaParameter);
        InitialAlphaParameter = initialAlphaParameter;
        SalienceExcitatoryParameter = salienceExcitatoryParameter;
        SalienceInhibitoryParameter = salienceInhibitoryParameter;
    }

    public double getAlpha(){
        return alpha;
    }

    public void setAlpha(double value){
        alpha = value;
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
        ConditionalStimulus copy = new ConditionalStimulus(name, InitialAlphaParameter, SalienceExcitatoryParameter, SalienceInhibitoryParameter);
        copy.associationExcitatory = associationExcitatory;
        copy.associationInhibitory = associationInhibitory;
        copy.alpha = alpha;
        return copy;
    }

    public void reset(IConditionalStimulus stim){
        ConditionalStimulus cs = (ConditionalStimulus) stim;
        associationExcitatory = cs.getAssociationExcitatory();
        associationInhibitory = cs.getAssociationInhibitory();
        setAlpha(cs.getAlpha());
    }

    @Override
    public void stimulate(GlobalParameterPool globalParams, double lambdaParameter, double vNet, char reinforcer) {
        double lambda = reinforcer == '-' ? 0 : lambdaParameter;
        double capitalLambda = lambda - vNet;
        double newAlpha = calcNewAlpha(globalParams.getGamma(), lambda, vNet, getAlpha());
        if (capitalLambda >= 0) {
            double newDeltaVe = calcNewDeltaVe(lambda);
            updateAssociationExcitatory(newDeltaVe);
        } else if (capitalLambda < 0) {
            double newDeltaVi = calcNewDeltaVi(capitalLambda);
            updateAssociationInhibitory(newDeltaVi);
        }
        setAlpha(newAlpha);
    }

    private double calcNewDeltaVe(double lambda){
        return SalienceExcitatoryParameter.getValue() * getAlpha() * lambda;
    }

    private double calcNewDeltaVi(double capitalLambda){
        return SalienceInhibitoryParameter.getValue() * getAlpha() * Math.abs(capitalLambda);
    }

    private double calcNewAlpha(Parameter gamma, double lambda, double vNet, double oldAlpha) {
        return gamma.getValue() * Math.abs(lambda - vNet) + (1 - gamma.getValue()) * oldAlpha;
    }

    public void reset(){
        setInitialValues(InitialAlphaParameter);
    }

    private void setInitialValues(InitialAlphaParameter initialAlphaParameter){
        associationExcitatory = 0;
        associationInhibitory = 0;
        setAlpha(initialAlphaParameter.getValue());
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
