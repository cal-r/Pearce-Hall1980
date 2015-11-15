package _UnitTests;

import Helpers.SimulatorBuilder;
import Models.Simulator;
import ViewModels.TrailTableModel;
import org.junit.Test;

/**
 * Created by Rokas on 14/11/2015.
 */
public class SimulatorBuilderTests extends junit.framework.TestCase {

    @Test
    public void testSimBuilder1() throws Exception{
        TrailTableModel tableModel = new TrailTableModel();
        tableModel.setValueAt("group name test", 0, 0);
        tableModel.setValueAt("2AAB+", 0, 1);
        Simulator sim = SimulatorBuilder.build(tableModel);
        assertTrue(sim!=null);
        assertTrue(sim.getGroups().size() == 1);
        assertTrue(sim.getGroups().get(0).Name == "group name test");
        assertTrue(sim.getCsParameters().size()==6);
        assertTrue(sim.getCsParameters().get(0).CueName == 'A');
        assertTrue(sim.getCsParameters().get(1).CueName == 'B');
    }
}
