package Models.Parameters.UnconditionalStimulus;

import Constants.DefaultValuesConstants;
import Constants.ParameterNamingConstants;
import Helpers.DefaultValuesHelper;
import Models.Group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rokasg on 09/03/2016.
 */
public class UsParameter implements Serializable, Comparable<UsParameter>{
    private String name;
    private List<Double> values;
    private List<Boolean> availability;
    private Group group;

    public UsParameter(String name, Group group) {
        this.name = name;
        this.group = group;
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

    public Group getGroup() {
        return group;
    }

    @Override
    public int compareTo(UsParameter o) {
        if(!group.equals(o.group)){
            return group.Name.compareTo(o.group.Name);
        }
        return name.compareTo(o.name);
    }
}
