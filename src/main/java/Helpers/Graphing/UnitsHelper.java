package Helpers.Graphing;

import java.util.List;

/**
 * Created by Rokas on 22/04/2016.
 */
public class UnitsHelper {
    public static String getYUnits(List<GraphBuilder.Variable> variables){
        if(variables.size()>1){
            return "V";
        }
        return variables.get(0).toString();
    }
}
