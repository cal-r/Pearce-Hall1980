package Models.Parameters.Pools;

import Constants.DefaultValuesConstants;
import Constants.ParameterNamingConstants;
import Models.Parameters.BetaExcitatoryParameter;
import Models.Parameters.BetaInhibitoryParameter;
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
        globalParameters.add(new BetaInhibitoryParameter());
        globalParameters.add(new BetaExcitatoryParameter());
        usParameterPool = new UsParameterPool(settings);
    }

    public List<Parameter> getGlobalParameters(){
        if(!settings.isRodriguezMode())
            return globalParameters;
        return Arrays.asList((Parameter)getGamma());
    }

    public GammaParameter getGamma(){
        return (GammaParameter) globalParameters.get(0);
    }

    public BetaInhibitoryParameter getBetaI(){
        return (BetaInhibitoryParameter) globalParameters.get(1);
    }

    public BetaExcitatoryParameter getBetaE(){
        return (BetaExcitatoryParameter) globalParameters.get(2);
    }

    private Parameter createGamma(){
        return new GammaParameter(settings);
    }

    public UsParameterPool getUsParameterPool() {
        return usParameterPool;
    }

}
