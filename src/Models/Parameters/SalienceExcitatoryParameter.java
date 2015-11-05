package Models.Parameters;

import Constants.ParameterNamingConstants;
import Models.ConditionalStimulus;

/**
 * Created by Rokas on 05/11/2015.
 */
public class SalienceExcitatoryParameter extends CsParameter {
    public SalienceExcitatoryParameter(ConditionalStimulus cue) {
        super(cue, ParameterNamingConstants.SALIENCE_EXCITATORY);
    }
}
