package Controllers;

import ViewModels.TrailTableModel;

import javax.swing.*;

/**
 * Created by Rokas on 03/11/2015.
 */
public class TrailTableController {

    private JTable table;

    public TrailTableController(JTable table) {
        this.table = table;
        table.setModel(new TrailTableModel());
    }
}
