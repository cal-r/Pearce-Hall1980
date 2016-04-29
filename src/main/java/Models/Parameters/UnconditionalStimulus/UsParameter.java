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
    private String groupName;
    private int displayId;

    public UsParameter(String name, String groupName) {
        this.name = name;
        this.groupName = groupName;
        values = new ArrayList<>();
        availability = new ArrayList<>();
        displayId = 0;
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

    public String getGroupName() {
        return groupName;
    }

    @Override
    public int compareTo(UsParameter o) {
        if(groupName != null && !groupName.equals(o.groupName)){
            return groupName.compareTo(o.groupName);
        }
        return Integer.compare(displayId, o.displayId);
    }

    public void setDisplayId(int displayId) {
        this.displayId = displayId;
    }
}
