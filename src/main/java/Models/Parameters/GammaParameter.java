package Models.Parameters;

import Constants.ParameterNamingConstants;

import java.io.Serializable;

/**
 * Created by Rokas on 07/11/2015.
 */
public class GammaParameter extends Parameter implements Serializable {
    public GammaParameter(){
        super(ParameterNamingConstants.GAMMA);
    }
}
