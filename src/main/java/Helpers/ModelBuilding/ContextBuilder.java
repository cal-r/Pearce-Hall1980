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
        InitialAlphaParameter initialAlphaParameter = new InitialAlphaParameter(config.getSymbol());
        initialAlphaParameter.setValue(config.getAlpha());

        SalienceExcitatoryParameter salienceExcitatoryParameter = new SalienceExcitatoryParameter(config.getSymbol());
        salienceExcitatoryParameter.setValue(config.getSe());

        SalienceInhibitoryParameter salienceInhibitoryParameter = new SalienceInhibitoryParameter(config.getSymbol());
        salienceInhibitoryParameter.setValue(config.getSi());

        return new ContextStimulus(
                config.getSymbol(),
                initialAlphaParameter,
                salienceExcitatoryParameter,
                salienceInhibitoryParameter);
    }
}
