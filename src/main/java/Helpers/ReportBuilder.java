package Helpers;

import Constants.TableStringConstants;
import Models.History.GroupHistory;
import Models.History.PhaseHistory;
import Models.Parameters.CsParameter;
import Models.Parameters.CsParameterPool;
import Models.Parameters.Parameter;
import Models.Phase;
import Models.Trail;
import ViewModels.GroupReportViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 22/12/2015.
 */
public class ReportBuilder {

    public static List<GroupReportViewModel> buildReport(List<GroupHistory> history){
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
        for(PhaseHistory phaseHistory : groupHistory.phaseHistories) {
            insertPhaseDescription(report, rowId++, phaseHistory.phase);
            rowId++;
            for(Variable variable : Variable.values())
            {
                report.setCell(rowId++, 0, variable.toString());
                int lastRowId = insertVariableTable(report, rowId, phaseHistory, variable);
                rowId = lastRowId+1;
            }
        }
        return report;
    }

    private static int insertCsParameterTable(GroupReportViewModel report, int rowId, CsParameterPool pool){
        report.setCell(rowId, 0, TableStringConstants.CS_PARAMETER);
        report.setCell(rowId, 1, TableStringConstants.VALUE);
        rowId++;
        for(CsParameter csParameter : pool.getAllParameters()){
            report.setCell(rowId, 0, csParameter.getDisplayName());
            report.setCell(rowId, 1, csParameter.getValue());
            rowId++;
        }
        return rowId;
    }

    private static int insertGlobalParameterTable(GroupReportViewModel report, int rowId, List<Parameter> params){
        report.setCell(rowId, 0, TableStringConstants.GLOBAL_PARAMETER);
        report.setCell(rowId, 1, TableStringConstants.VALUE);
        rowId++;
        for(Parameter parameter : params){
            report.setCell(rowId, 0, parameter.getDisplayName());
            report.setCell(rowId, 1, parameter.getValue());
            rowId++;
        }
        return rowId;
    }

    private static int insertVariableTable(GroupReportViewModel report, int rowId, PhaseHistory phaseHistory, Variable variable){
        int currRowId = rowId;

        currRowId++;
        for(char csname : phaseHistory.getCues()) {
            report.setCell(currRowId++, 0, csname);
        }
        currRowId = rowId;
        int colId = 1;

        for(int trailNo=1;trailNo<=phaseHistory.phase.getNumberOfTrails();trailNo++) {
            report.setCell(currRowId++, colId, String.format("trial %1$d", trailNo));
            for(char csname : phaseHistory.getCues()) {
                PhaseHistory.CsState state = phaseHistory.getState(csname, trailNo);
                if(csname == (Character)report.getCell(currRowId, 0) && trailNo == state.TrailNumber) { //sanity check
                    report.setCell(currRowId++, colId, getValue(state, variable));
                }
            }
            currRowId = rowId;
            colId++;
        }
        return rowId + phaseHistory.getCues().size()+1;
    }

    private static void insertPhaseDescription(GroupReportViewModel report, int rowId, Phase phase){
        int colId = 0;
        report.setCell(rowId, colId++, phase.toString()); //append name ("Phase 69")
        report.setCell(rowId, colId++, constructTrailsString(phase.trails));
        colId++;
        report.setCell(rowId, colId++, TableStringConstants.RANDOM + ": "+ phase.isRandom());
    }

    private static String constructTrailsString(List<Trail> trails){
        HashMap<String, Integer> counts = new HashMap<>();
        for(int tid = 0; tid<trails.size(); tid++){
            String trailDesc = trails.get(tid).toString();
            if(!counts.containsKey(trailDesc)){
                counts.put(trailDesc, 0);
            }
            //increment trail type count
            counts.put(trailDesc, counts.get(trailDesc)+1); // in C#: counts[trailDesc]++, stupid java..
        }
        //form 40AB+/30AB-
        String str = "";
        boolean slashNeeded = false;
        for(String trailDesc : counts.keySet()){
            if(slashNeeded){
                str+= TableStringConstants.TRAIL_TYPE_SEPARATOR;
            }
            str+=counts.get(trailDesc);
            str+=trailDesc;
            slashNeeded = true;
        }
        return str;
    }

    private enum Variable { ALPHA, VE, VI, VNET };
    private static double getValue(PhaseHistory.CsState state, Variable variable){
        switch (variable){
            case ALPHA: return state.Alpha;
            case VE: return state.Ve;
            case VI: return state.Vi;
            default: return state.Vnet;
        }
    }
}
