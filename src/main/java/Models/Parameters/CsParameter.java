package Models.Parameters;

import Helpers.DefaultValuesHelper;
import Models.ConditionalStimulus;

import java.io.Serializable;

/**
 * Created by Rokas on 05/11/2015.
 */
public abstract class CsParameter extends Parameter implements Serializable {

    public char CueName;

    public CsParameter(char cueName, String name) {
        super(name);
        CueName = cueName;
    }

    @Override
    public String getDisplayName(){
        return String.format("%s (%s)", name, CueName);
    }
}
