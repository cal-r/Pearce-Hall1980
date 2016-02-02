package Models;

import Constants.DefaultValuesConstants;

/**
 * Created by Rokas on 02/02/2016.
 */
public class SimulatorSettings {
    public boolean CompoundResults;
    public boolean ConfiguralCues;
    public int NumberOfRandomCombination;
    public SimulatorSettings(){
        CompoundResults = DefaultValuesConstants.COMPOUND_RESULTS;
        ConfiguralCues = DefaultValuesConstants.CONFIGURAL_CUES;
        NumberOfRandomCombination = DefaultValuesConstants.NUMBER_OF_RANDOM_COMBINATIONS;
    }
}
