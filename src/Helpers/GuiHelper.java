package Helpers;

import Models.Parameters.CsParameter;
import Models.Parameters.Parameter;
import ViewModels.CSParamsTableModel;
import ViewModels.TrailTableModel;

import javax.swing.*;
import java.util.List;

/**
 * Created by Rokas on 05/11/2015.
 */
public class GuiHelper {

    public static String GetPhaseDescription(JTable trailTable){
        TrailTableModel tableModel = (TrailTableModel)trailTable.getModel();
        return tableModel.GetPhaseDescription();
    }

    public static void SetUpCsParams(JTable csParamsTable, List<Parameter> params){
        CSParamsTableModel tableModel = (CSParamsTableModel) csParamsTable.getModel();
        tableModel.setUpParameters(params);
    }

}
