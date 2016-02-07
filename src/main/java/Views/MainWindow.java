package Views;

import Constants.ActionCommands;
import Controllers.MainWindowController;
import Controllers.MenuController;
import sun.misc.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

/**
 * Created by Rokas on 03/11/2015.
 */
public class MainWindow extends JFrame{

    private JPanel rootPane;
    private JTable trialTable;
    private JButton setParamsButton;
    private JTable csParamsTable;
    private JTable globalParamsTable;
    private JButton runButton;
    private JTextArea simOutputArea;
    private JButton plusPhaseButton;
    private JButton minusPhaseButton;
    private JButton plusGroupButton;
    private JButton minusGroupButton;
    private JButton xlsExportButton;
    private JButton graphsButton;
    private JPanel footerPanel;

    private MainWindowController controller;
    private MenuController menuController;

    public MainWindow(MainWindowController controller, MenuController menuController) {
        super();
        this.controller = controller;
        this.menuController = menuController;
        setContentPane(rootPane);
        setJMenuBar(menuController.getBar());
        footerPanel.add(new JLabel(new ImageIcon(Launcher.class.getResource("/images/Foot.png"))));

        pack();
        initSubElements();
    }

    private void initSubElements() {
        //tables
        controller.initTrialTable(trialTable);
        controller.initCsParamsTable(csParamsTable);
        controller.initGlobalParamsTable(globalParamsTable);
        //buttons
        controller.initButton(setParamsButton, ActionCommands.SET_PARAMETERS);
        controller.initButton(plusPhaseButton, ActionCommands.ADD_PHASE);
        controller.initButton(minusPhaseButton, ActionCommands.REMOVE_PHASE);
        controller.initButton(plusGroupButton, ActionCommands.ADD_GROUP);
        controller.initButton(minusGroupButton, ActionCommands.REMOVE_GROUP);
        //simulation button
        controller.initDisabledButton(runButton, ActionCommands.RUN_SIMULATION);
        controller.runSimButton = runButton;

        //export buttons
        controller.initExportButton(xlsExportButton, ActionCommands.XLS_EXPORT);
        controller.initExportButton(graphsButton, ActionCommands.GRAPHS_DISPLAY);

        controller.initOutputArea(simOutputArea);
    }
}
