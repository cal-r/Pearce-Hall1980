package Models.Stimulus;

import Models.Parameters.InitialAlphaParameter;
import Models.Parameters.SalienceExcitatoryParameter;
import Models.Parameters.SalienceInhibitoryParameter;

/**
 * Created by Rokas on 19/02/2016.
 */
public class ContextStimulus extends ConditionalStimulus {
    // completely useless class, feel free to get rid of this
    public ContextStimulus(String name, Models.Parameters.InitialAlphaParameter initialAlphaParameter, Models.Parameters.SalienceExcitatoryParameter salienceExcitatoryParameter, Models.Parameters.SalienceInhibitoryParameter salienceInhibitoryParameter) {
        super(name, initialAlphaParameter, salienceExcitatoryParameter, salienceInhibitoryParameter);
    }

    public ContextStimulus(String name, Models.Parameters.InitialAlphaParameter initialAlphaParameter, Models.Parameters.SalienceExcitatoryParameter salienceExcitatoryParameter, Models.Parameters.SalienceInhibitoryParameter salienceInhibitoryParameter, double associationExcitatory, double associationInhibitory, double alpha) {
        super(name, initialAlphaParameter, salienceExcitatoryParameter, salienceInhibitoryParameter, associationExcitatory, associationInhibitory, alpha);
    }
}
