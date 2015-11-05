package Models;

/**
 * Created by Rokas on 03/11/2015.
 */
public class ConditionalStimulus {
    public double SalienceExcitatory;
    public double SalienceInhibitory;
    public char Name;

    private double AssociationExcitatory;
    private double AssociationInhibitory;

    public ConditionalStimulus(char name){
        AssociationExcitatory = 0;
        AssociationInhibitory = 0;
        Name = name;
    }

    public double getAssociationNet() {
        return AssociationExcitatory - AssociationInhibitory;
    }
}
