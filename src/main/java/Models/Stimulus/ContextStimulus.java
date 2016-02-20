package Models.Stimulus;

import Models.Parameters.InitialAlphaParameter;
import Models.Parameters.SalienceExcitatoryParameter;
import Models.Parameters.SalienceInhibitoryParameter;

/**
 * Created by Rokas on 19/02/2016.
 */
public class ContextStimulus extends ConditionalStimulus {

    private String name;

    public ContextStimulus(String name, Models.Parameters.InitialAlphaParameter initialAlphaParameter, Models.Parameters.SalienceExcitatoryParameter salienceExcitatoryParameter, Models.Parameters.SalienceInhibitoryParameter salienceInhibitoryParameter) {
        super('_', initialAlphaParameter, salienceExcitatoryParameter, salienceInhibitoryParameter);
        this.name = name;
    }

    public ContextStimulus(String name, Models.Parameters.InitialAlphaParameter initialAlphaParameter, Models.Parameters.SalienceExcitatoryParameter salienceExcitatoryParameter, Models.Parameters.SalienceInhibitoryParameter salienceInhibitoryParameter, double associationExcitatory, double associationInhibitory, double alpha) {
        super('_', initialAlphaParameter, salienceExcitatoryParameter, salienceInhibitoryParameter, associationExcitatory, associationInhibitory, alpha);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
