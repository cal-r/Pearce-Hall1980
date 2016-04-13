package Models.Parameters.Pools.CsPools;

import Models.Parameters.ConditionalStimulus.CsParameter;
import Models.Parameters.ConditionalStimulus.Rodriguez.InitialAssociationParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 12/04/2016.
 */
public class ElaboratorCsParameterPool extends CsParameterPool {
    Map<String, InitialAssociationParameter> associationParameterMap;
    public ElaboratorCsParameterPool(){
        super();
        associationParameterMap = new HashMap<>();
    }

    public void createParameters(String cueName){
        super.createParameters(cueName);
        associationParameterMap.put(cueName, new InitialAssociationParameter(cueName));
    }

    public InitialAssociationParameter getVeParameter(String cueName){
        return associationParameterMap.get(cueName);
    }

    public List<CsParameter> getAllParameters(){
        List<CsParameter> params = super.getAllParameters();
        params.addAll(associationParameterMap.values());
        return params;
    }
}
