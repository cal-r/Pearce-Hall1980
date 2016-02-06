package Models.Parameters;

import Constants.ParameterNamingConstants;
import Models.ConditionalStimulus;

import java.io.Serializable;

/**
 * Created by Rokas on 05/11/2015.
 */
public class InitialAlphaParameter extends CsParameter implements Serializable {
    public InitialAlphaParameter(char cueName) {
        super(cueName, ParameterNamingConstants.INITIAL_ALPHA);
    }
}
