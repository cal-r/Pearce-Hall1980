package Helpers;

import Constants.GuiStringConstants;
import Models.ConditionalStimulus;
import Models.GroupPhase;
import Models.History.CsState;
import Models.History.GroupHistory;
import Models.History.GroupPhaseHistory;
import Models.History.SimulationHistory;
import Models.Parameters.CsParameter;
import Models.Parameters.CsParameterPool;
import Models.Parameters.Parameter;
import Models.Trial;
import ViewModels.GroupReportViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 22/12/2015.
 */
public class ReportBuilder {

    public static List<GroupReportViewModel> buildReport(SimulationHistory history){
        List<GroupReportViewModel> groupReports = new ArrayList<>();
        for(GroupHistory groupHistory : history){
            groupReports.add(buildGroupReport(groupHistory));
        }
        return groupReports;
    }

    private static GroupReportViewModel buildGroupReport(GroupHistory groupHistory){
        GroupReportViewModel report = new GroupReportViewModel(groupHistory.group.Name);
        int rowId = 0;
        report.setCell(rowId++, 0, "P&H parameters:");
        rowId = insertCsParameterTable(report, rowId, groupHistory.csParameterPool) +1;
        rowId = insertGlobalParameterTable(report, rowId, groupHistory.globalParameters) + 2;

        report.setCell(rowId++, 0, groupHistory.group.Name);
        for(int gpId = 0; gpId < groupHistory.getNumberOfPhases(); gpId++) {
            GroupPhaseHistory groupPhaseHistory = groupHistory.getGroupPhaseHistory(gpId);
            insertPhaseDescription(report, rowId++, groupPhaseHistory.getGroupPhase());
            rowId++;
            for(Variable variable : Variable.values())
            {
                report.setCell(rowId++, 0, variable.toString());
                int lastRowId = insertVariableTable(report, rowId, groupPhaseHistory, variable);
                rowId = lastRowId+1;
            }
        }
        return report;
    }

    private static int insertCsParameterTable(GroupReportViewModel report, int rowId, CsParameterPool pool){
        report.setCell(rowId, 0, GuiStringConstants.CS_PARAMETER);
        report.setCell(rowId, 1, GuiStringConstants.VALUE);
        rowId++;
        for(CsParameter csParameter : pool.getAllParameters()){
            report.setCell(rowId, 0, csParameter.getDisplayName());
            report.setCell(rowId, 1, csParameter.getValue());
            rowId++;
        }
        return rowId;
    }

    private static int insertGlobalParameterTable(GroupReportViewModel report, int rowId, List<Parameter> params){
        report.setCell(rowId, 0, GuiStringConstants.GLOBAL_PARAMETER);
        report.setCell(rowId, 1, GuiStringConstants.VALUE);
        rowId++;
        for(Parameter parameter : params){
            report.setCell(rowId, 0, parameter.getDisplayName());
            report.setCell(rowId, 1, parameter.getValue());
            rowId++;
        }
        return rowId;
    }

    private static int insertVariableTable(GroupReportViewModel report, int rowId, GroupPhaseHistory groupPhaseHistory, Variable variable){
        int currRowId = rowId;

        currRowId++;
        for(ConditionalStimulus cs : groupPhaseHistory.getOrderedCues()) {
            report.setCell(currRowId++, 0, cs.Name);
        }
        currRowId = rowId;
        int colId = 1;

        for(int trialNo=1;trialNo<= groupPhaseHistory.getNumberOfTrials();trialNo++) {
            report.setCell(currRowId++, colId, String.format("trial %1$d", trialNo));
            for(ConditionalStimulus cs : groupPhaseHistory.getOrderedCues()) {
                CsState state = groupPhaseHistory.getState(cs, trialNo);
                report.setCell(currRowId++, colId, getValue(state, variable));
            }
            currRowId = rowId;
            colId++;
        }
        return rowId + groupPhaseHistory.getOrderedCues().size()+1;
    }

    private static void insertPhaseDescription(GroupReportViewModel report, int rowId, GroupPhase groupPhase){
        int colId = 0;
        report.setCell(rowId, colId++, groupPhase.toString()); //append name ("Phase 69")
        report.setCell(rowId, colId++, constructTrialsString(groupPhase.trials));
        colId++;
        report.setCell(rowId, colId++, GuiStringConstants.RANDOM + ": "+ groupPhase.isRandom());
    }

    private static String constructTrialsString(List<Trial> trials){
        HashMap<String, Integer> counts = new HashMap<>();
        for(int tid = 0; tid<trials.size(); tid++){
            String trialDesc = trials.get(tid).toString();
            if(!counts.containsKey(trialDesc)){
                counts.put(trialDesc, 0);
            }
            //increment trial type count
            counts.put(trialDesc, counts.get(trialDesc)+1); // in C#: counts[trialDesc]++, stupid java..
        }
        //form 40AB+/30AB-
        String str = "";
        boolean slashNeeded = false;
        for(String trialDesc : counts.keySet()){
            if(slashNeeded){
                str+= GuiStringConstants.TRAIL_TYPE_SEPARATOR;
            }
            str+=counts.get(trialDesc);
            str+=trialDesc;
            slashNeeded = true;
        }
        return str;
    }

    private enum Variable { ALPHA, VE, VI, VNET };
    private static double getValue(CsState state, Variable variable){
        switch (variable){
            case ALPHA: return state.Alpha;
            case VE: return state.Ve;
            case VI: return state.Vi;
            default: return state.Vnet;
        }
    }
}
