package Models.Parameters.Pools.CsPools;

import Models.Group;
import Models.Parameters.ConditionalStimulus.CsParameter;
import Models.Parameters.ConditionalStimulus.InitialAlphaParameter;
import Models.Parameters.ConditionalStimulus.Rodriguez.InitialAssociationParameter;
import Models.Parameters.ConditionalStimulus.Rodriguez.SalienceParameter;
import Models.Stimulus.IConditionalStimulus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rokas on 29/03/2016.
 */
public class RodriguezCsParameterPool implements ICsParameterPool, Serializable {

    private Map<String, InitialAlphaParameter> initialAlphaParameterMap;
    private Map<String, SalienceParameter> salienceParameterMap;
    private Map<String, InitialAssociationParameter> initialAssociationParameterMap;

    public RodriguezCsParameterPool(){
        initialAlphaParameterMap = new HashMap<>();
        salienceParameterMap = new HashMap<>();
        initialAssociationParameterMap = new HashMap<>();
    }

    @Override
    public void createParameters(String cueName) {
        initialAlphaParameterMap.put(cueName, new InitialAlphaParameter(cueName));
        salienceParameterMap.put(cueName, new SalienceParameter(cueName));
        initialAssociationParameterMap.put(cueName, new InitialAssociationParameter(cueName));
    }

    public boolean contains(String cueName){
        return initialAlphaParameterMap.containsKey(cueName);
    }

    public InitialAlphaParameter getInitialAlphaParameter(String cueName){
        return initialAlphaParameterMap.get(cueName);
    }

    public SalienceParameter getSalienceParameter(String cueName){
        return salienceParameterMap.get(cueName);
    }

    public InitialAssociationParameter getInitialAssociationParameter(String cueName){
        return initialAssociationParameterMap.get(cueName);
    }

    @Override
    public List<CsParameter> getAllParameters() {
        List<CsParameter> list = new ArrayList<>();
        list.addAll(initialAlphaParameterMap.values());
        list.addAll(salienceParameterMap.values());
        list.addAll(initialAssociationParameterMap.values());
        return list;
    }

    @Override
    public List<CsParameter> getGroupParameters(Group group) {
        List<CsParameter> list = new ArrayList<>();
        for(IConditionalStimulus cs : group.getGroupCues()){
            list.add(initialAlphaParameterMap.get(cs.getName()));
            list.add(salienceParameterMap.get(cs.getName()));
            list.add(initialAssociationParameterMap.get(cs.getName()));
        }
        return list;
    }
}
