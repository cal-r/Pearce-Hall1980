package Models.Parameters.ConditionalStimulus;

import Constants.ParameterNamingConstants;
import Models.Parameters.ConditionalStimulus.CsParameter;

import java.io.Serializable;

/**
 * Created by Rokas on 05/11/2015.
 */
public class SalienceInhibitoryParameter extends CsParameter implements Serializable {
    public SalienceInhibitoryParameter(String cueName) {
        super(cueName, ParameterNamingConstants.SALIENCE_INHIBATORY);
    }
}

