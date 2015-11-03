package Views;

import Controllers.CSParamsTableController;
import Controllers.SetParamsButtonController;
import Controllers.TrailTableController;

import javax.swing.*;

/**
 * Created by Rokas on 03/11/2015.
 */
public class MainWindow extends JFrame{

    private JPanel rootPane;
    private JTable trailTable;
    private JButton setParamsButton;
    private JTable csParamsTable;

    private TrailTableController trailTableController;
    private CSParamsTableController csParamsTableController;
    private SetParamsButtonController setParamsButtonController;

    public MainWindow() {
        super();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void InnitSubElements() {
        InnitTables();
        InnitButtons();
        setContentPane(rootPane);
        pack();
    }

    private void InnitTables() {
        trailTableController = new TrailTableController(trailTable);
        csParamsTableController = new CSParamsTableController(csParamsTable);
    }

    private void InnitButtons() {
        setParamsButtonController = new SetParamsButtonController(setParamsButton);
    }
}
