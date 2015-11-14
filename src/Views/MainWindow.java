package Views;

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
        controller.initSetParamsButton(setParamsButton);
        controller.initPlusPhaseButton(plusPhaseButton);
        controller.initMinusPhaseButton(minusPhaseButton);
        controller.initRunSimButton(runButton);
        controller.initOutputArea(simOutputArea);
    }   
}
