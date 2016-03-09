package Models.Parameters;

import Constants.DefaultValuesConstants;
import Constants.ParameterNamingConstants;
import Helpers.DefaultValuesHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rokasg on 09/03/2016.
 */
public class UsParameter{
    private String name;
    private List<Double> values;

    public UsParameter(String name) {
        this.name = name;
        values = new ArrayList<>();
    }

    public double getValue(int phaseId) {
        return values.get(phaseId);
    }

    public void adjust(List<Integer> availability){

    }

    public String getDisplayName(){
        return name;
    }
}
