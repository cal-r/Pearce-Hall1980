package Controllers;

import Helpers.GuiHelper;
import Constants.GuiStringConstants;
import Helpers.SimulatorBuilder;
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

    private GlobalPramsTableModel globalParamsTableModel;
    private CSParamsTableModel csParamsTableModel;
    private TrailTableModel trailTableModel;
    private JButton setParamsButton;
    private JButton runSimButton;
    private JTextArea simOutputArea;
    private JButton plusPhaseButton;
    private JButton minusPhaseButton;

    private Simulator simulator;

    public void initSetParamsButton(JButton button) {
        setParamsButton = button;
        initButton(setParamsButton, GuiStringConstants.SET_PARAMETERS);
    }

    public void initRunSimButton(JButton button){
        runSimButton = button;
        initButton(runSimButton, GuiStringConstants.RUN_SIMULATION);
        runSimButton.setEnabled(false);
    }

    public void initPlusPhaseButton(JButton button){
        plusPhaseButton = button;
        initButton(plusPhaseButton, GuiStringConstants.ADD_PHASE);
    }

    public void initMinusPhaseButton(JButton button){
        minusPhaseButton = button;
        initButton(minusPhaseButton, GuiStringConstants.REMOVE_PHASE);
    }

    private void initButton(JButton button, String txt){
        button.addActionListener(this);
        button.setText(txt);
        button.setActionCommand(txt);
    }

    public void initTrailTable(JTable table) {
        trailTableModel = new TrailTableModel();
        table.setModel(trailTableModel);
    }

    public void initCsParamsTable(JTable table) {
        csParamsTableModel = new CSParamsTableModel();
        table.setModel(csParamsTableModel);
    }

    public void initGlobalParamsTable(JTable table){
        globalParamsTableModel = new GlobalPramsTableModel();
        table.setModel(globalParamsTableModel);
    }

    public void initOutputArea(JTextArea simOutputArea){
        this.simOutputArea = simOutputArea;
    }

    private void onSetParams(){
//        simulator = SimulatorBuilder.build(ta)
//        simulator.initPhase(phaseDescription);
//        List<Parameter> csParameters = simulator.getCsParameters();
//        List<Parameter> globalParameters = simulator.getGlobalParameters();
//        GuiHelper.setUpParams(csParamsTable, csParameters);
//        GuiHelper.setUpParams(globalParamsTable, globalParameters);
//        runSimButton.setEnabled(true);
    }

    private void onRunSim(){
//        PhaseHistory history = simulator.runSimulation();
//        GuiHelper.outputHistory(history, simOutputArea);
    }

    private void onPhasePlus(){
        trailTableModel.addPhase();
    }

    private void onPhaseMinus() {
        trailTableModel.removePhase();
    }

    private void processEvent(String cmd){
        switch (cmd){
            case GuiStringConstants.SET_PARAMETERS: onSetParams();
                break;
            case GuiStringConstants.RUN_SIMULATION: onRunSim();
                break;
            case GuiStringConstants.ADD_PHASE: onPhasePlus();
                break;
            case GuiStringConstants.REMOVE_PHASE: onPhaseMinus();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        processEvent(e.getActionCommand());
    }
}
