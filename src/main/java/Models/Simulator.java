package Models;

import Models.History.GroupHistory;
import Models.History.PhaseHistory;
import Models.Parameters.CsParameter;
import Models.Parameters.GammaParameter;
import Models.Parameters.Parameter;
import Models.Parameters.CsParameterPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 06/11/2015.
 */
public class Simulator {

    private List<Group> groups;
    private GammaParameter gamma;
    private CsParameterPool csParameterPool;

    public Simulator(CsParameterPool csParameterPool, List<Group> groups){
        this.csParameterPool = csParameterPool;
        this.groups = groups;
        gamma = new GammaParameter();
    }

    public List<CsParameter> getCsParameters(){
        return csParameterPool.getAllParameters();
    }

    public List<Parameter> getGlobalParameters(){
        List<Parameter> globals = new ArrayList<>();
        globals.add(gamma);
        return globals;
    }

    public List<Group> getGroups(){
        return groups;
    }

    public List<GroupHistory> runSimulation(){
        List<GroupHistory> histories = new ArrayList<>();
        for(Group group : groups){
            GroupHistory groupHistory = new GroupHistory(group);
            for(Phase phase : group.phases) {
                PhaseHistory phaseHistory = phase.simulateTrails(gamma);
                groupHistory.phaseHistories.add(phaseHistory);
            }
            histories.add(groupHistory);
        }
        return histories;
    }
}
