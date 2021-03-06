package Helpers;

import Constants.DefaultValuesConstants;
import Constants.ParameterNamingConstants;

import java.util.HashMap;

/**
 * Created by Rokas on 05/11/2015.
 */
public class DefaultValuesHelper {

    private static final HashMap<String, Double> map;

    static {
        map = new HashMap<>();
        map.put(ParameterNamingConstants.INITIAL_ALPHA, DefaultValuesConstants.INITIAL_ALPHA);
        map.put(ParameterNamingConstants.BETA_INHIBITORY, DefaultValuesConstants.SALIENCE_INHIBATORY);
        map.put(ParameterNamingConstants.BETA_EXCITATORY, DefaultValuesConstants.SALIENCE_EXCITATORY);
        map.put(ParameterNamingConstants.GAMMA, DefaultValuesConstants.GAMMA);
        map.put(ParameterNamingConstants.INITIAL_ASSOCIATION, DefaultValuesConstants.INITIAL_ASSOCIATION);
        map.put(ParameterNamingConstants.SALIENCE, DefaultValuesConstants.SALIENCE);
    }

    public static double GetDefaultValue(String variableName){
        if(map.containsKey(variableName)) {
            return map.get(variableName);
        }
        return 0;
    }
}
