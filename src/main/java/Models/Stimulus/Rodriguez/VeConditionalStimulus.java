package Models.Stimulus.Rodriguez;

import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.Rodriguez.InitialAssociationParameter;
import Models.Stimulus.ConditionalStimulus;

/**
 * Created by Rokas on 09/04/2016.
 */
public class VeConditionalStimulus extends ConditionalStimulus{
    private final InitialAssociationParameter initialAssociationParameter;

    public VeConditionalStimulus(String name,
                                 InitialAlphaParameter initialAlphaParameter,
                                 InitialAssociationParameter initialAssociationParameter) {
        super(name, initialAlphaParameter);
        this.initialAssociationParameter = initialAssociationParameter;
        associationExcitatory = initialAssociationParameter.getValue();
    }

    public void reset() {
        super.reset();
        associationExcitatory = initialAssociationParameter.getValue();
    }
}
