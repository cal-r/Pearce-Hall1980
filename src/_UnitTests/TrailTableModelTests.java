package _UnitTests;

import Constants.TableStringConstants;
import ViewModels.TrailTableModel;
import org.junit.Test;

/**
 * Created by Rokas on 15/11/2015.
 */
public class TrailTableModelTests  extends junit.framework.TestCase {

    @Test
    public void testTrailTableModel() throws Exception{
        TrailTableModel tableModel = new TrailTableModel();
        assertEquals(tableModel.getPhaseCount(), 1);
        assertEquals(tableModel.getGroupCount(), 1);
        assertEquals(tableModel.getGroupName(0), TableStringConstants.getDefaultGroupName(0));
        assertEquals(tableModel.getPhaseDescriptions(0).size(), 1);
        assertEquals(tableModel.getPhaseDescriptions(0).get(0), TableStringConstants.DEFAULT_PHASE);
    }

    @Test
    public void testTrailTableModel1() throws Exception {
        TrailTableModel tableModel = new TrailTableModel();
        tableModel.addPhase();
        assertEquals(tableModel.getPhaseCount(), 2);
        assertEquals(tableModel.getGroupCount(), 1);
        assertEquals(tableModel.getGroupName(0), TableStringConstants.getDefaultGroupName(0));
        assertEquals(tableModel.getPhaseDescriptions(0).size(), 2);
        assertEquals(tableModel.getPhaseDescriptions(0).get(0), TableStringConstants.DEFAULT_PHASE);
        assertEquals(tableModel.getPhaseDescriptions(0).get(1), TableStringConstants.DEFAULT_PHASE);
    }

    @Test
    public void testTrailTableModel2() throws Exception {
        TrailTableModel tableModel = new TrailTableModel();
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
        assertEquals(tableModel.getPhaseDescriptions(0).get(0), TableStringConstants.DEFAULT_PHASE);
        assertEquals(tableModel.getPhaseDescriptions(0).get(1), TableStringConstants.DEFAULT_PHASE);
    }
}

