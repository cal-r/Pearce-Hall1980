package com;

import Constants.GuiStringConstants;
import ViewModels.TableModels.TrialTableModel;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Rokas on 15/11/2015.
 */
public class TrialTableModelTests  extends TestCase {

    @Test
    public void testTrialTableModel() throws Exception{
        TrialTableModel tableModel = new TrialTableModel(false);
        assertEquals(tableModel.getPhaseCount(), 1);
        assertEquals(tableModel.getGroupCount(), 1);
        assertEquals(tableModel.getGroupName(0), GuiStringConstants.getDefaultGroupName(0));
        assertEquals(tableModel.getPhaseDescriptions(0).size(), 1);
        assertEquals(tableModel.getPhaseDescriptions(0).get(0), GuiStringConstants.DEFAULT_PHASE);
    }

    @Test
    public void testTrialTableModel1() throws Exception {
        TrialTableModel tableModel = new TrialTableModel(false);
        tableModel.addPhase();
        assertEquals(tableModel.getPhaseCount(), 2);
        assertEquals(tableModel.getGroupCount(), 1);
        assertEquals(tableModel.getGroupName(0), GuiStringConstants.getDefaultGroupName(0));
        assertEquals(tableModel.getPhaseDescriptions(0).size(), 2);
        assertEquals(tableModel.getPhaseDescriptions(0).get(0), GuiStringConstants.DEFAULT_PHASE);
        assertEquals(tableModel.getPhaseDescriptions(0).get(1), GuiStringConstants.DEFAULT_PHASE);
    }

    @Test
    public void testTrialTableModel2() throws Exception {
        TrialTableModel tableModel = new TrialTableModel(false);
        tableModel.addPhase();
        tableModel.addGroup();
        String group1Name = "testin group1";
        String group2Name = "testin group2";
        tableModel.setValueAt(group1Name, 0, 0);
        tableModel.setValueAt(group2Name, 1, 0);
        assertEquals(tableModel.getPhaseCount(), 2);
        assertEquals(tableModel.getGroupCount(), 2);
        assertEquals(tableModel.getGroupName(0), group1Name);
        assertEquals(tableModel.getGroupName(1), group2Name);
        assertEquals(tableModel.getPhaseDescriptions(0).size(), 2);
        assertEquals(tableModel.getPhaseDescriptions(0).get(0), GuiStringConstants.DEFAULT_PHASE);
        assertEquals(tableModel.getPhaseDescriptions(0).get(1), GuiStringConstants.DEFAULT_PHASE);
    }
}

