package Models.Trail;

import Models.Parameters.Parameter;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.Stimulus.ConditionalStimulus;
import Models.Stimulus.Stimulus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rokas on 20/02/2016.
 */
public class LearningPeriod implements Serializable {
    public boolean usPresent;
    public char reinforcer;
    public List<Stimulus> stims;

    public LearningPeriod(boolean usPresent, char reinforcer, List<Stimulus> stims) {
        this.usPresent = usPresent;
        this.stims = stims;
        this.reinforcer = reinforcer;
    }

    public void learn(double vNet, GlobalParameterPool globalParams) {
        double lambda = getLamba(globalParams);
        double capitalLambda = lambda - vNet;
        for (Stimulus stimulus : stims) {
            if (stimulus instanceof ConditionalStimulus) {
                ConditionalStimulus cs = (ConditionalStimulus) stimulus;
                double newAlpha = getNewAlpha(globalParams.getGamma(), lambda, vNet, cs.getAlpha());
                if (usPresent && capitalLambda > 0) {
                        updateDeltaVe(cs, lambda);
                } else if (!usPresent && capitalLambda < 0) {
                    updateDeltaVi(cs, capitalLambda);
                }
                cs.setAlpha(newAlpha);
            }
        }
    }

    private void updateDeltaVe(ConditionalStimulus cs, double lambda) {
        double deltaVe = cs.SalienceExcitatoryParameter.getValue() * cs.getAlpha() * lambda;
        cs.updateAssociationExcitatory(deltaVe);
    }

    private void updateDeltaVi(ConditionalStimulus cs, double capitalLambda) {
        double deltaVi = cs.SalienceExcitatoryParameter.getValue() * cs.getAlpha() * Math.abs(capitalLambda);
        cs.updateAssociationInhibitory(deltaVi);
    }

    private double getLamba(GlobalParameterPool globals)
    {
        if(!usPresent){
            return 0;
        }
        return globals.getLambda(reinforcer).getValue();
    }

    private double getNewAlpha(Parameter gamma, double lambda, double vNet, double oldAlpha) {
        return gamma.getValue() * Math.abs(lambda - vNet) + (1 - gamma.getValue()) * oldAlpha;
    }
}