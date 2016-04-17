package Models.Stimulus.Rodriguez;

import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.Rodriguez.InitialAssociationParameter;
import Models.Parameters.ConditionalStimulus.SalienceExcitatoryParameter;
import Models.Parameters.ConditionalStimulus.SalienceInhibitoryParameter;
import Models.Stimulus.ConditionalStimulus;

/**
 * Created by Rokas on 09/04/2016.
 */
public class VeConditionalStimulus extends ConditionalStimulus{
    private final InitialAssociationParameter initialAssociationParameter;

    public VeConditionalStimulus(String name,
                                 InitialAlphaParameter initialAlphaParameter,
                                 SalienceExcitatoryParameter salienceExcitatoryParameter,
                                 SalienceInhibitoryParameter salienceInhibitoryParameter,
                                 InitialAssociationParameter initialAssociationParameter) {
        super(name, initialAlphaParameter, salienceExcitatoryParameter, salienceInhibitoryParameter);
        this.initialAssociationParameter = initialAssociationParameter;
        associationExcitatory = initialAssociationParameter.getValue();
    }

    public void reset() {
        super.reset();
        associationExcitatory = initialAssociationParameter.getValue();
    }
}
