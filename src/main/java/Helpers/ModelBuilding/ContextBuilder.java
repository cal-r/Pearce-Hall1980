package Helpers.ModelBuilding;

import Models.Parameters.Pools.CsPools.CsParameterPool;
import Models.Stimulus.ContextStimulus;
import _from_RW_simulator.ContextConfig;

/**
 * Created by Rokas on 19/02/2016.
 */
public class ContextBuilder {
    public static ContextStimulus buildContext(ContextConfig config, CsParameterPool contextParameters){
        String contextName = config.getSymbol();
        contextParameters.createParameters(contextName);
        contextParameters.getInitialAlpha(contextName).setValue(config.getAlpha());
        contextParameters.getSeParameter(contextName).setValue(config.getSe());
        contextParameters.getSiParamter(contextName).setValue(config.getSi());

        return new ContextStimulus(
                contextName,
                contextParameters.getInitialAlpha(contextName),
                contextParameters.getSeParameter(contextName),
                contextParameters.getSiParamter(contextName));
    }
}
