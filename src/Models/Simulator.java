package Models;

import Helpers.PhaseParser;
import Helpers.PhaseStringTokenizer;
import Models.Parameters.CsParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 06/11/2015.
 */
public class Simulator {

    private SimPhase phase;

    public List<CsParameter> GetCsParameters(String phaseDescription){
        phase = createPhaseFromDescription(phaseDescription);
        return phase.GetAllCsParameters();
    }

    private SimPhase createPhaseFromDescription(String phaseDescription){
        return PhaseParser.ParsePhase(
                PhaseStringTokenizer.GetPhaseTokens(phaseDescription));
    }


}
