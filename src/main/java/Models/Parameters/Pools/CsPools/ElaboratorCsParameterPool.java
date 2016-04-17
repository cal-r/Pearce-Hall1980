package Models.Parameters.Pools.CsPools;

import Models.Group;
import Models.Parameters.ConditionalStimulus.CsParameter;
import Models.Parameters.ConditionalStimulus.Rodriguez.InitialAssociationParameter;
import Models.Stimulus.IConditionalStimulus;

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

    public List<CsParameter> getGroupParameters(Group group){
        List<CsParameter> params = super.getGroupParameters(group);
        for(IConditionalStimulus cs : group.getGroupCues()){
            params.add(associationParameterMap.get(cs.getName()));
        }
        return params;
    }
}
