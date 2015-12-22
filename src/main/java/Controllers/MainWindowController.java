package Controllers;

import Constants.GuiStringConstants;
import Helpers.ExcelExportHelper;
import Helpers.GuiHelper;
import Helpers.SimulatorBuilder;
import Models.History.GroupHistory;
import Models.Parameters.CsParameter;
import Models.Parameters.Parameter;
import Models.Simulator;
import ViewModels.CSParamsTableModel;
import ViewModels.GlobalPramsTableModel;
import ViewModels.ReportViewModel;
import ViewModels.TrailTableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class MainWindowController implements ActionListener, TableModelListener {

    public JButton runSimButton;
    public JButton xlsExportButton;

    private FilePickerController filePickerController;
    private GlobalPramsTableModel globalParamsTableModel;
    private CSParamsTableModel csParamsTableModel;
    private TrailTableModel trailTableModel;
    private JTextArea simOutputArea;

    private Simulator simulator;

    public MainWindowController(JFrame mainFrame){
        filePickerController = new FilePickerController(mainFrame);
    }

    public void initDisabledButton(JButton button, String command){
        initButton(button, command);
        button.setEnabled(false);
    }

    public void initButton(JButton button, String command){
        button.addActionListener(this);
        button.setActionCommand(command);
    }

    public void initTrailTable(JTable table) {
        trailTableModel = new TrailTableModel();
        trailTableModel.addTableModelListener(this);
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
        simulator = SimulatorBuilder.build(trailTableModel);
        List<CsParameter> csParameters = simulator.getCsParameters();
        List<Parameter> globalParameters = simulator.getGlobalParameters();
        csParamsTableModel.setUpParameters((List<Parameter>)(List<?>)csParameters);
        globalParamsTableModel.setUpParameters(globalParameters);
        runSimButton.setEnabled(true);
        xlsExportButton.setEnabled(true);
    }

    private void onExcelExport(){
        ExcelExportHelper.exportSimulation(filePickerController, simulator.runSimulation());
    }

    private void onRunSim(){
        ReportViewModel history = simulator.runSimulation();
        GuiHelper.outputHistory(history, simOutputArea);
    }

    private void onPhasePlus(){
        trailTableModel.addPhase();
    }

    private void onPhaseMinus() {
        trailTableModel.removePhase();
    }

    private void onGroupPlus(){
        trailTableModel.addGroup();
    }

    private void onGroupMinus(){
        trailTableModel.removeGroup();
    }

    private void onTrailTableChanged(){
        runSimButton.setEnabled(false);
        xlsExportButton.setEnabled(false);
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
            case GuiStringConstants.TRAIL_TABLE_CHANGED: onTrailTableChanged();
                break;
            case GuiStringConstants.ADD_GROUP: onGroupPlus();
                break;
            case GuiStringConstants.REMOVE_GROUP: onGroupMinus();
                break;
            case GuiStringConstants.XLS_EXPORT: onExcelExport();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        processEvent(e.getActionCommand());
    }

    @Override
    public void tableChanged(TableModelEvent e) {processEvent(GuiStringConstants.TRAIL_TABLE_CHANGED); }
}
