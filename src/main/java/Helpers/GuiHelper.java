package Helpers;

import Models.History.GroupHistory;
import ViewModels.ReportViewModel;

import javax.swing.*;
import java.util.List;

/**
 * Created by Rokas on 05/11/2015.
 */
public class GuiHelper {

    public static void outputHistory(ReportViewModel reportVM, JTextArea outputArea){
        outputArea.setText(null);
        for(int row=0;row<reportVM.getNumberOfRows();row++){
            for(int col=0;col<reportVM.getColumnCount();col++){
                outputArea.append(reportVM.getCell(row, col));
            }
            outputArea.append("\n");
        }
    }
}
