package Models.Parameters.Pools.CsPools;

import Models.Group;
import Models.Parameters.ConditionalStimulus.CsParameter;
import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Stimulus.IConditionalStimulus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 13/11/2015.
 */
public class CsParameterPool implements ICsParameterPool, Serializable {
    Map<String, InitialAlphaParameter> initialAlphaParameterMap;
    public CsParameterPool(){
        initialAlphaParameterMap = new HashMap<>();
    }

    public void createParameters(String cueName){
        initialAlphaParameterMap.put(cueName, new InitialAlphaParameter(cueName));
    }

    public boolean contains(String cueName){
        return initialAlphaParameterMap.containsKey(cueName);
    }

    public InitialAlphaParameter getInitialAlpha(String cueName){
        return initialAlphaParameterMap.get(cueName);
    }

    public List<CsParameter> getAllParameters(){
        List<CsParameter> list = new ArrayList<>();
        list.addAll(initialAlphaParameterMap.values());
        return list;
    }

    @Override
    public List<CsParameter> getGroupParameters(Group group) {
        List<CsParameter> list = new ArrayList<>();
        for(IConditionalStimulus cs : group.getGroupCues()){
            list.add(initialAlphaParameterMap.get(cs.getName()));
        }
        return list;
    }
}
