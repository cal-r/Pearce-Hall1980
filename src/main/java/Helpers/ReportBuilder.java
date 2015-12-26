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
                insertPhaseDescription(report, rowId, phaseHistory.phase);
                rowId++;
                for(char csname : phaseHistory.csHistoriesMap.keySet()) {
                    report.setCell(rowId, 0, String.format("Cs: %s", csname));
                    rowId++;
                    for (PhaseHistory.CsState state : phaseHistory.csHistoriesMap.get(csname)) {
                        int colId = 0;
                        report.setCell(rowId, colId++, String.format("#trial: %1$d", state.TrailNumber));
                        report.setCell(rowId, colId++, "\t");
                        report.setCell(rowId, colId++, state.TrailDescription);
                        report.setCell(rowId, colId++, "\t");
                        report.setCell(rowId, colId++, "Ve: ");
                        report.setCell(rowId, colId++, String.format("%1$.5f", state.Ve));
                        report.setCell(rowId, colId++, "\t");
                        report.setCell(rowId, colId++, "Vi: ");
                        report.setCell(rowId, colId++, String.format("%1$.5f", state.Vi));
                        report.setCell(rowId, colId++, "\t");
                        report.setCell(rowId, colId++, "Vnet: ");
                        report.setCell(rowId, colId++, String.format("%1$.5f", state.Vnet));
                        report.setCell(rowId, colId++, "\t");
                        rowId++;
                    }
                    rowId++;
                }
            }
        }
        return report;
    }

    private static void insertPhaseDescription(ReportViewModel report, int rowId, Phase phase){
        int colId = 0;
        report.setCell(rowId, colId++, phase.toString()); //append name ("Phase 69")
        report.setCell(rowId, colId++, "\t");
        report.setCell(rowId, colId++, constructTrailsString(phase.trails));
        report.setCell(rowId, colId++, "\t");
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
}
