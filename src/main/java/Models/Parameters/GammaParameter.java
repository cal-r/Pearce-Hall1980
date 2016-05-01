package Models.Parameters;

import Constants.DefaultValuesConstants;
import Constants.ParameterNamingConstants;
import Models.Simulator;
import Models.SimulatorSettings;

/**
 * Created by Rokas on 09/04/2016.
 */
public class GammaParameter extends Parameter {

    private final SimulatorSettings settings;

    public GammaParameter(SimulatorSettings settings) {
        super(ParameterNamingConstants.GAMMA);
        this.settings = settings;
    }

    public void setValue(double value){
        super.setValue(value);
    }

    boolean lastSettingValue;

    public double getValue(){
        double val = getValueInner();
        lastSettingValue = settings.isRodriguezMode();
        return val;
    }

    private double getValueInner(){
        if(isSet && lastSettingValue == settings.isRodriguezMode())
            return super.getValue();

        return settings.isRodriguezMode() ? DefaultValuesConstants.RODRIGUEZ_GAMMA : DefaultValuesConstants.GAMMA;
    }
}
