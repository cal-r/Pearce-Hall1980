package Models.Stimulus.Rodriguez;

import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.Rodriguez.InitialAssociationParameter;
import Models.Parameters.ConditionalStimulus.Rodriguez.SalienceParameter;
import Models.Parameters.Parameter;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.Stimulus.IConditionalStimulus;

import java.io.Serializable;

/**
 * Created by Rokas on 29/03/2016.
 */
public class RodriguezStimulus implements IConditionalStimulus, Serializable {

    public InitialAlphaParameter InitialAlphaParameter;
    public InitialAssociationParameter InitialAssociationParamater;
    public SalienceParameter SalienceParameter;

    private String name;

    public double getAssociationEvent() {
        return associationEvent;
    }

    public double getAssociationNoEvent() {
        return associationNoEvent;
    }

    private double associationEvent;
    private double associationNoEvent;

    private double alpha;

    public RodriguezStimulus(InitialAlphaParameter initialAlphaParameter, InitialAssociationParameter initialAssociationParamater, SalienceParameter salienceParameter, String name) {
        InitialAlphaParameter = initialAlphaParameter;
        InitialAssociationParamater = initialAssociationParamater;
        SalienceParameter = salienceParameter;
        this.name = name;
        setInitialValues(initialAlphaParameter, initialAssociationParamater);
    }

    @Override
    public IConditionalStimulus getCopy() {
        RodriguezStimulus copy = new RodriguezStimulus(InitialAlphaParameter, InitialAssociationParamater, SalienceParameter, name);
        copy.alpha = alpha;
        copy.associationEvent = associationEvent;
        copy.associationNoEvent = associationNoEvent;
        return copy;
    }

    @Override
    public void reset(IConditionalStimulus stim) {
        RodriguezStimulus rs = (RodriguezStimulus) stim;
        associationEvent = rs.associationEvent;
        associationNoEvent = rs.associationNoEvent;
        alpha = rs.alpha;
    }

    @Override
    public void reset() {
        setInitialValues(InitialAlphaParameter, InitialAssociationParamater);
    }

    @Override
    public void stimulate(GlobalParameterPool globalParams, double lambda, double vNet, char reinforcer) {
        double capitalLambda = lambda - vNet;
        double newAlpha = calcNewAlpha(globalParams.getGamma(), lambda, vNet, getAlpha());
        double newDeltaVi = calcNewDeltaVi(capitalLambda);
        associationNoEvent += newDeltaVi;
        alpha = newAlpha;
    }

    private double calcNewDeltaVi(double capitalLambda){
        return SalienceParameter.getValue() * getAlpha() * Math.abs(capitalLambda);
    }

    private double calcNewAlpha(Parameter gamma, double lambda, double vNet, double oldAlpha) {
        return gamma.getValue() * Math.abs(lambda - vNet) + (1 - gamma.getValue()) * oldAlpha;
    }

    @Override
    public double getAssociationNet() {
        return associationEvent - associationNoEvent;
    }

    @Override
    public String getName() {
        return name;
    }

    private void setInitialValues(InitialAlphaParameter alphaParameter, InitialAssociationParameter associationParameter){
        alpha = alphaParameter.getValue();
        associationEvent = associationParameter.getValue();
        associationNoEvent = 0;
    }

    public double getAlpha(){
        return alpha;
    }
}
