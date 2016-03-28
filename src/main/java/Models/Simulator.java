package Models;

import Helpers.ReportBuilder;
import Models.History.GroupHistory;
import Models.History.SimulationHistory;
import Models.Parameters.ConditionalStimulus.CsParameter;
import Models.Parameters.Parameter;
import Models.Parameters.Pools.CsParameterPool;
import Models.Parameters.Pools.GlobalParameterPool;
import Models.Parameters.Pools.UsParameterPool;
import ViewModels.GroupReportViewModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rokas on 06/11/2015.
 */
public class Simulator implements Serializable{
    private SimulatorSettings settings;
    private List<Group> groups;
    private CsParameterPool csParameterPool;
    private GlobalParameterPool globalParameterPool;
    private List<GroupReportViewModel> report;
    private SimulationHistory simulationHistory;

    public Simulator(){
        settings = new SimulatorSettings();
        globalParameterPool = new GlobalParameterPool();
    }

    public void setCsParameterPool(CsParameterPool csParameterPool){
        this.csParameterPool = csParameterPool;
    }

    public void setGroups(List<Group> groups){
        this.groups = groups;
    }

    public List<CsParameter> getCsParameters(){
        return csParameterPool.getAllParameters();
    }

    public UsParameterPool getUsParameterPool(){
        adjustUsParameters();
        return globalParameterPool.getUsParameterPool();
    }

    public List<Parameter> getGlobalParameters(){
        return globalParameterPool.getGlobalParameters();
    }

    public List<Group> getGroups(){
        return groups;
    }

    public void runSimulation(){
        resetPhases();
        SimulationHistory simHistory = new SimulationHistory();
        for(Group group : groups){
            GroupHistory groupHistory = createGroupHistory(group);
            for(GroupPhase groupPhase : group.groupPhases) {
                groupHistory.add(
                        groupPhase.simulateTrials(globalParameterPool, settings));
            }
            simHistory.add(groupHistory);
        }

        simulationHistory = simHistory;
        report = ReportBuilder.buildReport(simHistory);
    }

    private GroupHistory createGroupHistory(Group group){
        GroupHistory history = new GroupHistory(group, csParameterPool, getGlobalParameters(), getUsParameterPool().getUsParameters());
        if(settings.ContextSimulation){
            history.setContextParameterPool(group.getContextParameterPool());;
        }
        return history;
    }

    private void resetPhases(){
        for(Group group : groups) {
            for(GroupPhase groupPhase : group.groupPhases) {
                groupPhase.reset();
            }
        }
    }

    public List<GroupReportViewModel> getLatestReport(){
        return report;
    }

    public SimulationHistory getLatestSimHistory(){
        return simulationHistory;
    }

    public SimulatorSettings getSettings(){
        return settings;
    }

    private void adjustUsParameters() {
        if(groups!=null && !groups.isEmpty()){
            if(settings.UseDifferentUs) {
                globalParameterPool.getUsParameterPool().adjustLamdbas(groups);
            }else{
                globalParameterPool.getUsParameterPool().adjustSingleMode();
            }
        }
    }
}
