package Models.Stimulus;

import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.SalienceExcitatoryParameter;
import Models.Parameters.ConditionalStimulus.SalienceInhibitoryParameter;

import java.io.Serializable;

/**
 * Created by Rokas on 03/11/2015.
 */
public class ConditionalStimulus implements Serializable, Stimulus {

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

    public void reset(ConditionalStimulus cs){
        associationExcitatory = cs.getAssociationExcitatory();
        associationInhibitory = cs.getAssociationInhibitory();
        setAlpha(cs.getAlpha());
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
