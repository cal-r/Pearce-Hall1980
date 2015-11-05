package Models.Parameters;

import Constants.ParameterNamingConstants;
import Models.ConditionalStimulus;

/**
 * Created by Rokas on 05/11/2015.
 */
public class SalienceInhibitoryParameter extends CsParameter {
    public SalienceInhibitoryParameter(ConditionalStimulus cue) {
        super(cue, ParameterNamingConstants.SALIENCE_INHIBATORY);
    }
}

