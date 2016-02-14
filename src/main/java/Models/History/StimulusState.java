package Models.History;

import Models.Stimulus.Stimulus;

import java.io.Serializable;

/**
 * Created by Rokas on 09/02/2016.
 */
public class StimulusState implements Serializable {
    public double Vnet;

    public StimulusState(){}

    public StimulusState(Stimulus stim){
        Vnet = stim.getAssociationNet();
    }

    public void addValues(StimulusState stateToAdd){
        Vnet += stateToAdd.Vnet;
    }

    public void divideValues(int divider){
        Vnet /= divider;
    }
}
