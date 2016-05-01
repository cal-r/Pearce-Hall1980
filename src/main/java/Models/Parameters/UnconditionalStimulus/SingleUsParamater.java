package Models.Parameters.UnconditionalStimulus;

import Constants.DefaultValuesConstants;
import Models.SimulatorSettings;

import java.io.Serializable;

/**
 * Created by Rokas on 28/03/2016.
 */
public class SingleUsParamater extends UsParameter implements Serializable {

    private double value;
    private boolean isSet;
    private SimulatorSettings settings;

    public SingleUsParamater(String name, SimulatorSettings settings) {
        super(name, null);
        this.settings = settings;
        isSet = false;
    }

    public void setValue(int phaseId, double value){
        this.value = value;
        isSet = true;
    }

    public double getValue(int phaseId){
        if(isSet) {
            return value;
        }else{
            return settings.isRodriguezMode() ? 0 : DefaultValuesConstants.LAMBDA;
        }
    }

    public boolean isAvailable(int phaseId){

        return true;
    }
}
