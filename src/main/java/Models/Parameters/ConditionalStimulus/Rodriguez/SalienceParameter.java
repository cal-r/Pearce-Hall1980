package Models.Parameters.ConditionalStimulus.Rodriguez;

import Constants.ParameterNamingConstants;
import Models.Parameters.ConditionalStimulus.CsParameter;

import java.io.Serializable;

/**
 * Created by Rokas on 29/03/2016.
 */
public class SalienceParameter extends CsParameter implements Serializable {
    public SalienceParameter(String cue) {
        super(cue, ParameterNamingConstants.SALIENCE);
    }
}
