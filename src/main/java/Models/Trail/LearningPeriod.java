package Models.Trail;

import Models.Parameters.Parameter;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.Stimulus.ConditionalStimulus;
import Models.Stimulus.IStimulus;
import Models.Stimulus.MultipleStimulus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rokas on 20/02/2016.
 */
public class LearningPeriod implements Serializable {
    public boolean usPresent;
    public char reinforcer;
    public List<IStimulus> stims;

    public LearningPeriod(boolean usPresent, char reinforcer, List<IStimulus> stims) {
        this.usPresent = usPresent;
        this.stims = stims;
        this.reinforcer = reinforcer;
    }

    public void learn(double vNet, GlobalParameterPool globalParams, int phaseNumber) {
        double lambda = getLamba(globalParams, phaseNumber);
        double capitalLambda = lambda - vNet;
        for (IStimulus stimulus : stims) {
            if (stimulus instanceof ConditionalStimulus) {
                ConditionalStimulus cs = (ConditionalStimulus) stimulus;
                stimulateCs(cs, globalParams, lambda, capitalLambda, vNet);
            }
            if(stimulus instanceof MultipleStimulus){
                MultipleStimulus ms = (MultipleStimulus) stimulus;
                for(ConditionalStimulus cs : ms.getStims(reinforcer)){
                    stimulateCs(cs, globalParams, lambda, capitalLambda, vNet);
                }
            }
        }
    }

    private void stimulateCs(ConditionalStimulus cs, GlobalParameterPool globalParams, double lambda, double capitalLambda, double vNet){
        double newAlpha = getNewAlpha(globalParams.getGamma(), lambda, vNet, cs.getAlpha());
        if (usPresent && capitalLambda > 0) {
            updateDeltaVe(cs, lambda);
        } else if (!usPresent && capitalLambda < 0) {
            updateDeltaVi(cs, capitalLambda);
        }
        cs.setAlpha(newAlpha);
    }

    private void updateDeltaVe(ConditionalStimulus cs, double lambda) {
        double deltaVe = cs.SalienceExcitatoryParameter.getValue() * cs.getAlpha() * lambda;
        cs.updateAssociationExcitatory(deltaVe);
    }

    private void updateDeltaVi(ConditionalStimulus cs, double capitalLambda) {
        double deltaVi = cs.SalienceExcitatoryParameter.getValue() * cs.getAlpha() * Math.abs(capitalLambda);
        cs.updateAssociationInhibitory(deltaVi);
    }

    private double getLamba(GlobalParameterPool globals, int phaseNumber) {
        if(!usPresent){
            return 0;
        }
        return globals.getUsParameterPool().getLambda(reinforcer).getValue(phaseNumber);
    }

    private double getNewAlpha(Parameter gamma, double lambda, double vNet, double oldAlpha) {
        return gamma.getValue() * Math.abs(lambda - vNet) + (1 - gamma.getValue()) * oldAlpha;
    }
}