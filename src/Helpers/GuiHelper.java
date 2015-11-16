package Helpers;

import Models.ConditionalStimulus;
import Models.Group;
import Models.History.GroupHistory;
import Models.History.PhaseHistory;

import javax.swing.*;
import java.util.List;

/**
 * Created by Rokas on 05/11/2015.
 */
public class GuiHelper {

    public static void outputHistory(List<GroupHistory> history, JTextArea outputArea){
        outputArea.setText(null);
        for(GroupHistory groupHistory : history){
            outputArea.append(groupHistory.toString());
        }
    }
}
