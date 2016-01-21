package Helpers;

import ViewModels.GroupReportViewModel;

import javax.swing.*;
import java.util.List;

/**
 * Created by Rokas on 05/11/2015.
 */
public class GuiHelper {

    public static void outputHistory(List<GroupReportViewModel> groupReports, JTextArea outputArea){
        clearOuputArea(outputArea);
        for(GroupReportViewModel reportVM : groupReports) {
            for (int row = 0; row < reportVM.getNumberOfRows(); row++) {
                for (int col = 0; col < reportVM.getColumnCount(); col++) {
                    outputArea.append(reportVM.getCell(row, col).toString());
                    outputArea.append("\t");
                }
                outputArea.append("\n");
            }
            outputArea.append("\n");
        }
    }

    public static void outputErrorMessage(String error, JTextArea outputArea){
        clearOuputArea(outputArea);
        outputArea.append(error);
    }

    public static void clearOuputArea(JTextArea outputArea){
        outputArea.setText(null);
    }
}
