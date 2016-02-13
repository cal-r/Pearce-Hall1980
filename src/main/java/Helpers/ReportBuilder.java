package Helpers;

import Constants.GuiStringConstants;
import Models.ConditionalStimulus;
import Models.History.*;
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
            insertPhaseDescription(report, rowId++, groupPhaseHistory);
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
        for(String stimName : groupPhaseHistory.getStimsNames()) {
            report.setCell(currRowId++, 0, stimName);
        }
        currRowId = rowId;
        int colId = 1;
        int maxRow = currRowId;

        for(int trialNo=1;trialNo<= groupPhaseHistory.getNumberOfTrials();trialNo++) {
            report.setCell(currRowId++, colId, String.format("trial %1$d", trialNo));
            for(String stimName : groupPhaseHistory.getStimsNames()) {
                StimulusState stimState = groupPhaseHistory.getState(stimName, trialNo);
                if(hasValue(stimState, variable)){
                    report.setCell(currRowId++, colId, getValue(stimState, variable));
                    maxRow = Math.max(maxRow, currRowId);
                }
            }
            currRowId = rowId;
            colId++;
        }

        return maxRow + 1;
    }

    private static void insertPhaseDescription(GroupReportViewModel report, int rowId, GroupPhaseHistory groupPhaseHistory){
        int colId = 0;
        report.setCell(rowId, colId++, groupPhaseHistory.getPhaseName()); //append name ("Phase 69")
        report.setCell(rowId, colId++, groupPhaseHistory.getDescription());
        colId++;
        report.setCell(rowId, colId++, GuiStringConstants.RANDOM + ": " + groupPhaseHistory.isRandom());
    }

    private enum Variable { ALPHA, VE, VI, VNET };
    private static double getValue(StimulusState state, Variable variable){
        switch (variable){
            case ALPHA: return ((ConditionalStimulusState)state).Alpha;
            case VE: return ((ConditionalStimulusState)state).Ve;
            case VI: return ((ConditionalStimulusState)state).Vi;
            default: return ((ConditionalStimulusState)state).Vnet;
        }
    }

    private static boolean hasValue(StimulusState state, Variable variable){
        return variable == Variable.VNET || state instanceof ConditionalStimulusState;
    }
}
