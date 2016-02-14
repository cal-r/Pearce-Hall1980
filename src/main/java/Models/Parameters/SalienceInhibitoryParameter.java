package Models.Parameters;

import Constants.ParameterNamingConstants;

import java.io.Serializable;

/**
 * Created by Rokas on 05/11/2015.
 */
public class SalienceInhibitoryParameter extends CsParameter implements Serializable {
    public SalienceInhibitoryParameter(char cueName) {
        super(cueName, ParameterNamingConstants.SALIENCE_INHIBATORY);
    }
}

