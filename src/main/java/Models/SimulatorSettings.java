package Models;

import Constants.DefaultValuesConstants;

import java.io.Serializable;

/**
 * Created by Rokas on 02/02/2016.
 */
public class SimulatorSettings implements Serializable {
    private boolean compoundResults;
    private boolean contextSimulation;
    private boolean useDifferentUs;
    private boolean rodriguezMode;
    private boolean useInitialVe;
    private int numberOfRandomCombination;

    public SimulatorSettings(){
        compoundResults = DefaultValuesConstants.COMPOUND_RESULTS;
        contextSimulation = DefaultValuesConstants.CONTEXT_SIMULATION;
        numberOfRandomCombination = DefaultValuesConstants.NUMBER_OF_RANDOM_COMBINATIONS;
        useDifferentUs = DefaultValuesConstants.USE_DIFFERENT_US;
        rodriguezMode = DefaultValuesConstants.RODRIGUEZ_MODE;
        useInitialVe = DefaultValuesConstants.USE_INITIAL_VE;
    }

    public boolean isCompoundResults() {
        return compoundResults;
    }

    public void setCompoundResults(boolean compoundResults) {
        this.compoundResults = compoundResults;
    }

    public boolean isContextSimulation() {
        return contextSimulation;
    }

    public void setContextSimulation(boolean contextSimulation) {
        this.contextSimulation = contextSimulation;
    }

    public boolean isUseDifferentUs() {
        return useDifferentUs;
    }

    public void setUseDifferentUs(boolean useDifferentUs) {
        rodriguezMode = false;
        this.useDifferentUs = useDifferentUs;
    }

    public boolean isRodriguezMode() {
        return rodriguezMode;
    }

    public void setRodriguezMode(boolean rodriguezMode) {
        useDifferentUs = false;
        contextSimulation = false;
        useInitialVe = false;
        this.rodriguezMode = rodriguezMode;
    }

    public boolean isUseInitialVe() {
        return useInitialVe;
    }

    public void setUseInitialVe(boolean useInitialVe) {
        rodriguezMode = false;
        this.useInitialVe = useInitialVe;
    }

    public int getNumberOfRandomCombination() {
        return numberOfRandomCombination;
    }

    public void setNumberOfRandomCombination(int numberOfRandomCombination) {
        this.numberOfRandomCombination = numberOfRandomCombination;
    }
}
