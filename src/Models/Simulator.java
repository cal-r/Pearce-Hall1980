package Models;

import Helpers.PhaseParser;
import Helpers.PhaseStringTokenizer;
import Models.Parameters.Parameter;

import java.util.List;

/**
 * Created by Rokas on 06/11/2015.
 */
public class Simulator {

    private SimPhase phase;

    public void innitPhase(String phaseDescription) {
        phase = createPhaseFromDescription(phaseDescription);
    }

    public List<Parameter> getCsParameters(){
        return phase.getAllCsParameters();
    }

    public List<Parameter> getGlobalParameters(){
        return phase.getAllGlobalParameters();
    }

    private SimPhase createPhaseFromDescription(String phaseDescription){
        return PhaseParser.ParsePhase(
                PhaseStringTokenizer.getPhaseTokens(phaseDescription));
    }


}
