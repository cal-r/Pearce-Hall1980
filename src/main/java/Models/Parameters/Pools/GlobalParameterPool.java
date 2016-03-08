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

    private List<Parameter> globalParameters;
    private Map<String, Parameter> usParameterMap;
    public GlobalParameterPool(){
        //globals
        globalParameters = new ArrayList<>();
        globalParameters.add(createGamma());
        //us
        usParameterMap = new HashMap<>();
        addParameter(createLambdaPlus());
    }

    public List<Parameter> getUsParameters(){
        return new ArrayList<>(usParameterMap.values());
    }

    public List<Parameter> getGlobalParameters(){
        return globalParameters;
    }

    public Parameter getGamma(){
        return globalParameters.get(0);
    }

    public Parameter getLambda(char usSymbol){
        return usParameterMap.get(getLambdaName(usSymbol));
    }

    public void addExtraLambdas(){
        for(char symbol : extraUsSymbols.toCharArray()){
            String lambdaName = getLambdaName(symbol);
            if(!usParameterMap.containsKey(lambdaName)) {
                addParameter(createParameter(lambdaName, DefaultValuesConstants.LAMBDA));
            }
        }
    }

    public void removeExtraLamdbas(){
        for(char symbol : extraUsSymbols.toCharArray()){
            String lambdaName = getLambdaName(symbol);
            if(usParameterMap.containsKey(lambdaName)) {
                usParameterMap.remove(lambdaName);
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
        usParameterMap.put(param.getDisplayName(), param);
    }

    private String getLambdaName(char us){
        return String.format("%s %s", ParameterNamingConstants.LAMBDA, us);
    }

}
