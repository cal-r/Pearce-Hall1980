package Helpers;

import Models.ConditionalStimulus;
import Models.History.GroupHistory;
import Models.History.PhaseHistory;
import ViewModels.ReportViewModel;

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
                report.setCell(rowId, 0, phaseHistory.phase.toString());
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
}
