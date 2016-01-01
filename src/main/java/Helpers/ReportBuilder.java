package Helpers;

import Constants.TableStringConstants;
import Models.History.GroupHistory;
import Models.History.PhaseHistory;
import Models.Phase;
import Models.Trail;
import ViewModels.ReportViewModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Rokas on 22/12/2015.
 */
public class ReportBuilder {
    public static ReportViewModel buildReport(List<GroupHistory> history){
        ReportViewModel report = new ReportViewModel();
        int rowId = 0;

        for(GroupHistory groupHistory : history){
            report.setCell(rowId, 0, groupHistory.group.Name);
            rowId++;
            for(PhaseHistory phaseHistory : groupHistory.phaseHistories) {
                insertPhaseDescription(report, rowId++, phaseHistory.phase);
                for(Variable variable : Variable.values())
                {
                    report.setCell(rowId++, 0, variable.toString());
                    createVariableTable(report, rowId, phaseHistory, variable);
                    rowId+=phaseHistory.getCues().size()+2;
                }
            }
        }
        return report;
    }

    private static void createVariableTable(ReportViewModel report, int rowId, PhaseHistory phaseHistory, Variable variable){
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
    }

    private static void insertPhaseDescription(ReportViewModel report, int rowId, Phase phase){
        int colId = 0;
        report.setCell(rowId, colId++, phase.toString()); //append name ("Phase 69")
        report.setCell(rowId, colId++, constructTrailsString(phase.trails));
        if(phase.isRandom()){
            report.setCell(rowId, colId++, TableStringConstants.RANDOM);
        }
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
