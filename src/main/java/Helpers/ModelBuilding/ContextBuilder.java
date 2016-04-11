package Helpers.ModelBuilding;

import Models.Parameters.Pools.CsPools.CsParameterPool;
import Models.Stimulus.ConditionalStimulus;
import Models.Stimulus.IConditionalStimulus;
import _from_RW_simulator.ContextConfig;

/**
 * Created by Rokas on 19/02/2016.
 */
public class ContextBuilder {
    public static IConditionalStimulus buildContext(ContextConfig config, CsParameterPool contextParameters){
        String contextName = config.getSymbol();
        contextParameters.createParameters(contextName);
        contextParameters.getInitialAlpha(contextName).setValue(config.getAlpha());
        contextParameters.getSeParameter(contextName).setValue(config.getSe());
        contextParameters.getSiParamter(contextName).setValue(config.getSi());

        return new ConditionalStimulus(
                contextName,
                contextParameters.getInitialAlpha(contextName),
                contextParameters.getSeParameter(contextName),
                contextParameters.getSiParamter(contextName));
    }
}
