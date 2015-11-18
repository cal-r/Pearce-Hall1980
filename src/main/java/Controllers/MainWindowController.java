package Controllers;

import Constants.GuiStringConstants;
import Helpers.GuiHelper;
import Helpers.SimulatorBuilder;
import Models.History.GroupHistory;
import Models.Parameters.CsParameter;
import Models.Parameters.Parameter;
import Models.Simulator;
import ViewModels.CSParamsTableModel;
import ViewModels.GlobalPramsTableModel;
import ViewModels.TrailTableModel;
import org.jxls.template.SimpleExporter;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rokas on 03/11/2015.
 */
public class MainWindowController implements ActionListener, TableModelListener {

    private GlobalPramsTableModel globalParamsTableModel;
    private CSParamsTableModel csParamsTableModel;
    private TrailTableModel trailTableModel;
    private JButton runSimButton;
    private JTextArea simOutputArea;

    private Simulator simulator;

    public void initRunSimButton(JButton button, String command){
        runSimButton = button;
        initButton(runSimButton, command);
        runSimButton.setEnabled(false);
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
try {
    try (OutputStream os1 = new FileOutputStream("target/simple_export_output1.xls")) {
        List<String> employees = Arrays.asList("Name", "Birthday", "Payment");
        List<String> headers = Arrays.asList("Name", "Birthday", "Payment");
        SimpleExporter exporter = new SimpleExporter();
        exporter.gridExport(headers, employees, "name, birthDate, payment", os1);
    }
}catch(Exception ex){

}
    }

    private void onRunSim(){
        List<GroupHistory> history = simulator.runSimulation();
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
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        processEvent(e.getActionCommand());
    }

    @Override
    public void tableChanged(TableModelEvent e) {processEvent(GuiStringConstants.TRAIL_TABLE_CHANGED); }
}
