package Helpers;

import Models.ConditionalStimulus;
import Models.Parameters.CsParameter;
import Models.Parameters.Parameter;
import Models.Phase;
import Models.PhaseHistory;
import ViewModels.BaseParamsTableModel;
import ViewModels.CSParamsTableModel;
import ViewModels.TrailTableModel;

import javax.swing.*;
import java.util.List;

/**
 * Created by Rokas on 05/11/2015.
 */
public class GuiHelper {

    public static void outputHistory(PhaseHistory history, JTextArea outputArea){
        outputArea.setText(null);
        for(ConditionalStimulus cs : history.csHistoriesMap.keySet()){
            outputArea.append(String.format("Cs: %s \n", cs.Name));
            for(PhaseHistory.CsState state : history.csHistoriesMap.get(cs)){
                outputArea.append(String.format("#trial: %1$d \t", state.TrailNumber));
                outputArea.append(String.format("Ve: %1$.5f \t", state.Ve));
                outputArea.append(String.format("Vi: %1$.5f \t", state.Vi));
                outputArea.append(String.format("Vnet: %1$.5f \t", state.Vnet));
                outputArea.append("\n");
            }
            outputArea.append("\n");
        }
    }
}
