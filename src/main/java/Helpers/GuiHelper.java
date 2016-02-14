package Helpers;

import Constants.GuiStringConstants;
import ViewModels.GroupReportViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
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

    public static void displayErrorMessage(String error){
        JOptionPane.showMessageDialog(
                new JFrame(),
                error,
                GuiStringConstants.ERROR,
                JOptionPane.ERROR_MESSAGE);
    }

    public static int getIntFromUser(String message, int defaultValue){
        try {
            return Integer.parseInt(JOptionPane.showInputDialog(message, defaultValue));
        }catch (Exception ex){
            return defaultValue;
        }
    }

    public static boolean isCheckBoxSelected(ActionEvent e){
        return ((JCheckBox)e.getSource()).isSelected();
    }

    public static boolean isMenuItemSelected(ActionEvent e){
        return ((JCheckBoxMenuItem)e.getSource()).isSelected();
    }

    public static void clearOuputArea(JTextArea outputArea){
        outputArea.setText(null);
    }
}
