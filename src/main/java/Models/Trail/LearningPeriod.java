package Models.Trail;

import Models.Parameters.Pools.GlobalParameterPool;
import Models.Stimulus.IConditionalStimulus;
import Models.Stimulus.IStimulus;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 20/02/2016.
 */
public class LearningPeriod implements Serializable {
    public boolean usPresent;
    public char reinforcer;
    public List<IStimulus> stims;

    public LearningPeriod(boolean usPresent, char reinforcer, List<IStimulus> stims) {
        this.usPresent = usPresent;
        this.stims = stims;
        this.reinforcer = reinforcer;
    }

    public void learn(double vNet, GlobalParameterPool globalParams, double lambdaParameter) {
        for (IStimulus stimulus : stims) {
            if (stimulus instanceof IConditionalStimulus) {
                IConditionalStimulus cs = (IConditionalStimulus) stimulus;
                cs.stimulate(globalParams, lambdaParameter, vNet, reinforcer);
            }
        }
    }
}