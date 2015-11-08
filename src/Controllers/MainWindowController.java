package Controllers;

import Helpers.GuiHelper;
import Constants.GuiStringConstants;
import Models.Parameters.Parameter;
import Models.Simulator;
import ViewModels.CSParamsTableModel;
import ViewModels.GlobalPramsTableModel;
import ViewModels.TrailTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class MainWindowController implements ActionListener {

    private JTable globalParamsTable;
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

    public void initTrailTable(JTable table) {
        trailTable = table;
        trailTable.setModel(new TrailTableModel());
    }

    public void initCsParamsTable(JTable table) {
        csParamsTable = table;
        csParamsTable.setModel(new CSParamsTableModel());
    }

    public void innitGlobalParamsTable(JTable table){
        globalParamsTable = table;
        globalParamsTable.setModel(new GlobalPramsTableModel());
    }


    private void onSetParams(){
        String phaseDescription = GuiHelper.GetPhaseDescription(trailTable);
        simulator.innitPhase(phaseDescription);
        List<Parameter> csParameters = simulator.getCsParameters();
        List<Parameter> globalParameters = simulator.getGlobalParameters();
        GuiHelper.SetUpParams(csParamsTable, csParameters);
        GuiHelper.SetUpParams(globalParamsTable, globalParameters);

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
