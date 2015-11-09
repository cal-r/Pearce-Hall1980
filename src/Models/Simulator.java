package Models;

import Helpers.PhaseParser;
import Helpers.PhaseStringTokenizer;
import Models.Parameters.Parameter;

import java.util.List;

/**
 * Created by Rokas on 06/11/2015.
 */
public class Simulator {

    private Phase phase;

    private int lastTrailIndex = 0;

    public void initPhase(String phaseDescription) {
        phase = createPhaseFromDescription(phaseDescription);
        lastTrailIndex = 0;
    }

    public List<Parameter> getCsParameters(){
        return phase.getAllCsParameters();
    }

    public List<Parameter> getGlobalParameters(){
        return phase.getAllGlobalParameters();
    }

    public PhaseHistory simulatePhase(){
        PhaseHistory history = new PhaseHistory(phase);
        while (!simulationComplete()) {
            phase.simulateTrail(lastTrailIndex);
            history.recordState(lastTrailIndex);
            lastTrailIndex++;
        }
        return history;
    }

    private boolean simulationComplete(){
        return lastTrailIndex>=phase.trails.size();
    }

    private Phase createPhaseFromDescription(String phaseDescription){
        return PhaseParser.ParsePhase(
                PhaseStringTokenizer.getPhaseTokens(phaseDescription));
    }


}
