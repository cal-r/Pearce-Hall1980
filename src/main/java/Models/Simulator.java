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

    private List<Group> groups;
    private GammaParameter gamma;
    private CsParameterPool csParameterPool;
    private List<GroupReportViewModel> report;
    private SimulationHistory simulationHistory;

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

    public void runSimulation(){
        resetPhases();
        SimulationHistory simHistory = new SimulationHistory();
        for(Group group : groups){
            GroupHistory groupHistory = createGroupHistory(group);
            for(Phase phase : group.phases) {
                groupHistory.add(
                        phase.simulateTrails(gamma));
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
            for(Phase phase : group.phases) {
                phase.reset();
            }
        }
    }

    public List<GroupReportViewModel> getLatestReport(){
        return report;
    }

    public SimulationHistory getLatestSimHistory(){
        return simulationHistory;
    }
}
