package Models;

import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class Trail {
    public boolean usPresent;
    public List<ConditionalStimulus> cuesPresent;

    public Trail(boolean usPresent, List<ConditionalStimulus> cuesPresent){
        this.usPresent = usPresent;
        this.cuesPresent = cuesPresent;
    }

    public void simulate(double vNet, double gamma){
        double lambda = usPresent ? 1 : 0;
        double capitalLambda = lambda - vNet;
        for(ConditionalStimulus cs : cuesPresent){
            double newAlpha = gamma * Math.abs(lambda - vNet) + (1 - gamma) * cs.getAlpha();
            if(usPresent) {
                double deltaVe = cs.SalienceExcitatoryParameter.getValue() * cs.getAlpha() * lambda;
                cs.AssociationExcitatory += deltaVe;
            }else {
                double deltaVi = cs.SalienceExcitatoryParameter.getValue() * cs.getAlpha() * Math.abs(capitalLambda);
                cs.AssociationInhibitory += deltaVi;
            }
            cs.setAlpha(newAlpha);
        }
    }
}
