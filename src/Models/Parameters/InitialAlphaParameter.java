package Models.Parameters;

import Constants.ParameterNamingConstants;
import Models.ConditionalStimulus;

/**
 * Created by Rokas on 05/11/2015.
 */
public class InitialAlphaParameter extends CsParameter {
    public InitialAlphaParameter(ConditionalStimulus cue) {
        super(cue, ParameterNamingConstants.INITIAL_ALPHA);
    }
}
