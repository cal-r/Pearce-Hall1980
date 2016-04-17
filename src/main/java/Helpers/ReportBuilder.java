package Helpers;

import Constants.GuiStringConstants;
import Models.History.*;
import Models.Parameters.ConditionalStimulus.CsParameter;
import Models.Parameters.Parameter;
import Models.Parameters.Pools.CsPools.ICsParameterPool;
import Models.Parameters.UnconditionalStimulus.UsParameter;
import Models.SimulatorSettings;
import Models.Stimulus.ConditionalStimulus;
import ViewModels.GroupReportViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Rokas on 22/12/2015.
 */
public class ReportBuilder {

    public static List<GroupReportViewModel> buildReport(SimulationHistory history, SimulatorSettings settings){
        List<GroupReportViewModel> groupReports = new ArrayList<>();
        for(GroupHistory groupHistory : history){
            groupReports.add(buildGroupReport(groupHistory, settings));
        }
        return groupReports;
    }

    private static GroupReportViewModel buildGroupReport(GroupHistory groupHistory, SimulatorSettings settings){
        GroupReportViewModel report = new GroupReportViewModel(groupHistory.group.Name);
        int rowId = 0;
        report.setCell(rowId++, 0, "P&H parameters:");

        rowId = insertCsParameterTable(report, rowId, groupHistory) +1;

        rowId = insertGlobalParameterTable(report, rowId, groupHistory.globalParameters) + 1;

        rowId = insertUsParameterTable(report, rowId, groupHistory.usParameters) + 2;

        report.setCell(rowId++, 0, groupHistory.group.Name);
        for(int gpId = 0; gpId < groupHistory.getNumberOfPhases(); gpId++) {
            GroupPhaseHistory groupPhaseHistory = groupHistory.getGroupPhaseHistory(gpId);
            insertPhaseDescription(report, rowId++, groupPhaseHistory);
            rowId++;
            for(Variable variable : Variable.values())
            {
                report.setCell(rowId++, 0, getVariableDisplayName(variable, settings));
                int lastRowId = insertVariableTable(report, rowId, groupPhaseHistory, variable);
                rowId = lastRowId+1;
            }
        }
        return report;
    }

    private static int insertUsParameterTable(GroupReportViewModel report, int rowId, List<UsParameter> usParameters) {
        report.setCell(rowId, 0, GuiStringConstants.US_PARAMETER);
        if(usParameters.size()==1 && usParameters.get(0).getPhaseCount() == 0){
            report.setCell(rowId, 1, GuiStringConstants.VALUE);
            rowId++;
            report.setCell(rowId, 0, usParameters.get(0).getDisplayName());
            report.setCell(rowId, 1, usParameters.get(0).getValue(0));
            rowId++;
            return rowId;
        }

        for (int pid = 0; pid < usParameters.get(0).getPhaseCount(); pid++) {
            report.setCell(rowId, pid + 1, GuiStringConstants.getPhaseTitle(pid));
        }
        rowId++;
        for (UsParameter usParam : usParameters) {
            report.setCell(rowId, 0, usParam.getDisplayName());
            for (int pid = 0; pid < usParam.getPhaseCount(); pid++) {
                if (usParam.isAvailable(pid)) {
                    report.setCell(rowId, pid + 1, usParam.getValue(pid));
                }
            }
            rowId++;
        }
        return rowId;
    }

    private static int insertCsParameterTable(GroupReportViewModel report, int rowId, GroupHistory groupHistory) {
        report.setCell(rowId, 0, GuiStringConstants.CS_PARAMETER);
        report.setCell(rowId, 1, GuiStringConstants.VALUE);
        rowId++;
        rowId = insertCsParameterTable(report, rowId, groupHistory.csParameterPool);
        return rowId;
    }

    private static int insertCsParameterTable(GroupReportViewModel report, int rowId, ICsParameterPool pool){
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
        Collection<String> orderedStimNames = StimulusOrderingHelper.orderStimNamesByDescription(groupPhaseHistory.getStimsNames(), groupPhaseHistory.getDescription());
        List<String> tableStimNames = new ArrayList<>();
        List<String> alphaLinks = new ArrayList<>();
        for(String stimName : orderedStimNames) {
            if(hasValue(groupPhaseHistory, stimName, variable)) {
                if(isMultiAlphaCase(groupPhaseHistory, stimName, variable)){
                    String alphaLink = getAlphaLink(groupPhaseHistory, stimName);
                    if(!alphaLinks.contains(alphaLink)){
                        report.setCell(currRowId++, 0, alphaLink);
                        alphaLinks.add(alphaLink);
                        tableStimNames.add(stimName);
                    }
                }else {
                    report.setCell(currRowId++, 0, stimName);
                    tableStimNames.add(stimName);
                }
            }
        }

        int maxRow = currRowId;
        currRowId = rowId;
        int colId = 1;

        for(int periodNo=1;periodNo<= groupPhaseHistory.getNumberOfPeriods();periodNo++) {
            report.setCell(currRowId++, colId, String.format("trial %1$d", periodNo));
            for(String stimName : tableStimNames) {
                StimulusState stimState = groupPhaseHistory.getState(stimName, periodNo);
                if(stimState == null) {
                    currRowId++;
                }else{
                    report.setCell(currRowId++, colId, getValue(stimState, variable));
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

    public enum Variable { ALPHA, VE, VI, VNET };
    private static double getValue(StimulusState state, Variable variable){
        switch (variable){
            case ALPHA: return ((ConditionalStimulusState)state).Alpha;
            case VE: return ((ConditionalStimulusState)state).Ve;
            case VI: return ((ConditionalStimulusState)state).Vi;
            default: return state.Vnet;
        }
    }

    private static boolean isMultiAlphaCase(GroupPhaseHistory groupPhaseHistory, String stimName, Variable variable) {
        StimulusState stimState = groupPhaseHistory.getLastState(stimName);
        return variable == Variable.ALPHA
                && stimState instanceof ConditionalStimulusState
                && ((ConditionalStimulusState) stimState).AlphaLink != null;
    }

    private static String getAlphaLink(GroupPhaseHistory groupPhaseHistory, String stimName){
        return ((ConditionalStimulusState)groupPhaseHistory.getLastState(stimName)).AlphaLink;
    }

    private static boolean hasValue(StimulusState state, Variable variable){
        return variable == Variable.VNET || state instanceof ConditionalStimulusState;
    }

    private static boolean hasValue(GroupPhaseHistory gpHist, String stimName, Variable variable){
        StimulusState firstState = gpHist.getState(stimName, 1);
        return hasValue(firstState, variable);
    }

    public static String getVariableDisplayName(Variable variable, SimulatorSettings settings){
        if(variable == Variable.VI && settings.isRodriguezMode())
            return "VnE";
        return variable.toString();
    }
}
