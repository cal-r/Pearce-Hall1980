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
        availability = new ArrayList<>();
    }

    public void setValue(int phaseId, double value){
        values.set(phaseId, value);
    }

    public double getValue(int phaseId){
        if(availability.size()<=phaseId || !availability.get(phaseId)){
            return DefaultValuesConstants.LAMBDA;
        }
        return values.get(phaseId);
    }

    public boolean isAvailable(int phaseId){
        return availability.size()> phaseId && availability.get(phaseId);
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

    public int getPhaseCount(){
        return availability.size();
    }

    public String getDisplayName(){
        return name;
    }
}
