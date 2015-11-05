package Controllers;

import Helpers.GuiHelper;
import Constants.GuiStringConstants;
import ViewModels.CSParamsTableModel;
import ViewModels.TrailTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Rokas on 03/11/2015.
 */
public class MainWindowController implements ActionListener {

    private JTable csParamsTable;
    private JTable trailTable;
    private JButton setParamsButton;

    public void initSetParamsButton(JButton button) {
        setParamsButton = button;
        setParamsButton.addActionListener(this);
        setParamsButton.setText(GuiStringConstants.SET_PARAMETERS);
    }

    public void initCsParamsTable(JTable table) {
        csParamsTable = table;
        csParamsTable.setModel(new CSParamsTableModel());
    }

    public void initTrailTable(JTable table) {
        trailTable = table;
        trailTable.setModel(new TrailTableModel());
    }

    private void onSetParams(){
        String phaseDescription = GuiHelper.GetPhaseDescription(trailTable);

    }

    private void processEvent(String cmd){
        switch (cmd){
            case GuiStringConstants.SET_PARAMETERS: onSetParams();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        processEvent(e.getActionCommand());
    }
}
