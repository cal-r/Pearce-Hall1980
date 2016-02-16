package _from_RW_simulator;

/**
 * The Centre for Computational and Animal Learning Research (CAL-R)
 * @title Rescorla & Wagner Model Simulator
 * @author Esther Mondragón, Eduardo Alonso, Alberto Fernández & Jonathan Gray

 * Preliminary version 10-Mar-2005 Esther Mondragón, Eduardo Alonso & Dioysios Skordoulis.
 * Modified October 2009  Esther Mondragón, Eduardo Alonso & Rocío García Durán.
 * Modified July 2011 Esther Mondragón, Eduardo Alonso & Alberto Fernández.
 * Modified October 2012 Esther Mondragón, Eduardo Alonso, Alberto Fernández & Jonathan Gray.
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;


/**
 * City University
 * BSc Computing with Artificial Intelligence
 * Project title: Building a TD Simulator for Real-Time Classical Conditioning
 * @supervisor Dr. Eduardo Alonso
 * @author Jonathan Gray
 **/
public class ContextEditor extends AbstractCellEditor implements ActionListener,
        TableCellEditor {

    /** Context configuration object. **/
    private ContextConfig currentConfig;
    private JButton button;
    /** Context editor dialog. **/
    private ContextDialog dialog;
    protected static final String EDIT = "edit";
    private JTable myTable;

    /**
     * Create a new editor.
     */
    public ContextEditor() {
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);
        dialog = new ContextDialog(this);
// AF Sep-2012
        currentConfig = new ContextConfig();
    }

    /* (non-Javadoc)
    * @see javax.swing.CellEditor#getCellEditorValue()
    */
    @Override
    public Object getCellEditorValue() {
        return currentConfig;
    }

    /**
     * Open an editing dialog when clicked filled with the current configuration,
     * then close it again afterwards.
     */

    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            //The user has clicked the cell, so
            //bring up the dialog.
            dialog.setContextConfig(currentConfig);
            dialog.setVisible(true);

            //dialog.toFront(); // AF Sep-2012. Doesn't work
            // this works
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    dialog.toFront();
                    dialog.repaint();
                }
            });


            fireEditingStopped(); //Make the renderer reappear.

        } else {
            currentConfig = dialog.getConfig();
            dialog.setVisible(false);
            myTable.repaint();
        }
    }

    /* (non-Javadoc)
    * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
    */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        currentConfig = (ContextConfig) value;
        dialog.setTitle(((String) table.getValueAt(row, 0))+" Phase "+(column/5 + 1));
        myTable = table;
        return button;
    }

}
