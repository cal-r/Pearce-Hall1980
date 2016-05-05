package Helpers.GUI;

import Constants.GuiStringConstants;
import Helpers.ResourceHelper;
import ViewModels.GroupReportViewModel;

import javax.swing.*;
import java.awt.*;
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
                JOptionPane.ERROR_MESSAGE,
                ResourceHelper.getForbiddenImageIcon());
    }

    public static int getIntFromUser(String message, int defaultValue){

        JOptionPane pane = new JOptionPane();
        pane.setIcon(ResourceHelper.getRandomImageIcon());

        try {
            return Integer.parseInt(
                    (String) JOptionPane.showInputDialog(
                                    new JFrame(),
                                    message,
                                    "",
                                    JOptionPane.QUESTION_MESSAGE,
                                    ResourceHelper.getRandomImageIcon(),
                                    null,
                                    defaultValue));
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

    public static void centerFrame(JFrame frame){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height/2);
    }
}
