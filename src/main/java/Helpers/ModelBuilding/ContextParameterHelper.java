package Helpers.ModelBuilding;

import Models.Parameters.Pools.CsPools.CsParameterPool;
import Models.Stimulus.ConditionalStimulus;
import Models.Stimulus.IConditionalStimulus;
import _from_RW_simulator.ContextConfig;

/**
 * Created by Rokas on 19/02/2016.
 */
public class ContextParameterHelper {
    public static void setContextConfigInParamsPool(ContextConfig config, CsParameterPool csParameterPool){
        String contextName = config.getSymbol();
        if(!csParameterPool.contains(contextName)){
            csParameterPool.createParameters(contextName);
        }
        csParameterPool.getInitialAlpha(contextName).setValue(config.getAlpha());
        csParameterPool.getInitialAlpha(contextName).visibleInCsParamsTable = false;
    }
}
