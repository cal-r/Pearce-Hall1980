package Controllers;

import Helpers.GuiHelper;
import Constants.GuiStringConstants;
import Models.ConditionalStimulus;
import Models.Parameters.CsParameter;
import Models.Parameters.Parameter;
import Models.Simulator;
import ViewModels.CSParamsTableModel;
import ViewModels.TrailTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class MainWindowController implements ActionListener {

    private JTable csParamsTable;
    private JTable trailTable;
    private JButton setParamsButton;

    private Simulator simulator;

    public MainWindowController(){
        simulator = new Simulator();
    }

    public void initSetParamsButton(JButton button) {
        setParamsButton = button;
        setParamsButton.addActionListener(this);
        setParamsButton.setText(GuiStringConstants.SET_PARAMETERS);
        setParamsButton.setActionCommand(GuiStringConstants.SET_PARAMETERS);
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
        List<Parameter> csParameters = simulator.GetCsParameters(phaseDescription);
        GuiHelper.SetUpCsParams(csParamsTable, csParameters);
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
