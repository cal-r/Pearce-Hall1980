package Controllers;

import ViewModels.*;

import javax.swing.*;

/**
 * Created by Rokas on 03/11/2015.
 */
public class CSParamsTableController {
    private JTable table;

    public CSParamsTableController(JTable table) {
        this.table = table;
        table.setModel(new CSParamsTableModel());
    }
}
