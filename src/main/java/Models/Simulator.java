package Models;

import Helpers.ReportBuilder;
import Models.History.GroupHistory;
import Models.History.SimulationHistory;
import Models.Parameters.CsParameter;
import Models.Parameters.GammaParameter;
import Models.Parameters.Parameter;
import Models.Parameters.CsParameterPool;
import ViewModels.GroupReportViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 06/11/2015.
 */
public class Simulator {
    private SimulatorSettings settings;

    private List<Group> groups;
    private GammaParameter gamma;
    private CsParameterPool csParameterPool;
    private List<GroupReportViewModel> report;
    private SimulationHistory simulationHistory;

    public Simulator(){
        settings = new SimulatorSettings();
        gamma = new GammaParameter();
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

    public List<Parameter> getGlobalParameters(){
        List<Parameter> globals = new ArrayList<>();
        globals.add(gamma);
        return globals;
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
                        groupPhase.simulateTrials(gamma));
            }
            simHistory.add(groupHistory);
        }

        simulationHistory = simHistory;
        report = ReportBuilder.buildReport(simHistory);
    }

    private GroupHistory createGroupHistory(Group group){
        return new GroupHistory(group, csParameterPool, getGlobalParameters());
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

    public SimulatorSettings getSimulatorSettings(){
        return settings;
    }
}
