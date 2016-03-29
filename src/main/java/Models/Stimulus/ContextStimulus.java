package Models.Stimulus;

/**
 * Created by Rokas on 19/02/2016.
 */
public class ContextStimulus extends ConditionalStimulus {
    // completely useless class, feel free to get rid of this
    public ContextStimulus(String name, Models.Parameters.ConditionalStimulus.InitialAlphaParameter initialAlphaParameter, Models.Parameters.ConditionalStimulus.SalienceExcitatoryParameter salienceExcitatoryParameter, Models.Parameters.ConditionalStimulus.SalienceInhibitoryParameter salienceInhibitoryParameter) {
        super(name, initialAlphaParameter, salienceExcitatoryParameter, salienceInhibitoryParameter);
    }
}
