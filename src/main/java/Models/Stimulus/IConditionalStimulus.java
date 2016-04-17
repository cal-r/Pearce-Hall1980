package Models.Stimulus;

import Models.Parameters.Pools.GlobalParameterPool;

import java.util.Map;

/**
 * Created by Rokas on 13/03/2016.
 */
public interface IConditionalStimulus extends IStimulus {
    IConditionalStimulus getCopy();
    void reset(IConditionalStimulus cs);
    void reset();
    void stimulate(GlobalParameterPool globalParams, double lambdaParameter, double vNet, char reinforcer);
}
