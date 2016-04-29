package Models.Parameters.UnconditionalStimulus;

import Constants.DefaultValuesConstants;

import java.io.Serializable;

/**
 * Created by Rokas on 28/03/2016.
 */
public class SingleUsParamater extends UsParameter implements Serializable {

    private double value;

    public SingleUsParamater(String name) {
        super(name, null);
        value = DefaultValuesConstants.LAMBDA;
    }

    public void setValue(int phaseId, double value){
        this.value = value;
    }

    public double getValue(int phaseId){
        return value;
    }

    public boolean isAvailable(int phaseId){

        return true;
    }
}
