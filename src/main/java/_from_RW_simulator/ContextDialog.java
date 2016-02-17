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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;


/**
 * City University
 * BSc Computing with Artificial Intelligence
 * Project title: Building a TD Simulator for Real-Time Classical Conditioning
 * @supervisor Dr. Eduardo Alonso
 * @author Jonathan Gray
 **/
public class ContextDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    /** Context configuration object. **/
    private ContextConfig context;

    // AF Sep-2012. Trying to store default button. For some reason it is lost
    JButton defaultButton;

    /**
     * Create the dialog.
     */
    public ContextDialog(ActionListener listener) {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            //Fill with contexts.
            JComboBox typeBox = new JComboBox( ContextConfig.Context.getList() );
            final DefaultCellEditor typeEditor = new DefaultCellEditor( typeBox );

            table = new JTable(new ContextValuesTableModel()){
                private int lastColumnEdited; // AF Aug-2012
                //Overridden to return a combobox for duration type
                public TableCellEditor getCellEditor(int row, int column)
                {
                    int modelColumn = convertColumnIndexToModel( column );

                    if (modelColumn == 0)
                        return typeEditor;
                    else
                        return super.getCellEditor(row, column);
                }
                // AF August-2012
                public boolean editCellAt(int row, int column, EventObject e) {
                    boolean b = super.editCellAt(row, column, e);
                    if (column == 1) {
                        TableCellEditor tce = getCellEditor( row, column);
                        DefaultCellEditor dce = (DefaultCellEditor)tce;
                        Component c = dce.getComponent();
                        JTextComponent jtc = (JTextComponent)c;

                        jtc.setText("");

                        jtc.requestFocus();

                        InputMap inputMap = jtc.getInputMap();
                        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
                        inputMap.put(key, "none"); //$NON-NLS-1$

                    }
                    lastColumnEdited = column; // remember the edited cell
                    return b;
                }
                // AF. Aug-2012. TMP: trying to catch enter
                public void editingStopped(ChangeEvent e) {
                    super.editingStopped(e);

                    if (lastColumnEdited == 1) {
                        TableCellEditor tce = getCellEditor(0,1);
                        DefaultCellEditor dce = (DefaultCellEditor)tce;
                        Component c = dce.getComponent();
                        JTextComponent jtc = (JTextComponent)c;
                        String text = jtc.getText();
                        try {
                            new Double(text);
                        }
                        catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, Messages.getString("ContextDialog.DataFormatError"), Messages.getString("ContextDialog.warningTitle"), JOptionPane.PLAIN_MESSAGE); //NO_OPTION //$NON-NLS-1$
                            TableModel model = table.getModel();
                            model.setValueAt("0",0, 1);
//	 		            	jtc.setText("0");
                        }
                        JRootPane jrp = getRootPane();
                        jrp.setDefaultButton(defaultButton); // AF Sep-2012 to restore the defaultButton. For some reason it was lost.
                        JButton jb = jrp.getDefaultButton();
                        jb.requestFocusInWindow();

//		    			getRootPane().getDefaultButton().requestFocusInWindow();
                    }

                }

            };
            //Make window close on second enter press
            InputMap map = table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            map.remove(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
            //Make single click start editing instead of needing double
            DefaultCellEditor singleClickEditor = new DefaultCellEditor(new JTextField());
            singleClickEditor.setClickCountToStart(1);
            table.setDefaultEditor(Object.class, singleClickEditor);
            table.setCellSelectionEnabled(false);
            JScrollPane scrollPane = new JScrollPane(table);
            contentPanel.add(scrollPane);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                okButton.addActionListener(listener);
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
                // AF Sep-2012
                defaultButton = okButton;
            }
        }
    }

    /**
     * Get the configuration from the table model.
     * @return a new ContextConfig object
     */

    public ContextConfig getConfig() {
        TableModel model = table.getModel();
        context.setContext((ContextConfig.Context) model.getValueAt(0, 0));
        context.setAlpha(Double.parseDouble((String) model.getValueAt(0, 1)));
        context.setSe((double) model.getValueAt(0, 2));
        context.setSi((double) model.getValueAt(0, 3));
        return context;
    }

    /**
     * Fill the model with a context configuration.
     * @param currentConfig the config to fill the table model with
     */

    public void setContextConfig(ContextConfig currentConfig) {
        context = currentConfig;
        ((ContextValuesTableModel)table.getModel()).setValuesTable(true);
    }


    /**
     * Table model for ITI configurations.
     *
     * @author J Gray
     *
     */

    private class ContextValuesTableModel extends ValuesTableModel {

        /**
         * Default constructor.
         */
        public ContextValuesTableModel() {
            super();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            return true;
        }


        /**
         * Initialize the table with default values and labels.
         * @param iniValues
         */

        public void setValuesTable(boolean iniValues) {
            Vector data1 = new Vector();

            try {
                row = 1;
                col = 4;
                columnNames = new String[]{"Context","\u03B1", "Se", "Si"};


                data1.add(new Object[]{context.getContext(), context.getAlpha().floatValue() + "", context.getSe(), context.getSi()});
                setData(data1);
                fireTableChanged(null); // notify everyone that we have a new table.
            }
            catch(Exception e) {
                setData(new Vector()); // blank it out and keep going.
                e.printStackTrace();
            }
        }
    }

}
