package Models.Parameters.Pools;

import Constants.DefaultValuesConstants;
import Constants.ParameterNamingConstants;
import Models.Parameters.Parameter;

import java.util.*;

/**
 * Created by Rokas on 27/02/2016.
 */
public class GlobalParameterPool {

    private static final String extraUsSymbols = "#$*";

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

    public Parameter getLambda(char usSymbol){
        return parameterMap.get(getLambdaName(usSymbol));
    }

    public void addExtraLambdas(){
        for(char symbol : extraUsSymbols.toCharArray()){
            String lambdaName = getLambdaName(symbol);
            if(!parameterMap.containsKey(lambdaName)) {
                addParameter(createParameter(lambdaName, DefaultValuesConstants.LAMBDA));
            }
        }
    }

    public void removeExtraLamdbas(){
        for(char symbol : extraUsSymbols.toCharArray()){
            String lambdaName = getLambdaName(symbol);
            if(parameterMap.containsKey(lambdaName)) {
                parameterMap.remove(lambdaName);
            }
        }
    }

    private Parameter createGamma(){
        return createParameter(ParameterNamingConstants.GAMMA, DefaultValuesConstants.GAMMA);
    }

    private Parameter createLambdaPlus() {
        return createParameter(getLambdaName('+'), DefaultValuesConstants.LAMBDA);
    }

    private Parameter createParameter(String name, double value){
        Parameter param = new Parameter(name);
        param.setValue(value);
        return param;
    }

    private void addParameter(Parameter param){
        parameterMap.put(param.getDisplayName(), param);
    }

    private String getLambdaName(char us){
        return String.format("%s %s", ParameterNamingConstants.LAMBDA, us);
    }

}
