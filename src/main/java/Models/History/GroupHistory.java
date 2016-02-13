package Models.History;

import Models.Group;
import Models.Parameters.CsParameterPool;
import Models.Parameters.Parameter;

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
    public List<Parameter> globalParameters;
    public GroupHistory(Group group, CsParameterPool csParameterPool, List<Parameter> globalParameters){
        this.group = group;
        this.csParameterPool = csParameterPool;
        this.globalParameters = globalParameters;
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
}
