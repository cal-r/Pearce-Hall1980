package Models.Parameters.Pools;

import Constants.DefaultValuesConstants;
import Constants.ParameterNamingConstants;
import Models.Parameters.GammaParameter;
import Models.Parameters.Parameter;
import Models.SimulatorSettings;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Rokas on 27/02/2016.
 */
public class GlobalParameterPool implements Serializable {

    private List<Parameter> globalParameters;

    private UsParameterPool usParameterPool;
    private SimulatorSettings settings;

    public GlobalParameterPool(SimulatorSettings settings){
        this.settings = settings;
        globalParameters = new ArrayList<>();
        globalParameters.add(createGamma());
        usParameterPool = new UsParameterPool(settings);
    }

    public List<Parameter> getGlobalParameters(){
        return globalParameters;
    }

    public Parameter getGamma(){
        return globalParameters.get(0);
    }

    private Parameter createGamma(){
        return new GammaParameter(settings);
    }

    public UsParameterPool getUsParameterPool() {
        return usParameterPool;
    }

}
