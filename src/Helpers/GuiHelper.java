package Helpers;

import ViewModels.TrailTableModel;

import javax.swing.*;

/**
 * Created by Rokas on 05/11/2015.
 */
public class GuiHelper {

    public static String GetPhaseDescription(JTable trailTable){
        TrailTableModel trailTableModel = (TrailTableModel)trailTable.getModel();
        return trailTableModel.GetPhaseDescription();
    }

}
