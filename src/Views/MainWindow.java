package Views;

import Controllers.TrailTableController;

import javax.swing.*;

/**
 * Created by Rokas on 03/11/2015.
 */
public class MainWindow extends JFrame{

    private JPanel rootPane;
    private JTable trailTable;

    private TrailTableController trailTableController;

    public MainWindow() {
        super();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void InnitSubElements() {
        setContentPane(rootPane);
        InnitTrailTable();

        pack();
    }

    private void InnitTrailTable() {
        trailTableController = new TrailTableController(trailTable);

    }
}
