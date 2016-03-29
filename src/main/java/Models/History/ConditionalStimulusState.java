package Models.History;

import Models.Stimulus.ConditionalStimulus;
import Models.Stimulus.RodriguezStimulus;

import java.io.Serializable;

/**
 * Created by Rokas on 31/01/2016.
 */
public class ConditionalStimulusState extends StimulusState implements Serializable {
    public double Ve;
    public double Vi;
    public double Alpha;

    public ConditionalStimulusState(){}

    public ConditionalStimulusState(ConditionalStimulus cs){
        super(cs);
        Ve = cs.getAssociationExcitatory();
        Vi = cs.getAssociationInhibitory();
        Alpha = cs.getAlpha();
    }

    public ConditionalStimulusState(RodriguezStimulus rs){
        super(rs);
        Ve = rs.getAssociationEvent();
        Vi = rs.getAssociationNoEvent();
        Alpha = rs.getAlpha();
    }

    public void addValues(StimulusState stateToAdd){
        super.addValues(stateToAdd);
        ConditionalStimulusState csStateToAdd = (ConditionalStimulusState) stateToAdd;
        Ve += csStateToAdd.Ve;
        Vi += csStateToAdd.Vi;
        Alpha += csStateToAdd.Alpha;
    }

    public void divideValues(int divider){
        super.divideValues(divider);
        Ve /= divider;
        Vi /= divider;
        Alpha /= divider;
    }
}

