package Controllers;

import Constants.ActionCommands;
import Constants.GuiStringConstants;
import Helpers.*;
import Helpers.ModelBuilding.SimulatorBuilder;
import Models.Parameters.Parameter;
import Models.Simulator;
import ViewModels.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class MainWindowController implements ActionListener, TableModelListener {

    public JButton runSimButton;
    private List<JButton> exportButtons;

    private GlobalPramsTableModel globalParamsTableModel;
    private CSParamsTableModel csParamsTableModel;
    private TrialTableModel trialTableModel;
    private JTextArea simOutputArea;

    private Simulator simulator;

    public MainWindowController(Simulator simulator){
        this.simulator = simulator;
        exportButtons = new ArrayList<>();
    }

    public void initExportButton(JButton button, String command){
        exportButtons.add(button);
        initDisabledButton(button, command);
    }

    public void initDisabledButton(JButton button, String command){
        initButton(button, command);
        button.setEnabled(false);
    }

    public void initButton(JButton button, String command){
        button.addActionListener(this);
        button.setActionCommand(command);
    }

    public void initTrialTable(JTable table) {
        trialTableModel = new TrialTableModel();
        initTableModel(table, trialTableModel);
    }

    public void initCsParamsTable(JTable table) {
        csParamsTableModel = new CSParamsTableModel();
        initTableModel(table, csParamsTableModel);
    }

    public void initGlobalParamsTable(JTable table){
        globalParamsTableModel = new GlobalPramsTableModel();
        initTableModel(table, globalParamsTableModel);
    }

    private void initTableModel(JTable table, BaseTableModel tableModel){
        tableModel.addTableModelListener(this);
        table.setModel(tableModel);
    }

    public void initOutputArea(JTextArea simOutputArea){
        this.simOutputArea = simOutputArea;
    }

    private void onSetParams(){
        try {
            SimulatorBuilder.initSimulator(trialTableModel, simulator);
            List<Parameter> csParameters = ListCaster.cast(simulator.getCsParameters());
            List<Parameter> globalParameters = simulator.getGlobalParameters();
            csParamsTableModel.setUpParameters(csParameters);
            globalParamsTableModel.setUpParameters(globalParameters);
            runSimButton.setEnabled(true);
        }catch (IllegalArgumentException ex){
            GuiHelper.displayErrorMessage(GuiStringConstants.TRAIL_TABLE_ERROR);
        }
    }

    private void onExcelExport(){
        ExcelExportHelper.exportSimulation(new FilePickerController(), simulator.getLatestReport());
    }

    private void onRunSim(){
        simulator.runSimulation();
        GuiHelper.outputHistory(simulator.getLatestReport(), simOutputArea);
        enableExportButtons(true);
    }

    private void enableExportButtons(boolean enable){
        for(JButton btn : exportButtons){
            btn.setEnabled(enable);
        }
    }

    private void onPhasePlus(){
        trialTableModel.addPhase();
    }

    private void onPhaseMinus() {
        trialTableModel.removePhase();
    }

    private void onGroupPlus(){
        trialTableModel.addGroup();
    }

    private void onGroupMinus(){
        trialTableModel.removeGroup();
    }

    private void onTrialTableChanged(){
        runSimButton.setEnabled(false);
        onSelectionChanged();
    }

    private void onParamsTableChanged(){
        onSelectionChanged();
    }

    private void onSelectionChanged(){
        GuiHelper.clearOuputArea(simOutputArea);
        enableExportButtons(false);
    }

    private void onShowGraphs(){
        GraphWindowController.showGraphs(simulator.getLatestSimHistory());
    }

    private void processEvent(String cmd){
        switch (cmd){
            case ActionCommands.SET_PARAMETERS: onSetParams();
                break;
            case ActionCommands.RUN_SIMULATION: onRunSim();
                break;
            case ActionCommands.ADD_PHASE: onPhasePlus();
                break;
            case ActionCommands.REMOVE_PHASE: onPhaseMinus();
                break;
            case ActionCommands.TRAIL_TABLE_CHANGED: onTrialTableChanged();
                break;
            case ActionCommands.CS_PARAMS_TABLE_CHANGED: onParamsTableChanged();
                break;
            case ActionCommands.GLOBAL_PARAMS_TABLE_CHANGED: onParamsTableChanged();
                break;
            case ActionCommands.ADD_GROUP: onGroupPlus();
                break;
            case ActionCommands.REMOVE_GROUP: onGroupMinus();
                break;
            case ActionCommands.XLS_EXPORT: onExcelExport();
                break;
            case ActionCommands.GRAPHS_DISPLAY : onShowGraphs();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        processEvent(e.getActionCommand());
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        Class tableClass = e.getSource().getClass();
        if(tableClass == TrialTableModel.class) {
            processEvent(ActionCommands.TRAIL_TABLE_CHANGED);
        }
        if(tableClass == CSParamsTableModel.class) {
            processEvent(ActionCommands.CS_PARAMS_TABLE_CHANGED);
        }
        if(tableClass == GlobalPramsTableModel.class){
            processEvent(ActionCommands.GLOBAL_PARAMS_TABLE_CHANGED);
        }
    }
}
