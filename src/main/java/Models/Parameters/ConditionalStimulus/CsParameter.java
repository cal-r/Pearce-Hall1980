package Models.Parameters.ConditionalStimulus;

import Models.Parameters.Parameter;

import java.io.Serializable;

/**
 * Created by Rokas on 05/11/2015.
 */
public abstract class CsParameter extends Parameter implements Serializable {

    public String CueName;
    //context is treated just like any other stimulus,
    //however, for some fucking reason context params appear in trial table and not in CS params table
    public boolean visibleInCsParamsTable;

    public CsParameter(String cueName, String name) {
        super(name);
        CueName = cueName;
        visibleInCsParamsTable = true;
    }

    @Override
    public String getDisplayName(){
        return String.format("%s (%s)", name, CueName);
    }
}
