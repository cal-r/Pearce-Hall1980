package Models.Parameters.Pools;

import Constants.DefaultValuesConstants;
import Constants.ParameterNamingConstants;
import Models.Parameters.Parameter;

import java.util.*;

/**
 * Created by Rokas on 27/02/2016.
 */
public class GlobalParameterPool {
    private Map<String, Parameter> parameterMap;
    public GlobalParameterPool(){
        parameterMap = new HashMap<>();
        addParameter(createGamma());
        addParameter(createLambdaPlus());
    }

    public List<Parameter> getParameters(){
        return new ArrayList<>(parameterMap.values());
    }

    public Parameter getGamma(){
        return parameterMap.get(ParameterNamingConstants.GAMMA);
    }

    public Parameter getLambda(char us){
        return parameterMap.get(ParameterNamingConstants.LAMBDA_PLUS);
    }

    private Parameter createGamma(){
        return createParameter(ParameterNamingConstants.GAMMA, DefaultValuesConstants.GAMMA);
    }

    private Parameter createLambdaPlus(){
        return createParameter(ParameterNamingConstants.LAMBDA_PLUS, DefaultValuesConstants.LAMBDA);
    }

    private Parameter createParameter(String name, double value){
        Parameter param = new Parameter(name);
        param.setValue(value);
        return param;
    }

    private void addParameter(Parameter param){
        parameterMap.put(param.getDisplayName(), param);
    }

}
