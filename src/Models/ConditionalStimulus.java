package Models;

import Models.Parameters.CsParameter;
import Models.Parameters.InitialAlphaParameter;
import Models.Parameters.SalienceExcitatoryParameter;
import Models.Parameters.SalienceInhibitoryParameter;

import java.util.ArrayList;

/**
 * Created by Rokas on 03/11/2015.
 */
public class ConditionalStimulus {

    public InitialAlphaParameter InitialAlphaParameter;
    public SalienceExcitatoryParameter SalienceExcitatoryParameter;
    public SalienceInhibitoryParameter SalienceInhibitoryParameter;

    public char Name;

    private double associationInhibitory;
    private double associationExcitatory;

    private boolean alphaSet;
    private double alpha;

    public ConditionalStimulus(char name){
        associationExcitatory = 0;
        associationInhibitory = 0;
        Name = name;

        InitialAlphaParameter = new InitialAlphaParameter(this);
        SalienceExcitatoryParameter = new SalienceExcitatoryParameter(this);
        SalienceInhibitoryParameter = new SalienceInhibitoryParameter(this);

        alphaSet = false;
    }

    public double getAssociationNet() {
        return Math.min(associationExcitatory - associationInhibitory, 1);
    }

    public double getAlpha(){
        return alphaSet ? alpha : InitialAlphaParameter.getValue();
    }

    public void setAlpha(double value){
        alpha = value;
        alphaSet = true;
    }

    public double getAssociationExcitatory(){
        return Math.min(associationExcitatory, 1);
    }

    public double getAssociationInhibitory(){
        return Math.min(associationInhibitory, 1);
    }

    public void updateAssociationExcitatory(double change){
        associationExcitatory+=change;
    }

    public void updateAssociationInhibitory(double change){
        associationInhibitory+=change;
    }

    public ArrayList<CsParameter> getAllParameters(){
        ArrayList<CsParameter> params = new ArrayList<>();
        params.add(InitialAlphaParameter);
        params.add(SalienceExcitatoryParameter);
        params.add(SalienceInhibitoryParameter);
        return params;
    }
}
