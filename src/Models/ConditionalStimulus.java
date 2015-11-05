package Models;

import Models.Parameters.CsParameter;
import Models.Parameters.InitialAlphaParameter;
import Models.Parameters.SalienceExcitatoryParameter;
import Models.Parameters.SalienceInhibitoryParameter;
import org.omg.Dynamic.Parameter;

import java.util.ArrayList;

/**
 * Created by Rokas on 03/11/2015.
 */
public class ConditionalStimulus {

    public InitialAlphaParameter initialAlphaParameter;
    public SalienceExcitatoryParameter salienceExcitatoryParameter;
    public SalienceInhibitoryParameter salienceInhibitoryParameter;

    public char Name;

    private double AssociationExcitatory;
    private double AssociationInhibitory;

    public ConditionalStimulus(char name){
        AssociationExcitatory = 0;
        AssociationInhibitory = 0;
        Name = name;

        initialAlphaParameter = new InitialAlphaParameter(this);
        salienceExcitatoryParameter = new SalienceExcitatoryParameter(this);
        salienceInhibitoryParameter = new SalienceInhibitoryParameter(this);
    }

    public double getAssociationNet() {
        return AssociationExcitatory - AssociationInhibitory;
    }

    public ArrayList<CsParameter> getAllParameters(){
        ArrayList<CsParameter> params = new ArrayList<>();
        params.add(initialAlphaParameter);
        params.add(salienceExcitatoryParameter);
        params.add(salienceInhibitoryParameter);
        return params;
    }
}
