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

    private MainWindowController controller;

    public MainWindow() {
        super();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(rootPane);
        pack();
    }

    public void InnitSubElements() {
        controller = new MainWindowController();
        controller.initCsParamsTable(csParamsTable);
        controller.initTrailTable(trailTable);
        controller.initSetParamsButton(setParamsButton);
    }
}
