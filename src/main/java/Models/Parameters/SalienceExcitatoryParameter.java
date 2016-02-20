package Models.Parameters;

import Constants.ParameterNamingConstants;

import java.io.Serializable;

/**
 * Created by Rokas on 05/11/2015.
 */
public class SalienceExcitatoryParameter extends CsParameter implements Serializable {
    public SalienceExcitatoryParameter(String cue) {
        super(cue, ParameterNamingConstants.SALIENCE_EXCITATORY);
    }
}
