package Models.Parameters.ConditionalStimulus;

import Constants.ParameterNamingConstants;
import Models.Parameters.ConditionalStimulus.CsParameter;

import java.io.Serializable;

/**
 * Created by Rokas on 05/11/2015.
 */
public class InitialAlphaParameter extends CsParameter implements Serializable {
    public InitialAlphaParameter(String cueName) {
        super(cueName, ParameterNamingConstants.INITIAL_ALPHA);
    }
}
