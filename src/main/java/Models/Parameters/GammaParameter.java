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

    public double getValue(){
        if(isSet)
            return super.getValue();

        return settings.isRodriguezMode() ? DefaultValuesConstants.RODRIGUEZ_GAMMA : DefaultValuesConstants.GAMMA;
    }
}
