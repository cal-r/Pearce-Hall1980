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

    public double AssociationExcitatory;
    public double AssociationInhibitory;

    private boolean alphaSet;
    private double alpha;

    public ConditionalStimulus(char name){
        AssociationExcitatory = 0;
        AssociationInhibitory = 0;
        Name = name;

        InitialAlphaParameter = new InitialAlphaParameter(this);
        SalienceExcitatoryParameter = new SalienceExcitatoryParameter(this);
        SalienceInhibitoryParameter = new SalienceInhibitoryParameter(this);

        alphaSet = false;
    }

    public double getAssociationNet() {
        return AssociationExcitatory - AssociationInhibitory;
    }

    public double getAlpha(){
        return alphaSet ? alpha : InitialAlphaParameter.getValue();
    }

    public void setAlpha(double value){
        alpha = value;
        alphaSet = true;
    }

    public ArrayList<CsParameter> getAllParameters(){
        ArrayList<CsParameter> params = new ArrayList<>();
        params.add(InitialAlphaParameter);
        params.add(SalienceExcitatoryParameter);
        params.add(SalienceInhibitoryParameter);
        return params;
    }
}
