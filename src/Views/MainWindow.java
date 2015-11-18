package Views;

import Constants.GuiStringConstants;
import Controllers.MainWindowController;

import javax.swing.*;

/**
 * Created by Rokas on 03/11/2015.
 */
public class MainWindow extends JFrame{

    private JPanel rootPane;
    private JTable trailTable;
    private JButton setParamsButton;
    private JTable csParamsTable;
    private JTable globalParamsTable;
    private JButton runButton;
    private JTextArea simOutputArea;
    private JButton plusPhaseButton;
    private JButton minusPhaseButton;
    private JButton plusGroupButton;
    private JButton minusGroupButton;

    private MainWindowController controller;

    public MainWindow() {
        super();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(rootPane);
        pack();
    }

    public void initSubElements() {
        controller = new MainWindowController();
        controller.initTrailTable(trailTable);
        controller.initCsParamsTable(csParamsTable);
        controller.initGlobalParamsTable(globalParamsTable);
        controller.initButton(setParamsButton, GuiStringConstants.SET_PARAMETERS);
        controller.initButton(plusPhaseButton, GuiStringConstants.ADD_PHASE);
        controller.initButton(minusPhaseButton, GuiStringConstants.REMOVE_PHASE);
        controller.initButton(plusGroupButton, GuiStringConstants.ADD_GROUP);
        controller.initButton(minusGroupButton, GuiStringConstants.REMOVE_GROUP);
        controller.initRunSimButton(runButton, GuiStringConstants.RUN_SIMULATION);
        controller.initOutputArea(simOutputArea);
    }   
}
