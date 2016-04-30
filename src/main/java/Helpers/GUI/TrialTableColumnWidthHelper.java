package Helpers.GUI;

import Models.SimulatorSettings;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableColumnModel;

/**
 * Created by Rokas on 30/04/2016.
 */
public class TrialTableColumnWidthHelper implements TableColumnModelListener {
    private TableColumnModel columnModel;
    private JTable table;
    private SimulatorSettings settings;

    public TrialTableColumnWidthHelper(TableColumnModel columnModel, JTable table, SimulatorSettings settings){
        this.columnModel = columnModel;
        this.table = table;
        this.settings = settings;
    }

    @Override
    public void columnAdded(TableColumnModelEvent e) {
        columnModel.getColumn(0).setMaxWidth(100);
        for(int colId=1;colId<columnModel.getColumnCount();colId++){
            if(isRandomColumn(colId) || isItiCsRatioColumn(colId) || isContextSymbolColumn(colId)){
                columnModel.getColumn(colId).setMaxWidth(50);
            }
        }
    }

    private boolean isRandomColumn(int colId){
        if(colId == 0)
            return false;
        if(!settings.isContextSimulation())
            return colId % 2 == 0;
        // 2 6 10 14
        return (colId - 2) % 4 == 0;
    }

    private boolean isItiCsRatioColumn(int colId){
        return settings.isContextSimulation() && colId % 4 == 0;
    }

    private boolean isContextSymbolColumn(int colId){
        return settings.isContextSimulation() && (colId+1) % 4 == 0;
    }

    @Override
    public void columnRemoved(TableColumnModelEvent e) {

    }

    @Override
    public void columnMoved(TableColumnModelEvent e) {

    }

    @Override
    public void columnMarginChanged(ChangeEvent e) {

    }

    @Override
    public void columnSelectionChanged(ListSelectionEvent e) {

    }
}
