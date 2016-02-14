package Models;

import Models.Stimulus.ConditionalStimulus;
import Models.Stimulus.Stimulus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class Trial implements Serializable {
    public boolean usPresent;
    public List<Stimulus> cuesPresent;

    public Trial(boolean usPresent, List<Stimulus> cuesPresent){
        this.usPresent = usPresent;
        this.cuesPresent = cuesPresent;
    }

    public void simulate(double vNet, double gamma){
        double lambda = getLamba();
        double capitalLambda = lambda - vNet;
        for(Stimulus stimulus : cuesPresent){
            if(stimulus instanceof ConditionalStimulus) {
                ConditionalStimulus cs = (ConditionalStimulus) stimulus;
                double newAlpha = getNewAlpha(gamma, lambda, vNet, cs.getAlpha());
                if (usPresent && capitalLambda > 0) {
                    updateDeltaVe(cs, lambda);
                } else if (!usPresent && capitalLambda < 0) {
                    updateDeltaVi(cs, capitalLambda);
                }
                cs.setAlpha(newAlpha);
            }
        }
    }

    private void updateDeltaVe(ConditionalStimulus cs, double lambda){
        double deltaVe = cs.SalienceExcitatoryParameter.getValue() * cs.getAlpha() * lambda;
        cs.updateAssociationExcitatory(deltaVe);
    }

    private void updateDeltaVi(ConditionalStimulus cs, double capitalLambda){
        double deltaVi = cs.SalienceExcitatoryParameter.getValue() * cs.getAlpha() * Math.abs(capitalLambda);
        cs.updateAssociationInhibitory(deltaVi);
    }

    private double getLamba(){
        return usPresent ? 1 : 0;
    }

    private double getNewAlpha(double gamma, double lambda, double vNet, double oldAlpha){
        return gamma * Math.abs(lambda - vNet) + (1 - gamma) * oldAlpha;
    }
}
