package Models.Parameters.Pools;

import Constants.DefaultValuesConstants;
import Constants.ParameterNamingConstants;
import Models.Parameters.Parameter;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Rokas on 27/02/2016.
 */
public class GlobalParameterPool implements Serializable {

    private List<Parameter> globalParameters;

    private UsParameterPool usParameterPool;

    public GlobalParameterPool(){
        globalParameters = new ArrayList<>();
        globalParameters.add(createGamma());
        usParameterPool = new UsParameterPool();
    }

    public List<Parameter> getGlobalParameters(){
        return globalParameters;
    }

    public Parameter getGamma(){
        return globalParameters.get(0);
    }

    private Parameter createGamma(){
        return createParameter(ParameterNamingConstants.GAMMA, DefaultValuesConstants.GAMMA);
    }

    private Parameter createParameter(String name, double value){
        Parameter param = new Parameter(name);
        param.setValue(value);
        return param;
    }

    public UsParameterPool getUsParameterPool() {
        return usParameterPool;
    }

}
