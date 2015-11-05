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
        map.put(ParameterNamingConstants.INITIAL_ALPHA, DefaultValuesConstants.INNITIAL_ALPHA);
        map.put(ParameterNamingConstants.SALIENCE_INHIBATORY, DefaultValuesConstants.SALIENCE_INHIBATORY);
        map.put(ParameterNamingConstants.SALIENCE_EXCITATORY, DefaultValuesConstants.SALIENCE_EXCITATORY);
    }

    public static double GetDefaultValue(String variableName){
        if(map.containsKey(variableName)) {
            return map.get(variableName);
        }
        return 0;
    }
}
