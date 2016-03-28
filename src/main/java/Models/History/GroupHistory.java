package Models.History;

import Models.Group;
import Models.Parameters.Pools.CsParameterPool;
import Models.Parameters.Parameter;
import Models.Parameters.UnconditionalStimulus.UsParameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 16/11/2015.
 */
public class GroupHistory implements Serializable {
    public Group group;
    private List<GroupPhaseHistory> phaseHistories;
    public CsParameterPool csParameterPool;
    public CsParameterPool contextParameterPool;
    public List<Parameter> globalParameters;
    public List<UsParameter> usParameters;
    public GroupHistory(Group group, CsParameterPool csParameterPool, List<Parameter> globalParameters, List<UsParameter> usParameters){
        this.group = group;
        this.csParameterPool = csParameterPool;
        this.globalParameters = globalParameters;
        this.usParameters = usParameters;
        phaseHistories = new ArrayList<>();
    }

    public void add(GroupPhaseHistory gpHist){
        phaseHistories.add(gpHist);
    }

    public GroupPhaseHistory getGroupPhaseHistory(int phaseId){
        return phaseHistories.get(phaseId);
    }

    public int getNumberOfPhases(){
        return phaseHistories.size();
    }

    public CsParameterPool getContextParameterPool() {
        return contextParameterPool;
    }

    public void setContextParameterPool(CsParameterPool contextParameterPool) {
        this.contextParameterPool = contextParameterPool;
    }
}
