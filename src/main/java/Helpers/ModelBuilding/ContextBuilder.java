package Helpers.ModelBuilding;

import Models.Parameters.InitialAlphaParameter;
import Models.Parameters.SalienceExcitatoryParameter;
import Models.Parameters.SalienceInhibitoryParameter;
import Models.Stimulus.ContextStimulus;
import _from_RW_simulator.ContextConfig;

/**
 * Created by Rokas on 19/02/2016.
 */
public class ContextBuilder {
    public static ContextStimulus buildContext(ContextConfig config){
        InitialAlphaParameter initialAlphaParameter = new InitialAlphaParameter('_');
        initialAlphaParameter.setValue(config.getAlpha());

        SalienceExcitatoryParameter salienceExcitatoryParameter = new SalienceExcitatoryParameter('_');
        salienceExcitatoryParameter.setValue(config.getSe());

        SalienceInhibitoryParameter salienceInhibitoryParameter = new SalienceInhibitoryParameter('_');
        salienceInhibitoryParameter.setValue(config.getSi());

        return new ContextStimulus(
                config.getSymbol(),
                initialAlphaParameter,
                salienceExcitatoryParameter,
                salienceInhibitoryParameter);
    }
}
