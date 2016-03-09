package Models.Parameters;

import Constants.DefaultValuesConstants;
import Constants.ParameterNamingConstants;
import Helpers.DefaultValuesHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rokasg on 09/03/2016.
 */
public class UsParameter{
    private String name;
    private List<Double> values;
    private List<Boolean> availability;

    public UsParameter(String name) {
        this.name = name;
        values = new ArrayList<>();
    }

    public void setValue(int phaseId, double value){
        values.set(phaseId - 1, value);
    }

    public double getValue(int phaseId){
        return values.get(phaseId-1);
    }

    public boolean isAvailable(int phaseId){
        return availability.get(phaseId);
    }

    public void adjust(List<Integer> phasesWherePresent, int phaseCount){
        availability = new ArrayList<>();
        for(int i=0;i<phaseCount;i++){
            availability.add(phasesWherePresent.contains(i));
            if(values.size()==i){
                values.add(DefaultValuesConstants.LAMBDA);
            }
        }
    }

    public String getDisplayName(){
        return name;
    }
}
