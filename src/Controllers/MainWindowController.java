package Controllers;

import Helpers.GuiHelper;
import Constants.GuiStringConstants;
import Models.Parameters.Parameter;
import Models.PhaseHistory;
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
    private JButton runSimButton;
    private JTextArea simOutputArea;

    private Simulator simulator;

    public MainWindowController(){
        simulator = new Simulator();
    }

    public void initSetParamsButton(JButton button) {
        setParamsButton = button;
        initButton(setParamsButton, GuiStringConstants.SET_PARAMETERS);
    }

    public void initRunSimButton(JButton button){
        runSimButton = button;
        initButton(runSimButton, GuiStringConstants.RUN_SIMULATION);
        runSimButton.setEnabled(false);
    }

    private void initButton(JButton button, String txt){
        button.addActionListener(this);
        button.setText(txt);
        button.setActionCommand(txt);
    }

    public void initTrailTable(JTable table) {
        trailTable = table;
        trailTable.setModel(new TrailTableModel());
    }

    public void initCsParamsTable(JTable table) {
        csParamsTable = table;
        csParamsTable.setModel(new CSParamsTableModel());
    }

    public void initGlobalParamsTable(JTable table){
        globalParamsTable = table;
        globalParamsTable.setModel(new GlobalPramsTableModel());
    }

    public void initOutputArea(JTextArea simOutputArea){
        this.simOutputArea = simOutputArea;
    }

    private void onSetParams(){
        String phaseDescription = GuiHelper.getPhaseDescription(trailTable);
        simulator.initPhase(phaseDescription);
        List<Parameter> csParameters = simulator.getCsParameters();
        List<Parameter> globalParameters = simulator.getGlobalParameters();
        GuiHelper.setUpParams(csParamsTable, csParameters);
        GuiHelper.setUpParams(globalParamsTable, globalParameters);
        runSimButton.setEnabled(true);
    }

    private void onRunSim(){
        PhaseHistory history = simulator.simulatePhase();
        GuiHelper.outputHistory(history, simOutputArea);
    }

    private void processEvent(String cmd){
        switch (cmd){
            case GuiStringConstants.SET_PARAMETERS: onSetParams();
                break;
            case GuiStringConstants.RUN_SIMULATION: onRunSim();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        processEvent(e.getActionCommand());
    }
}
